package pink.zak.simplediscord.storage.mongo;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.cache.options.CacheStorage;

import java.util.Map;
import java.util.Set;

public abstract class MongoStorage<K, T> implements CacheStorage<K, T> {
    private final MongoCollection<Document> collection;
    private final String idKey;

    public MongoStorage(SimpleBot bot, String collectionName, String idKey) {
        this.collection = bot.getMongoConnectionFactory().getCollection(collectionName);
        this.idKey = idKey;
    }
    public abstract MongoSerializer<T> serializer();

    public abstract MongoDeserializer<T> deserializer();

    public abstract T create(K id);

    public T load(K primaryKey, Map<String, Object> keyValues) {
        Document foundDocument = this.find(new BasicDBObject(keyValues));
        if (foundDocument == null) {
            return this.create(primaryKey);
        }
        return this.deserializer().apply(foundDocument);
    }

    public T load(String key, Object value) {
        Document foundDocument = this.find(Filters.eq(key, value));
        if (foundDocument == null) {
            return null;
        }
        return this.deserializer().apply(foundDocument);
    }

    public T load(K key) {
        T loaded = this.load(this.idKey, key);
        if (loaded == null) {
            return this.create(key);
        }
        return this.load(this.idKey, key);
    }

    public Set<T> loadAll() {
        Set<T> loaded = Sets.newHashSet();
        for (Document document : this.collection.find()) {
            loaded.add(this.deserializer().apply(document));
        }
        return loaded;
    }

    public void save(Bson filter, Document document) {
        if (this.find(filter) == null) {
            this.collection.insertOne(document);
        } else {
            this.collection.findOneAndReplace(filter, document);
        }
    }

    public void save(T type) {
        Document document = this.serializer().apply(type, new Document());
        this.save(Filters.eq(this.idKey, document.get(this.idKey)), document);
    }

    public void save(Map<String, Object> keyValues, T type) {
        Document document = this.serializer().apply(type, new Document());
        this.save(new BasicDBObject(keyValues), document);
    }

    public void delete(Bson filter) {
        this.collection.deleteOne(filter);
    }

    public void delete(K key) {
        this.delete(Filters.eq(this.idKey, key));
    }

    public void delete(Map<String, Object> keyValues) {
        this.delete((Bson) new BasicDBObject(keyValues));
    }

    private Document find(Bson filter) {
        return this.collection.find(filter).first();
    }
}
