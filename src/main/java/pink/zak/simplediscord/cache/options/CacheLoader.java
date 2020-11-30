package pink.zak.simplediscord.cache.options;

public interface CacheLoader<K, V> {

    V load(K key);
}
