package io.github.matirosen.bugreport.managers;

import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.Utils;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BugReportManager {

    @Inject
    private FileManager fileManager;
    @Inject
    private ObjectRepository<BugReport, Integer> bugReportRepository;

    private final List<BugReport> bugReportList = new ArrayList<>();

    public void addReport(BugReport bugReport){
        bugReportList.add(0, bugReport);
        if (bugReportList.size() >= 500){
            bugReportList.remove(499);
        }
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

    public List<BugReport> getBugReportList(){
        return bugReportList;
    }

    public BugReport getBugReportById(int id){
        BugReport bugreport = bugReportList.stream().filter(bugReport -> bugReport.getId() == id).findFirst().orElse(null);

        if (bugreport == null) bugreport = bugReportRepository.load(id);


        return bugreport;
    }

    public void start(){
        bugReportRepository.start();
        bugReportList.addAll(bugReportRepository.loadAll());
    }

    public void stop(){
    }
}

