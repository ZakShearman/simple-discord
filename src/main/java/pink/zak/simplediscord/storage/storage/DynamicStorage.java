package pink.zak.simplediscord.storage.storage;


import pink.zak.simplediscord.storage.Backend;

public abstract class DynamicStorage<T> extends Storage<T> {

    public DynamicStorage(Backend backend) {
        super(backend);
    }
}
