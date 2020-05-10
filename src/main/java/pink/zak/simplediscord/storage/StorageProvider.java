package pink.zak.simplediscord.storage;


import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.storage.storage.Storage;

import java.util.function.Function;

public class StorageProvider {

    public static <T> Storage<T> provide(SimpleBot plugin, Function<BackendFactory, Backend> backend, Function<Backend, Storage<T>> instance) {
        return instance.apply(backend.apply(new BackendFactory(plugin)));
    }

    public static <T> Storage<T> provide(BackendFactory backendFactory, Function<BackendFactory, Storage<T>> instance) {
        return instance.apply(backendFactory);
    }
}
