package pink.zak.simplediscord.cache.options;

public interface CacheExpiryListener<K, V> {

    void onExpiry(K key, V value);
}
