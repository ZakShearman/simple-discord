package pink.zak.simplediscord.json;

import java.lang.reflect.Type;

public class TypeTokens {

    public static <T> Type findType() {
        return new Token<T>().type();
    }
}
