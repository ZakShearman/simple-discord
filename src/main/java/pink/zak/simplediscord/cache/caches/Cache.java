package pink.zak.simplediscord.cache.caches;

import pink.zak.simplediscord.cache.options.CacheStorage;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Cache<K, V> {
    protected final ConcurrentHashMap<K, V> cacheMap = new ConcurrentHashMap<>();

    protected final ExecutorService executor;
    protected final Consumer<V> removalAction;
    protected final CacheStorage<K, V> storage;
    protected final AtomicInteger hits = new AtomicInteger();
    protected final AtomicInteger loads = new AtomicInteger();

    public Cache() {
        this(null, null, null);
    }

    public Cache(Consumer<V> removalAction, CacheStorage<K, V> storage, ExecutorService executorService) {
        this(removalAction, storage, executorService, null, null, 0);
    }

    public Cache(Consumer<V> removalAction, CacheStorage<K, V> storage, ExecutorService executor, ScheduledExecutorService scheduledExecutor, TimeUnit autoSaveTimeUnit, int autoSaveInterval) {
        this.executor = executor;
        this.removalAction = removalAction;
        this.storage = storage;
        if (autoSaveTimeUnit != null && autoSaveInterval > 0) {
            this.startAutoSave(scheduledExecutor, autoSaveTimeUnit, autoSaveInterval);
        }
    }

    public V getSync(K key) {
        V retrieved = this.cacheMap.get(key);
        this.hits.incrementAndGet();
        if (retrieved == null) {
            if (this.storage == null) {
                return null;
            }
            V loaded = this.storage.load(key);
            this.loads.incrementAndGet();
            if (loaded != null) {
                return this.set(key, loaded);
            }
            return null;
        }
        return retrieved;
    }

    public CompletableFuture<V> get(K key) {
        return CompletableFuture.supplyAsync(() -> this.getSync(key), this.executor);
    }

    public V set(K key, V value) {
        return this.cacheMap.put(key, value);
    }

    public boolean contains(K key) {
        return this.cacheMap.containsKey(key);
    }

    public void save(K key) {
        this.storage.save(this.getSync(key));
    }

    public V invalidate(K key) {
        if (this.storage != null) {
            this.save(key);
        }
        if (this.removalAction != null) {
            this.removalAction.accept(this.cacheMap.get(key));
        }
        return this.cacheMap.remove(key);
    }

    public V invalidate(K key, boolean save) {
        if (save) {
            this.invalidate(key);
            return null;
        }
        return this.cacheMap.remove(key);
    }

    public CompletableFuture<V> invalidateAsync(K key) {
        return this.invalidateAsync(key, true);
    }

    public CompletableFuture<V> invalidateAsync(K key, boolean save) {
        return CompletableFuture.supplyAsync(() -> this.invalidate(key, save), this.executor);
    }

    public void invalidateAll() {
        if (this.storage == null) {
            this.cacheMap.clear();
            return;
        }
        for (K key : this.cacheMap.keySet()) {
            this.invalidate(key);
        }
    }

    public CompletableFuture<Void> invalidateAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            this.invalidateAll();
            return null;
        }, this.executor);
    }

    public int size() {
        return this.cacheMap.size();
    }

    public ConcurrentHashMap<K, V> getMap() {
        return this.cacheMap;
    }

    public Cache<K, V> getCache() {
        return this;
    }

    public AtomicInteger getHits() {
        return this.hits;
    }

    public void resetHits() {
        this.hits.set(0);
    }

    public AtomicInteger getLoads() {
        return this.loads;
    }

    public void resetLoads() {
        this.loads.set(0);
    }

    private void startAutoSave(ScheduledExecutorService scheduledExecutor, TimeUnit timeUnit, int interval) {
        scheduledExecutor.scheduleAtFixedRate(() -> {
            this.executor.submit(() -> {
                for (K key : this.cacheMap.keySet()) {
                    this.save(key);
                }
            });
        }, interval, interval, timeUnit);
    }
}
