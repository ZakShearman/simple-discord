package pink.zak.simplediscord.save;

import java.util.Timer;
import java.util.TimerTask;

public class SaveTask extends TimerTask {
    private final Timer timer;
    private final Savable savable;

    public SaveTask(Savable savable, int interval) {
        this.savable = savable;

        this.timer = new Timer(savable.getClass().toString());
        this.timer.scheduleAtFixedRate(this, interval, interval);
    }

    public void stop() {
        this.timer.cancel();
    }

    @Override
    public void run() {
        this.savable.save();
    }
}
