package pink.zak.simplediscord.command.command;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.command.CommandContainer;

import java.util.function.Function;

public abstract class Command<T extends User> {
    protected final SimpleBot bot;
    private final Role role;
    private final boolean allowsBots;
    private Function<T, String> noPermission;

    public Command(SimpleBot bot, Role role, boolean allowBots) {
        this.bot = bot;
        this.role = role;
        this.allowsBots = allowBots;
        this.noPermission = player -> "&cYou do not have permission to do this";
    }

    public abstract void onExecute(T sender, CommandContainer container, String[] args);

    @SuppressWarnings("unchecked")
    public void middleMan(User sender, CommandContainer container, String[] args) {
        this.onExecute((T) sender, container, args);
    }

    public Role getRole() {
        return this.role;
    }

    public boolean allowsBots() {
        return this.allowsBots;
    }

    public void noPermissionLang(Function<T, String> noPermission) {
        this.noPermission = noPermission;
    }

    @SuppressWarnings("unchecked")
    public String getNoPermissionLang(User sender) {
        return this.noPermission.apply((T) sender);
    }
}