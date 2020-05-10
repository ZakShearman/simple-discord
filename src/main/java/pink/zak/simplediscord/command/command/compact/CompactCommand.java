package pink.zak.simplediscord.command.command.compact;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.command.command.SimpleCommand;

import java.util.function.UnaryOperator;

public abstract class CompactCommand<T extends User> extends SimpleCommand<T> {

    public CompactCommand(SimpleBot bot, String command, Role role, boolean allowBots) {
        super(bot, command, role, allowBots);
    }

    public CompactCommand(SimpleBot bot, String command, boolean allowBots) {
        super(bot, command, allowBots);
    }

    public CompactCommand(SimpleBot bot, String command, Role role) {
        super(bot, command, role);
    }

    public CompactCommand(SimpleBot bot, String command) {
        super(bot, command);
    }

    public void subChain(UnaryOperator<SubChain<? extends User>> operator) {
        SubChain<? extends User> subChain = operator.apply(new SubChain<T>());
        this.setSubCommands(subChain.getSubCommands());
    }
}
