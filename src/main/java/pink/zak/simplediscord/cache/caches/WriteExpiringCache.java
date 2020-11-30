package pink.zak.simplediscord.cache.caches;

import pink.zak.simplediscord.cache.options.CacheExpiryListener;
import pink.zak.simplediscord.cache.options.CacheStorage;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class WriteExpiringCache<K, V> extends Cache<K, V> {
    protected final Map<K, Long> writeTimes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutor;
    private final CacheExpiryListener<K, V> expiryListener;
    private final TimeUnit timeUnit;
    private final int delay;

    public WriteExpiringCache(CacheStorage<K, V> storage, CacheExpiryListener<K, V> expiryListener, ExecutorService executorService, ScheduledExecutorService scheduledExecutor,
                              Consumer<V> removalAction, TimeUnit timeUnit, int delay, TimeUnit autoSaveUnit,
                              int autoSaveInterval) {
        super(removalAction, storage, executorService, scheduledExecutor, autoSaveUnit, autoSaveInterval);
        this.scheduledExecutor = scheduledExecutor;
        this.expiryListener = expiryListener;
        this.timeUnit = timeUnit;
        this.delay = delay;

        this.startScheduledCleanup();
    }

    public WriteExpiringCache(CacheStorage<K, V> storage, CacheExpiryListener<K, V> expiryListener, ExecutorService executorService, ScheduledExecutorService scheduledExecutor,
                              Consumer<V> removalAction, TimeUnit timeUnit, int delay) {
        this(storage, expiryListener, executorService, scheduledExecutor, removalAction, timeUnit, delay, null, 0);
    }

    public WriteExpiringCache(CacheStorage<K, V> storage, ExecutorService executorService, ScheduledExecutorService scheduledExecutor,
                              TimeUnit timeUnit, int delay, TimeUnit autoSaveUnit, int autoSaveInterval) {
        this(storage, null, executorService, scheduledExecutor, null, timeUnit, delay, autoSaveUnit, autoSaveInterval);
    }

    public WriteExpiringCache(CacheStorage<K, V> storage, ExecutorService executorService, ScheduledExecutorService scheduledExecutor, TimeUnit timeUnit, int delay) {
        this(storage, null, executorService, scheduledExecutor, null, timeUnit, delay, null, 0);
    }

    @Override
    public V set(K key, V value) {
        this.writeTimes.put(key, System.currentTimeMillis());
        return super.set(key, value);
    }

    @Override
    public V getSync(K key) {
        V retrieved = super.getSync(key);
        return this.getAndCheck(key, retrieved);
    }

    @Override
    public CompletableFuture<V> get(K key) {
        return super.get(key).thenApply(retrieved -> this.getAndCheck(key, retrieved))
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    private V getAndCheck(K key, V retrieved) {
        if (retrieved == null) {
            return null;
        }
        if (System.currentTimeMillis() - this.writeTimes.get(key) > this.timeUnit.toMillis(this.delay)) {
            this.invalidate(key);
            return null;
        }
        return retrieved;
    }

    @Override
    public V invalidate(K key) {
        this.writeTimes.remove(key);
        if (this.expiryListener != null) {
            this.expiryListener.onExpiry(key, this.getSync(key));
        }
        return super.invalidate(key);
    }

    @Override
    public V invalidate(K key, boolean save) {
        this.writeTimes.remove(key);
        if (save && this.expiryListener != null) {
            this.expiryListener.onExpiry(key, this.getSync(key));
        }
        return super.invalidate(key, save);
    }

    @Override
    public void invalidateAll() {
        this.writeTimes.clear();
        super.invalidateAll();
    }

    @Override
    public CompletableFuture<Void> invalidateAllAsync() {
        this.writeTimes.clear();
        return super.invalidateAllAsync();
    }

    private void startScheduledCleanup() {
        this.scheduledExecutor.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            for (Map.Entry<K, Long> entry : this.writeTimes.entrySet()) {
                if (currentTime - entry.getValue() > this.timeUnit.toMillis(this.delay)) {
                    this.invalidate(entry.getKey());
                }
            }
        }, 5, 5, TimeUnit.MINUTES);
    }
}
