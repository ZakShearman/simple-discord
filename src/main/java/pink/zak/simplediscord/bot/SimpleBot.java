package pink.zak.simplediscord.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;
import pink.zak.simplediscord.command.CommandBase;
import pink.zak.simplediscord.command.command.SimpleCommand;
import pink.zak.simplediscord.config.ConfigStore;
import pink.zak.simplediscord.registry.Registry;
import pink.zak.simplediscord.storage.BackendFactory;
import pink.zak.simplediscord.storage.StorageSettings;
import pink.zak.simplediscord.storage.mongo.MongoConnectionFactory;

import java.nio.file.Path;
import java.util.Set;

public interface SimpleBot {

    void unload();

    void initialize(String token, String prefix, Set<GatewayIntent> intents);

    void registerRegistries(Registry... registries);

    void registerCommands(SimpleCommand... commands);

    void registerListeners(Object... listeners);

    boolean isInitialized();

    StorageSettings getStorageSettings();

    BackendFactory getBackendFactory();

    MongoConnectionFactory getMongoConnectionFactory();

    Path getBasePath();

    String getPrefix();

    CommandBase getCommandBase();

    ConfigStore getConfigStore();

    JDA getJda();
}
