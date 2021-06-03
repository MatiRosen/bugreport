package io.github.matirosen.bugreport.managers;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.storage.Callback;
import io.github.matirosen.bugreport.utils.Utils;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class BugReportManager {

    @Inject
    private FileManager fileManager;
    @Inject
    private ObjectRepository<BugReport, Integer> bugReportRepository;
    @Inject
    private ReportPlugin plugin;

    public void addReport(BugReport bugReport){
        saveReport(bugReport);
        Utils.totalReports++;

        File file = new File(fileManager.getReportsFolder(), "info.yml");
        FileConfiguration info = fileManager.get("info");

        FileConfigurationOptions fileConfigurationOptions = info.options();
        fileConfigurationOptions.header("Do not change this number. It's the total report counter.");

        info.set("report-number", Utils.totalReports);
        try {
            info.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveReport(BugReport bugReport){
        bugReportRepository.saveAsync(bugReport, report -> {});
    }

    public void getBugReportList(Callback<List<BugReport>> callback){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<BugReport> bugReportList = bugReportRepository.loadAll();
            callback.call(bugReportList);
        });
    }

    public void getBugReportById(int id, Callback<BugReport> callback){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            BugReport bugReport = bugReportRepository.load(id);
            callback.call(bugReport);
        });
    }

    public void start(){
        bugReportRepository.start();
    }

    public void stop(){
    }
}

