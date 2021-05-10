package io.github.matirosen.bugreport;

import io.github.matirosen.bugreport.listeners.InventoryListener;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.managers.FileManager;
import io.github.matirosen.bugreport.modules.CoreModule;
import io.github.matirosen.bugreport.storage.DataConnection;
import io.github.matirosen.bugreport.storage.repositories.BugReportSQLRepository;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import io.github.matirosen.bugreport.utils.ConfigHandler;
import io.github.matirosen.bugreport.utils.MessageHandler;
import io.github.matirosen.bugreport.commands.MainCommand;
import me.yushust.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.sql.Connection;

public class ReportPlugin extends JavaPlugin {


    @Inject
    private DataConnection<Connection> connection;
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

    public void onEnable() {
        try {
            Injector injector = Injector.create(new CoreModule(this));
            injector.injectMembers(this);
        } catch (Exception e){
            e.printStackTrace();
        }

        connection.connect();

        fileManager.loadAllFileConfigurations();

        configHandler = new ConfigHandler(fileManager);
        configHandler.setConfigValues();
        messageHandler = new MessageHandler(fileManager);

        bugReportManager.start();
    }

    public void onDisable(){
        connection.disconnect();
    }


    public static MessageHandler getMessageHandler(){
        return messageHandler;
    }

    public static ConfigHandler getConfigHandler(){
        return configHandler;
    }
}