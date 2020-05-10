package pink.zak.simplediscord.storage.backends;

import com.google.gson.JsonObject;
import pink.zak.simplediscord.storage.Backend;

import java.util.Set;

public class MongoBackend implements Backend {

    @Override
    public JsonObject load(String id) {
        return null;
    }

    @Override
    public void save(String id, JsonObject json) {

    }

    @Override
    public Set<JsonObject> loadAll() {
        return null;
    }

    @Override
    public void close() {

    }
}
