package io.github.matirosen.managers;

import io.github.matirosen.reports.BugReport;
import io.github.matirosen.storage.repositories.ObjectRepository;
import io.github.matirosen.utils.ConfigHandler;
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
        bugReportRepository.save(bugReport);
        ConfigHandler.totalReports++;

        File file = new File(fileManager.getReportsFolder(), "info.yml");

        FileConfiguration config = fileManager.get("info");

        FileConfigurationOptions fileConfigurationOptions = config.options();
        fileConfigurationOptions.header("Do not change this number. It's the total report counter.");

        config.set("report-number", ConfigHandler.totalReports);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        bugReportList.addAll(bugReportRepository.loadAll());
    }
}

