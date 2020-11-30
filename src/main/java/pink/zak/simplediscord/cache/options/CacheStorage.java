package pink.zak.simplediscord.cache.options;

import java.util.Map;

public interface CacheStorage<K, T> {

    T load(K primaryKey, Map<String, Object> keyValues);

    T load(K value);

    void save(T type);

    void save(Map<String, Object> keyValues, T type);
}
