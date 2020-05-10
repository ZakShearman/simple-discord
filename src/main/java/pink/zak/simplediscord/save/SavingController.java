package pink.zak.simplediscord.save;

import com.google.common.collect.Sets;

import java.util.Set;

public class SavingController {
    private Set<SaveTask> saveTasks = Sets.newHashSet();

    public void addSavable(Savable savable, int interval) {
        this.saveTasks.add(new SaveTask(savable, interval));
    }

    public void clearController() {
        for (SaveTask saveTask : this.saveTasks) {
            saveTask.stop();
        }
        this.saveTasks.clear();
    }
}
