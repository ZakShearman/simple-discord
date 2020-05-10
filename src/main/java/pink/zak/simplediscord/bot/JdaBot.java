package pink.zak.simplediscord.bot;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import pink.zak.simplediscord.command.CommandBase;
import pink.zak.simplediscord.command.command.SimpleCommand;
import pink.zak.simplediscord.config.ConfigStore;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

public class JdaBot implements SimpleBot {
    private CommandBase commandBase;
    private ConfigStore configStore;
    private Path basePath;
    private String prefix;
    private JDA jda;

    @SneakyThrows
    public JdaBot(UnaryOperator<Path> subBasePath) {
        this.commandBase = new CommandBase(this);
        this.configStore = new ConfigStore(this);
        this.basePath = subBasePath.apply(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toPath());
    }

    @Override
    public void initialize(String token, String prefix) {
        this.prefix = prefix;
        try {
            this.jda = JDABuilder.createDefault(token).build();
            this.registerListeners(this.commandBase);
        } catch (LoginException e) {
            System.out.println("Unable to log into Discord, the following error occurred:");
            e.printStackTrace();
        }
    }

    @Override
    public void registerCommands(SimpleCommand... commands) {
        for (SimpleCommand command : commands) {
            this.commandBase.registerCommand(command);
        }
    }

    @Override
    public void registerListeners(Object... listeners) {
        if (this.isInitialized()) {
            for (Object listener : listeners) {
                this.jda.addEventListener(listener);
            }
        }
    }

    @SneakyThrows
    @Override
    public void saveResource(String location, boolean override) {
        location = location.replace("\\", "/");
        InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(location);
        if (inStream == null) {
            System.out.println("Failed to find ".concat(location).concat(" in the resources"));
            return;
        }

        File targetFile = new File(this.basePath.toFile(), location);
        if (targetFile.exists() && !override) {
            System.out.println("File already exists ".concat(location).concat(" and override is not set to true."));
            return;
        }
        File targetFolder = new File(this.basePath.toFile(), location.substring(Math.max(location.lastIndexOf("/"), 0)));
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }
        OutputStream outStream = new FileOutputStream(targetFile);
        byte[] buf = new byte[1024];
        int len;
        while ((len = inStream.read(buf)) > 0) {
            outStream.write(buf, 0, len);
        }
        outStream.close();
        inStream.close();
    }

    @Override
    public boolean isInitialized() {
        return this.jda == null;
    }

    @Override
    public Path getBasePath() {
        return this.basePath;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public CommandBase getCommandBase() {
        return this.commandBase;
    }

    @Override
    public ConfigStore getConfigStore() {
        return this.configStore;
    }

    @Override
    public JDA getJda() {
        return this.jda;
    }
}
