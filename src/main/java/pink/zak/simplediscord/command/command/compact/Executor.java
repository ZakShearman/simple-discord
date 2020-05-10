package pink.zak.simplediscord.command.command.compact;

import net.dv8tion.jda.api.entities.User;
import pink.zak.simplediscord.command.command.SubCommand;

public interface Executor {

    void execute(User sender, String[] args, SubCommand<User> sub);
}
