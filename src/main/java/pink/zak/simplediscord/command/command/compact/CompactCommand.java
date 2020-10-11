package pink.zak.simplediscord.command.command.compact;

import net.dv8tion.jda.api.entities.Role;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.command.command.SimpleCommand;

import java.util.function.UnaryOperator;

public abstract class CompactCommand extends SimpleCommand {

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

    public void subChain(UnaryOperator<SubChain> operator) {
        SubChain subChain = operator.apply(new SubChain());
        this.setSubCommands(subChain.getSubCommands());
    }
}
