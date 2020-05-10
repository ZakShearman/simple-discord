package pink.zak.simplediscord.command.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.command.CommandContainer;

public abstract class Command {
    protected final SimpleBot bot;
    private final Role role;
    private final boolean allowsBots;

    public Command(SimpleBot bot, Role role, boolean allowBots) {
        this.bot = bot;
        this.role = role;
        this.allowsBots = allowBots;
    }

    public abstract void onExecute(Member sender, CommandContainer container, String[] args);

    public void middleMan(Member sender, CommandContainer container, String[] args) {
        this.onExecute(sender, container, args);
    }

    public Role getRole() {
        return this.role;
    }

    public boolean allowsBots() {
        return this.allowsBots;
    }
}