package io.github.matirosen;

import io.github.matirosen.commands.MainCommand;
import io.github.matirosen.listeners.InventoryListener;
import io.github.matirosen.managers.BugReportManager;
import io.github.matirosen.managers.FileManager;
import io.github.matirosen.modules.CoreModule;
import io.github.matirosen.utils.ConfigHandler;
import io.github.matirosen.utils.MessageHandler;
import me.yushust.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class ReportPlugin extends JavaPlugin {

    @Inject
    private FileManager fileManager;
    @Inject
    private BugReportManager bugReportManager;

    @Inject
    private InventoryListener inventoryListener;

    @Inject
    private MainCommand mainCommand;

    private static MessageHandler messageHandler;
    private static ConfigHandler configHandler;

    @Override
    public void onEnable() {
        Injector injector = Injector.create(new CoreModule(this));
        injector.injectMembers(this);

        fileManager.loadAllFileConfigurations();

        configHandler = new ConfigHandler(fileManager);
        configHandler.setConfigValues();
        messageHandler = new MessageHandler(fileManager);

        bugReportManager.start();

    }

    public static MessageHandler getMessageHandler(){
        return messageHandler;
    }

    public static ConfigHandler getConfigHandler(){
        return configHandler;
    }
}