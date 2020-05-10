package pink.zak.simplediscord.command;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandContainer {
    private final Guild guild;
    private final User sender;
    private final MessageChannel channel;
    private final Message message;
    private final Member member;
    private final String messageId;

    public CommandContainer(MessageReceivedEvent event) {
        this.guild = event.getGuild();
        this.sender = event.getAuthor();
        this.channel = event.getChannel();
        this.message = event.getMessage();
        this.member = event.getMember();
        this.messageId = event.getMessageId();
    }

    public Guild getGuild() {
        return this.guild;
    }

    public User getSender() {
        return this.sender;
    }

    public MessageChannel getChannel() {
        return this.channel;
    }

    public Message getMessage() {
        return this.message;
    }

    public Member getMember() {
        return this.member;
    }

    public String getMessageId() {
        return this.messageId;
    }
}
