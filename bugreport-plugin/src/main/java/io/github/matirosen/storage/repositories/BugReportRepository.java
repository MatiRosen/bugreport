package io.github.matirosen.storage.repositories;

import io.github.matirosen.reports.BugReport;
import io.github.matirosen.storage.Callback;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.List;

public class BugReportRepository implements ObjectRepository<BugReport, Integer> {


    @Inject
    private JavaPlugin plugin;



    @Override
    public BugReport load(Integer id) {
        return null;
    }

    @Override
    public List<BugReport> loadAll() {
        return null;
    }

    @Override
    public void save(BugReport bugReport) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void loadAsync(Integer id, Callback<BugReport> callback) {

    }

}
