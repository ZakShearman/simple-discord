package pink.zak.simplediscord.command.command;

import com.google.common.collect.Sets;
import net.dv8tion.jda.api.entities.Role;
import pink.zak.simplediscord.bot.SimpleBot;

import java.util.Arrays;
import java.util.Set;

public abstract class SimpleCommand extends Command {
    private final String command;
    private Set<String> aliases;
    private Set<SubCommand> subCommands = Sets.newLinkedHashSet();

    public SimpleCommand(SimpleBot bot, String command, Role role, boolean allowBots) {
        super(bot, role, allowBots);
        this.command = command;
    }

    public SimpleCommand(SimpleBot bot, String command, boolean allowBots) {
        this(bot, command, null, allowBots);
    }

    public SimpleCommand(SimpleBot bot, String command, Role role) {
        this(bot, command, role, true);
    }

    public SimpleCommand(SimpleBot bot, String command) {
        this(bot, command, true);
    }

    public String getCommand() {
        return this.command;
    }

    public Set<SubCommand> getSubCommands() {
        return this.subCommands;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public void setAliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public Set<String> getAliases() {
        return this.aliases;
    }

    public void setSubCommands(Set<SubCommand> subCommands) {
        this.subCommands = subCommands;
    }

    protected void setSubCommands(SubCommand... subCommands) {
        this.subCommands.addAll(Arrays.asList(subCommands));
    }
}