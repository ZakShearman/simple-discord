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

import java.util.Optional;
import java.util.Set;

public class CommandBase extends ListenerAdapter {
    private final SimpleBot bot;
    private Set<SimpleCommand> commands = Sets.newHashSet();

    public CommandBase(SimpleBot bot) {
        this.bot = bot;
        this.registerArgumentTypes();
    }

    public void registerCommand(SimpleCommand command) {
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
        Member sender = event.getMember();
        for (SimpleCommand simpleCommand : this.commands) {
            if (!simpleCommand.getCommand().equalsIgnoreCase(commandName) && !simpleCommand.getAliases().contains(commandName)) {
                continue;
            }
            if (sender.getUser().isBot() && !simpleCommand.allowsBots()) {
                return;
            }
            Member member = event.getMember();
            if (member == null || simpleCommand.getRole() != null && !member.getRoles().contains(simpleCommand.getRole())) {
                return;
            }
            if (!rawMessage.contains(" ")) {
                simpleCommand.middleMan(sender, new CommandContainer(event), new String[]{});
                return;
            }
            String message = rawMessage.split(prefix.concat(commandName).concat(" "))[1];
            String[] args = message.split(" ");

            SubCommand subResult = null;
            for (SubCommand subCommand : simpleCommand.getSubCommands()) {
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
            if (!subResult.allowsBots() && sender.getUser().isBot()) {
                return;
            }
            subResult.middleMan(sender, new CommandContainer(event), args);
        }
    }

    public Set<SimpleCommand> getCommands() {
        return this.commands;
    }

    private void registerArgumentTypes() {
        this.registerArgumentType(String.class, (string, guild) -> string)
                .registerArgumentType(Member.class, (string, guild) -> {
                    String id = string.length() == 21 ? string.substring(2, 20) : string.length() == 22 ? string.substring(3, 21) : null;
                    if (id == null) {
                        return Optional.empty();
                    }
                    return Optional.of(guild.retrieveMemberById(id).complete());
                })
                .registerArgumentType(User.class, (string, guild) -> {
                    String id = string.length() == 21 ? string.substring(2, 20) : string.length() == 22 ? string.substring(3, 21) : null;
                    if (id == null) {
                        return Optional.empty();
                    }
                    return Optional.of(this.bot.getJda().retrieveUserById(id).complete());
                })
                .registerArgumentType(Integer.class, (string, guild) -> StringUtils.isNumeric(string) ? Integer.parseInt(string) : -1)
                .registerArgumentType(Boolean.class, (string, guild) -> string.equalsIgnoreCase("true") ? true : string.equalsIgnoreCase("false") ? false : null);
    }
}