package pink.zak.simplediscord.command;

import com.google.common.collect.Sets;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.command.argument.ArgumentHandler;
import pink.zak.simplediscord.command.argument.ArgumentType;
import pink.zak.simplediscord.command.command.SimpleCommand;
import pink.zak.simplediscord.command.command.SubCommand;

import java.util.Set;

public class CommandBase extends ListenerAdapter {
    private final SimpleBot bot;
    private Set<SimpleCommand<? extends User>> commands = Sets.newHashSet();

    public CommandBase(SimpleBot bot) {
        this.bot = bot;
        this.registerArgumentTypes();
    }

    public void registerCommand(SimpleCommand<? super User> command) {
        this.commands.add(command);
    }

    public CommandBase registerArgumentType(Class<?> clazz, ArgumentType<?> argumentType) {
        ArgumentHandler.register(clazz, argumentType);
        return this;
    }

    @Override
    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        String rawMessage = event.getMessage().getContentRaw();
        String prefix = this.bot.getPrefix();
        if (!rawMessage.startsWith(prefix)) {
            return;
        }
        String commandName = rawMessage.substring(1).split(" ")[0];
        User sender = event.getAuthor();
        System.out.println("3");
        for (SimpleCommand<? extends User> simpleCommand : this.commands) {
            if (!simpleCommand.getCommand().equalsIgnoreCase(commandName)) {
                continue;
            }
            if (sender.isBot() && !simpleCommand.allowsBots()) {
                return;
            }
            Member member = event.getMember();
            System.out.println(member);
            if (member == null || simpleCommand.getRole() != null && !member.getRoles().contains(simpleCommand.getRole())) {
                return;
            }
            if (!rawMessage.contains(" ")) {
                simpleCommand.middleMan(sender, new CommandContainer(event), new String[]{});
                return;
            }
            String message = rawMessage.split(prefix.concat(commandName).concat(" "))[1];
            String[] args = message.split(" ");

            SubCommand<? extends User> subResult = null;
            for (SubCommand<? extends User> subCommand : simpleCommand.getSubCommands()) {
                if ((args.length > subCommand.getArgumentsSize() && subCommand.isEndless()) || (subCommand.getArgumentsSize() == args.length && subCommand.isMatch(args))) {
                    subResult = subCommand;
                    break;
                }
            }
            if (subResult == null) {
                simpleCommand.middleMan(sender, new CommandContainer(event), args);
                return;
            }
            if (subResult.getRole() != null && !member.getRoles().contains(subResult.getRole())) {
                return;
            }
            if (!subResult.allowsBots() && sender.isBot()) {
                return;
            }
            subResult.middleMan(sender, new CommandContainer(event), args);
        }
    }

    public Set<SimpleCommand<? extends User>> getCommands() {
        return this.commands;
    }

    private void registerArgumentTypes() {
        this.registerArgumentType(String.class, (string, guild) -> string)
                .registerArgumentType(Member.class, (string, guild) -> guild.getMember(this.bot.getJda().getUserById(string.contains("!") ? string.substring(4, 22) : string.substring(3, 21))))
                .registerArgumentType(User.class, (string, guild) -> this.bot.getJda().getUserById(string.contains("!") ? string.substring(4, 22) : string.substring(3, 21)))
                .registerArgumentType(Integer.class, (string, guild) -> StringUtils.isNumeric(string) ? Integer.parseInt(string) : -1)
                .registerArgumentType(Boolean.class, (string, guild) -> string.equalsIgnoreCase("true") ? true : string.equalsIgnoreCase("false") ? false : null);
    }
}