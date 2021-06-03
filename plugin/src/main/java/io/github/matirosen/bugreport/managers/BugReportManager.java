package io.github.matirosen.bugreport.managers;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.storage.Callback;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.util.List;

public class BugReportManager {

    @Inject
    private ObjectRepository<BugReport, Integer> bugReportRepository;
    @Inject
    private ReportPlugin plugin;

    public void saveReport(BugReport bugReport){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> bugReportRepository.save(bugReport));
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

