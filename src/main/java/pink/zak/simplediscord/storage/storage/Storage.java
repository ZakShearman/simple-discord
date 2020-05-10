package pink.zak.simplediscord.storage.storage;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.storage.Backend;
import pink.zak.simplediscord.storage.BackendFactory;
import pink.zak.simplediscord.storage.adapter.Adapter;
import pink.zak.simplediscord.storage.storage.load.Deserializer;
import pink.zak.simplediscord.storage.storage.load.Serializer;

import java.util.Set;
import java.util.function.Function;

public abstract class Storage<T> {
    private Backend backend;
    private GsonBuilder gsonBuilder;
    private Gson gson;

    public Storage(SimpleBot plugin, Function<BackendFactory, Backend> backend) {
        this.backend = backend.apply(new BackendFactory(plugin));
        this.gsonBuilder = new GsonBuilder();
        this.gson = new Gson();
    }

    public Storage(Backend backend) {
        this.backend = backend;
    }

    public abstract Serializer<T> serializer();

    public abstract Deserializer<T> deserializer();

    public void addAdapter(Class<?> clazz, Adapter<?> adapter) {
        this.gsonBuilder.registerTypeAdapter(clazz, adapter);
    }

    public void saveChanges() {
        this.gson = this.gsonBuilder.create();
    }

    public T load(String id) {
        JsonObject json = this.backend.load(id);
        return json == null ? null : this.deserializer().apply(json, this.gson);
    }

    public T save(String id, T object) {
        this.backend.save(id, this.serializer().apply(object, new JsonObject(), this.gson));
        return object;
    }

    public Set<T> loadAll() {
        Set<T> all = Sets.newHashSet();
        for (JsonObject json : this.backend.loadAll()) {
            all.add(this.deserializer().apply(json, this.gson));
        }
        return all;
    }

    public void closeBack() {
        this.backend.close();
    }
}
