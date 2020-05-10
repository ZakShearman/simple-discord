package pink.zak.simplediscord.command.command.compact;

import com.google.common.collect.Sets;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.argument.Argument;
import pink.zak.simplediscord.command.command.SubCommand;

import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

public class SubChain {
    private Set<SubCommand> subCommands = Sets.newHashSet();

    public Set<SubCommand> getSubCommands() {
        return this.subCommands;
    }

    public SubChain newSub(SimpleBot bot, Role role, boolean allowBots, UnaryOperator<ArgumentBuilder> builder, Executor executor) {
        List<Argument<?>> arguments = builder.apply(new ArgumentBuilder()).getArguments();
        SubCommand subCommand = new SubCommand(bot, role, allowBots) {

            @Override
            public void onExecute(Member sender, CommandContainer container, String[] args) {
                executor.execute(sender, args, null);
            }
        };
        subCommand.setArguments(arguments);
        this.subCommands.add(subCommand);
        return this;
    }

    public SubChain newSub(SimpleBot bot, UnaryOperator<ArgumentBuilder> builder, Executor executor) {
        return this.newSub(bot, null, true, builder, executor);
    }

    public SubChain newSub(SimpleBot bot, Role role, UnaryOperator<ArgumentBuilder> builder, Executor executor) {
        return this.newSub(bot, role, true, builder, executor);
    }

    public SubChain newSub(SimpleBot bot, boolean isConsole, UnaryOperator<ArgumentBuilder> builder, Executor executor) {
        return this.newSub(bot, null, isConsole, builder, executor);
    }
}
