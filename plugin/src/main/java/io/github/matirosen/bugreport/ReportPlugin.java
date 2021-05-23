package io.github.matirosen.bugreport;

import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.managers.FileManager;
import io.github.matirosen.bugreport.modules.CoreModule;
import io.github.matirosen.bugreport.storage.DataConnection;
import io.github.matirosen.bugreport.utils.MessageHandler;
import io.github.matirosen.bugreport.commands.MainCommand;
import io.github.matirosen.bugreport.utils.Utils;
import me.yushust.inject.Injector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.gui.core.GUIListeners;

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
    private MainCommand mainCommand;

    private static MessageHandler messageHandler;

    public void onEnable() {
        try {
            Injector injector = Injector.create(new CoreModule(this));
            injector.injectMembers(this);
        } catch (Exception e){
            e.printStackTrace();
        }

        fileManager.loadAllFileConfigurations();
        connection.connect();

        messageHandler = new MessageHandler(fileManager);
        Utils.totalReports = fileManager.get("info").getInt("report-number");
        if (Utils.totalReports == 0) Utils.totalReports++;
        bugReportManager.start();

        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);
    }

    public void onDisable(){
        bugReportManager.stop();
        connection.disconnect();
    }

    public static MessageHandler getMessageHandler(){
        return messageHandler;
    }
}