package pink.zak.simplediscord.console;

import pink.zak.simplediscord.bot.SimpleBot;

import java.util.Scanner;

public class ConsoleListener implements Runnable {
    private final SimpleBot bot;

    public ConsoleListener(SimpleBot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "stop":
                    if (this.bot.isInitialized()) {
                        this.bot.unload();
                    }
                    break;
            }
        }
    }
}
