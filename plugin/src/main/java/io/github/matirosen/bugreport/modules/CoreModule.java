package io.github.matirosen.bugreport.modules;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.guis.*;
import io.github.matirosen.bugreport.reports.BookReport;
import io.github.matirosen.bugreport.reports.BookReportFactory;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.managers.FileManager;
import me.yushust.inject.Binder;
import me.yushust.inject.Module;
import org.bukkit.plugin.java.JavaPlugin;



public class CoreModule implements Module {

    private final ReportPlugin reportPlugin;

    public CoreModule(ReportPlugin reportPlugin){
        this.reportPlugin = reportPlugin;
    }

    @Override
    public void configure(Binder binder){
        binder.bind(JavaPlugin.class).toInstance(reportPlugin);
        binder.bind(ReportPlugin.class).toInstance(reportPlugin);

        binder.bind(FileManager.class).singleton();
        binder.bind(BugReportManager.class).singleton();


        binder.bind(BugReportMainMenu.class).singleton();
        binder.bind(BugReportSecondMenu.class).singleton();
        binder.bind(PriorityMenu.class).singleton();
        binder.bind(LabelsMenu.class).singleton();

        binder.bind(BookReport.class).toFactory(BookReportFactory.class);


        binder.install(new StorageModule(reportPlugin));
        binder.install(new PacketsModule());

    }
}

