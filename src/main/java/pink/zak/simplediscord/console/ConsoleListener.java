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
        System.out.println("1");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            System.out.println("INPUT: ".concat(input));
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
