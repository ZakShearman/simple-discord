package pink.zak.simplediscord.command.command.compact;

import com.google.common.collect.Sets;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.argument.Argument;
import pink.zak.simplediscord.command.command.SubCommand;

import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

public class SubChain<T extends User> {
    private Set<SubCommand<? extends User>> subCommands = Sets.newHashSet();

    public Set<SubCommand<? extends User>> getSubCommands() {
        return this.subCommands;
    }

    public SubChain<T> newSub(SimpleBot bot, Role role, boolean allowBots, UnaryOperator<ArgumentBuilder> builder, Executor executor) {
        List<Argument<?>> arguments = builder.apply(new ArgumentBuilder()).getArguments();
        SubCommand<T> subCommand = new SubCommand<T>(bot, role, allowBots) {

            @Override
            public void onExecute(User sender, CommandContainer container, String[] args) {
                executor.execute(sender, args, null);
            }
        };
        subCommand.setArguments(arguments);
        this.subCommands.add(subCommand);
        return this;
    }

    public SubChain<T> newSub(SimpleBot bot, UnaryOperator<ArgumentBuilder> builder, Executor executor) {
        return this.newSub(bot, null, true, builder, executor);
    }

    public SubChain<T> newSub(SimpleBot bot, Role role, UnaryOperator<ArgumentBuilder> builder, Executor executor) {
        return this.newSub(bot, role, true, builder, executor);
    }

    public SubChain<T> newSub(SimpleBot bot, boolean isConsole, UnaryOperator<ArgumentBuilder> builder, Executor executor) {
        return this.newSub(bot, null, isConsole, builder, executor);
    }
}
