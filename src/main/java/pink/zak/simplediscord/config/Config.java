package pink.zak.simplediscord.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.timvisee.yamlwrapper.ConfigurationSection;
import com.timvisee.yamlwrapper.YamlConfiguration;
import pink.zak.simplediscord.bot.SimpleBot;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

public class Config {
    private final SimpleBot bot;
    private final File file;
    private final boolean reloadable;
    private YamlConfiguration configuration;
    private Map<String, Object> valueMap;
    private Set<String> enduringKeys;

    public Config(SimpleBot bot, UnaryOperator<Path> path, boolean reloadable, String... enduringKeys) {
        this.bot = bot;
        this.file = path.apply(this.bot.getBasePath()).toFile();
        this.reloadable = reloadable;
        this.enduringKeys = Sets.newHashSet(enduringKeys);
        this.createIfAbsent(path.apply(Paths.get("")).toString());
        this.reload();
        this.load();
    }

    public Config(SimpleBot bot, File file, boolean reloadable, String... enduringKeys) {
        this.bot = bot;
        this.file = file;
        this.reloadable = reloadable;
        this.enduringKeys = Sets.newHashSet(enduringKeys);
        this.reload();
        this.load();
    }

    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    public boolean isReloadable() {
        return this.reloadable;
    }

    public String string(String key) {
        return (String) this.get(key);
    }

    public Boolean bool(String key) {
        Object object = this.get(key);
        return (object instanceof Boolean) ? (Boolean) object : false;
    }

    public int integer(String key) {
        Object object = this.get(key);
        return object instanceof Number ? ((Number) object).intValue() : -1;
    }

    public Double doubl(String key) {
        Object object = this.get(key);
        return object instanceof Number ? ((Number) object).doubleValue() : -1;
    }

    @SuppressWarnings("unchecked")
    public List<String> stringList(String key) {
        Object object = this.get(key);
        return object instanceof List ? (List<String>) object : Lists.newArrayList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(String key) {
        Object object = this.get(key);
        return object instanceof List ? (List<T>) object : Lists.newArrayList();
    }

    public Set<String> keys(String key) {
        ConfigurationSection configurationSection = this.configuration.getConfigurationSection(key);
        if (configurationSection == null) {
            return null;
        }
        return Sets.newHashSet(configurationSection.getKeys(key));
    }

    public Object get(String key) {
        return this.valueMap.getOrDefault(key, null);
    }

    public synchronized void load() {
        boolean isReload = true;
        if (this.valueMap == null) {
            this.valueMap = Maps.newHashMap();
            isReload = false;
        }
        for (String key : this.configuration.getKeys("")) {
            if (isReload && this.enduringKeys.contains(key)) {
                continue;
            }
            this.valueMap.put(key, this.configuration.get(key));
        }
    }

    public void reload() {
        this.configuration = YamlConfiguration.loadFromFile(this.file);
        this.load();
    }

    private void createIfAbsent(String file) {
        if (!this.file.exists()) {
            this.bot.getBasePath().toFile().mkdirs();
            this.bot.saveResource(file, false);
        }
    }
}