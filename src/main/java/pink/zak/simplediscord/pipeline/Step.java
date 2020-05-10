package pink.zak.simplediscord.pipeline;

public interface Step<T, R> {

    R process(T type);
}
