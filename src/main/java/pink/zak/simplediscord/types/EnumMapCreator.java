package pink.zak.simplediscord.types;

import com.google.common.collect.Maps;
import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;
import java.util.EnumMap;

public class EnumMapCreator<K extends Enum<K>, V> implements InstanceCreator<EnumMap<K, V>> {
    private final Class<K> clazz;

    public EnumMapCreator(Class<K> clazz) {
        this.clazz = clazz;
    }

    @Override
    public EnumMap<K, V> createInstance(Type type) {
        return Maps.newEnumMap(this.clazz);
    }
}
