package pink.zak.simplediscord.cache.singular;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CachedValue<V> {
    private final Supplier<V> valueSupplier;
    private V value;

    private final TimeUnit timeUnit;
    private final int delay;
    private long lastUpdateTime;

    public CachedValue(TimeUnit timeUnit, int delay, Supplier<V> valueSupplier) {
        this.valueSupplier = valueSupplier;
        this.value = valueSupplier.get();
        this.timeUnit = timeUnit;
        this.delay = delay;
    }

    public V get() {
        if (System.currentTimeMillis() - lastUpdateTime > this.timeUnit.toMillis(delay)) {
            return this.updateAndGet();
        }
        return this.value;
    }

    public V updateAndGet() {
        this.value = this.valueSupplier.get();
        this.lastUpdateTime = System.currentTimeMillis();
        return this.value;
    }

    public V getAndUpdate() {
        V initialValue = this.value;
        this.value = this.valueSupplier.get();
        this.lastUpdateTime = System.currentTimeMillis();
        return initialValue;
    }
}
