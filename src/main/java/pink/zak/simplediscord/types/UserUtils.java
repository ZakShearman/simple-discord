package pink.zak.simplediscord.types;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.Member;

@UtilityClass
public class UserUtils {

    public static String getNameDiscrim(Member member) {
        return member.getEffectiveName() + "#" + member.getUser().getDiscriminator();
    }

    public long parseIdInput(String input) {
        if (input.contains(" ")) {
            return -1;
        }
        try {
            return Long.parseLong(input.length() == 18 ? input : input.length() == 21 ? input.substring(2, 20) : input.length() == 22 ? input.substring(3, 21) : "-1");
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}
