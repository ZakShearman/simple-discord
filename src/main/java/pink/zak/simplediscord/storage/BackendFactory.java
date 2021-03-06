package pink.zak.simplediscord.storage;

import com.google.common.collect.Maps;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.storage.backends.FlatBackend;
import pink.zak.simplediscord.storage.backends.mysql.MySqlBackend;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class BackendFactory {
    private final SimpleBot bot;
    private Map<String, BiFunction<UnaryOperator<Path>, String, Backend>> backendMap = Maps.newConcurrentMap();

    public BackendFactory(SimpleBot bot) {
        this.bot = bot;
        this.addBackend("mysql", destination -> new MySqlBackend(this.bot, destination));
    }

    public Backend create(String backendType, UnaryOperator<Path> path, String destination) {
        for (Map.Entry<String, BiFunction<UnaryOperator<Path>, String, Backend>> backendEntry : backendMap.entrySet()) {
            if (backendEntry.getKey().toLowerCase().equalsIgnoreCase(backendType)) {
                return backendEntry.getValue().apply(path, destination);
            }
        }
        return new FlatBackend(path.apply(this.bot.getBasePath().toAbsolutePath()).resolve(destination));
    }

    public Backend create(String backendType, String destination) {
        return this.create(backendType, path -> path, destination);
    }

    public Backend create(String backendType, UnaryOperator<Path> path) {
        return this.create(backendType, path, "");
    }

    public BackendFactory addBackend(String name, BiFunction<UnaryOperator<Path>, String, Backend> backend) {
        this.backendMap.put(name, backend);
        return this;
    }

    public BackendFactory addBackend(String name, Function<String, Backend> backend) {
        this.addBackend(name, (path, destination) -> backend.apply(destination));
        return this;
    }

    public BackendFactory addBackendAsPath(String name, Function<UnaryOperator<Path>, Backend> backend) {
        this.addBackend(name, (path, destination) -> backend.apply(path));
        return this;
    }
}
