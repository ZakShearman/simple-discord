package pink.zak.simplediscord.command.command;

import com.google.common.collect.Sets;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import pink.zak.simplediscord.bot.SimpleBot;

import java.util.Arrays;
import java.util.Set;

public abstract class SimpleCommand<T extends User> extends Command<T> {
    private final String command;
    private Set<SubCommand<? extends User>> subCommands = Sets.newLinkedHashSet();

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

    public Set<SubCommand<? extends User>> getSubCommands() {
        return this.subCommands;
    }

    public void setSubCommands(Set<SubCommand<? extends User>> subCommands) {
        this.subCommands = subCommands;
    }

    protected void setSubCommands(SubCommand<? extends User>... subCommands) {
        this.subCommands.addAll(Arrays.asList(subCommands));
    }
}