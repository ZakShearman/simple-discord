package pink.zak.simplediscord.bot;

import net.dv8tion.jda.api.JDA;
import pink.zak.simplediscord.command.CommandBase;
import pink.zak.simplediscord.command.command.SimpleCommand;
import pink.zak.simplediscord.config.ConfigStore;

import java.nio.file.Path;

public interface SimpleBot {

    void initialize(String token, String prefix);

    void registerCommands(SimpleCommand... commands);

    void registerListeners(Object... listeners);

    void saveResource(String name, boolean override);

    boolean isInitialized();

    Path getBasePath();

    String getPrefix();

    CommandBase getCommandBase();

    ConfigStore getConfigStore();

    JDA getJda();
}
