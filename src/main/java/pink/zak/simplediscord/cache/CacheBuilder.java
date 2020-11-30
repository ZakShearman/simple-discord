package pink.zak.simplediscord.cache;

import pink.zak.simplediscord.cache.caches.AccessExpiringCache;
import pink.zak.simplediscord.cache.caches.Cache;
import pink.zak.simplediscord.cache.caches.WriteExpiringCache;
import pink.zak.simplediscord.cache.options.CacheExpiryListener;
import pink.zak.simplediscord.cache.options.CacheStorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CacheBuilder<K, V> {
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutor;
    private CacheExpiryListener<K, V> expiryListener;
    private Consumer<V> removalAction;
    private CacheStorage<K, V> storage;
    private TimeUnit autoSaveTimeUnit;
    private int autoSaveInterval;
    private TimeUnit expiryTimeUnit;
    private int expiryDelay;
    private boolean expireAfterAccess;

    public Cache<K, V> build() {
        if (this.expiryTimeUnit != null && this.expiryDelay > 0) {
            if (this.expireAfterAccess) {
                return new AccessExpiringCache<>(this.storage, this.expiryListener, this.executorService, this.scheduledExecutor, this.removalAction, this.expiryTimeUnit, this.expiryDelay, this.autoSaveTimeUnit, this.autoSaveInterval);
            }
            return new WriteExpiringCache<>(this.storage, this.expiryListener, this.executorService, this.scheduledExecutor, this.removalAction, this.expiryTimeUnit, this.expiryDelay, this.autoSaveTimeUnit, this.autoSaveInterval);
        }
        return new Cache<>(this.removalAction, this.storage, this.executorService, this.scheduledExecutor, this.autoSaveTimeUnit, this.autoSaveInterval);
    }

    public CacheBuilder<K, V> setExecutorService(ScheduledExecutorService scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
        return this;
    }

    public CacheBuilder<K, V> setScheduledExecutor(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public CacheBuilder<K, V> setExpiryListener(CacheExpiryListener<K, V> expiryListener) {
        this.expiryListener = expiryListener;
        return this;
    }

    public CacheBuilder<K, V> setRemovalAction(Consumer<V> operator) {
        this.removalAction = operator;
        return this;
    }

    public CacheBuilder<K, V> expireAfterAccess(int delay, TimeUnit timeUnit) {
        this.expiryTimeUnit = timeUnit;
        this.expiryDelay = delay;
        this.expireAfterAccess = true;
        return this;
    }

    public CacheBuilder<K, V> expireAfterWrite(int delay, TimeUnit timeUnit) {
        this.expiryTimeUnit = timeUnit;
        this.expiryDelay = delay;
        this.expireAfterAccess = false;
        return this;
    }

    public CacheBuilder<K, V> autoSave(int delay, TimeUnit timeUnit) {
        this.autoSaveTimeUnit = timeUnit;
        this.autoSaveInterval = delay;
        return this;
    }

    public CacheBuilder<K, V> setStorage(CacheStorage<K, V> storage) {
        this.storage = storage;
        return this;
    }
}
