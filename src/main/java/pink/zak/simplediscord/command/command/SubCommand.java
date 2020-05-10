package pink.zak.simplediscord.command.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.command.argument.Argument;
import pink.zak.simplediscord.command.argument.ArgumentHandler;

import java.util.List;
import java.util.Set;

public abstract class SubCommand extends Command {
    private final boolean endless;
    private List<Argument<?>> arguments = Lists.newArrayList();
    private boolean inheritRoleRequirements;

    public SubCommand(SimpleBot bot, Role role, boolean allowBots) {
        this(bot, role, allowBots, false);
    }

    public SubCommand(SimpleBot bot, Role role, boolean allowBots, boolean endless) {
        super(bot, role, allowBots);
        this.endless = endless;
    }

    public SubCommand(SimpleBot bot) {
        this(bot, null, true);
    }

    public SubCommand(SimpleBot bot, Role role) {
        this(bot, role, true);
    }

    public SubCommand(SimpleBot bot, boolean allowBots) {
        this(bot, null, allowBots);
    }

    protected void inheritPermission() {
        this.inheritRoleRequirements = true;
    }

    public boolean doesInheritPermission() {
        return this.inheritRoleRequirements;
    }

    public boolean isEndless() {
        return this.endless;
    }

    public void setArguments(List<Argument<?>> arguments) {
        this.arguments = arguments;
    }

    public void addFlat(String flat) {
        this.arguments.add(new Argument<>(null, flat));
    }

    public void addFlatWithAliases(String flat, String... aliases) {
        this.arguments.add(new Argument<>(null, flat, aliases));
    }

    public void addFlats(String... flat) {
        for (String flatArgument : flat) {
            this.addFlat(flatArgument);
        }
    }

    protected <S> void addArgument(Class<S> clazz, String argument) {
        this.arguments.add(new Argument<S>(ArgumentHandler.getArgumentType(clazz), argument));
    }

    public int getArgumentsSize() {
        return this.arguments.size();
    }

    @SuppressWarnings("unchecked")
    public <U> U parseArgument(String[] args, Guild guild, int index) {
        return ((Argument<U>) this.arguments.get(index)).getType().parse(args[index], guild);
    }

    public boolean isMatch(String[] arguments) {
        for (int i = 0; i < arguments.length; i++) {
            if (!this.isArgumentValid(arguments, i)) {
                return false;
            }
        }
        return true;
    }

    public String[] getEnd(String[] arguments) {
        Set<String> newSet = Sets.newLinkedHashSet();
        for (int i = 0; i < arguments.length; i++) {
            if (i < this.arguments.size() - 1) {
                continue;
            }
            newSet.add(arguments[i]);
        }
        return newSet.toArray(new String[]{});
    }

    private boolean isArgumentValid(String[] arguments, int index) {
        if (this.arguments.size() < index && this.endless) {
            return true;
        }
        Argument<?> argument = this.arguments.get(index);
        if (argument.getType() == null) {
            String matchTo = arguments[index];
            for (String alias : argument.getAliases()) {
                if (matchTo.equalsIgnoreCase(alias)) {
                    return true;
                }
            }
            return arguments[index].equalsIgnoreCase(argument.getArgument());
        }
        return true;
    }
}