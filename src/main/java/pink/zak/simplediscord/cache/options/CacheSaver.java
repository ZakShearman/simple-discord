package pink.zak.simplediscord.cache.options;

public interface CacheSaver<K, V> {

    void save(K key, V value);
}
