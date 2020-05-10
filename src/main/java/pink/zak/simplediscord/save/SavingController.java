package pink.zak.simplediscord.save;

import com.google.common.collect.Sets;
import pink.zak.simplediscord.bot.SimpleBot;

import java.util.Set;

public class SavingController {
    private final SimpleBot bot;
    private Set<SaveTask> saveTasks = Sets.newHashSet();

    public SavingController(SimpleBot bot) {
        this.bot = bot;
    }

    public void addSavable(Savable savable, int interval) {
        this.saveTasks.add(new SaveTask(this.bot, savable, interval));
    }

    public void clearController() {
        for (SaveTask saveTask : this.saveTasks) {
            saveTask.stop();
        }
        this.saveTasks.clear();
    }
}
