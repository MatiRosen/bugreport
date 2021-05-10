package io.github.matirosen.bugreport.storage.repositories;

import io.github.matirosen.bugreport.managers.FileManager;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.storage.Callback;
import io.github.matirosen.bugreport.utils.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BugReportYamlRepository implements ObjectRepository<BugReport, Integer>{

    @Inject
    private FileManager fileManager;
    @Inject
    private JavaPlugin plugin;

    @Override
    public void start(){

    }

    @Override
    public BugReport load(Integer id) {
        String fileName = "report-" + id + ".yml";
        File file = new File(fileManager.getReportsFolder(), fileName);
        if (!file.exists()) return null;

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        String playerName = configuration.getString("playerName");
        String reportMessage = configuration.getString("reportMessage");
        int priority = configuration.getInt("priority");
        long currentTimeMillis = configuration.getLong("time");
        boolean solved = configuration.getBoolean("solved");

        BugReport bugReport = new BugReport(playerName, reportMessage, currentTimeMillis, false);
        bugReport.setId(id);
        bugReport.setPriority(priority);
        bugReport.setSolved(solved);

        return bugReport;
    }

    @Override
    public List<BugReport> loadAll() {
        List<BugReport> bugReportList = new ArrayList<>();
        int counter = ConfigHandler.totalReports;
        int goal = ConfigHandler.totalReports - 500;

        while (counter > goal){
            if (counter == 0) break;
            BugReport bugReport = load(counter);
            counter--;

            if (bugReport == null) {
                goal--;
                continue;
            }
            bugReportList.add(bugReport);
        }

        return bugReportList;
    }

    @Override
    public void save(BugReport bugReport) {
        String fileName = "report-" + bugReport.getId() + ".yml";
        File reportFile = new File(fileManager.getReportsFolder(), fileName);

        FileConfiguration config = new YamlConfiguration();
        config.set("id", bugReport.getId());
        config.set("playerName", bugReport.getPlayerName());
        config.set("reportMessage", bugReport.getReportMessage());
        config.set("priority", bugReport.getPriority());
        config.set("time", bugReport.getCurrentTimeMillis());
        config.set("solved", bugReport.isSolved());

        try {
            config.save(reportFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String fileName = "report-" + id + ".yml";
        File file = new File(fileManager.getReportsFolder(), fileName);
        file.delete();
    }

    @Override
    public void saveAsync(BugReport bugReport, Callback<BugReport> callback){

    }

    @Override
    public void loadAsync(Integer id, Callback<BugReport> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            BugReport bugReport = load(id);
            callback.call(bugReport);
        });
    }
}