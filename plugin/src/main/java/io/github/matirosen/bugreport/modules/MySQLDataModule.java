package io.github.matirosen.bugreport.modules;

import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.storage.DataConnection;
import io.github.matirosen.bugreport.storage.connections.HikariConnection;
import io.github.matirosen.bugreport.storage.repositories.BugReportSQLRepository;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import me.yushust.inject.Binder;
import me.yushust.inject.Module;
import me.yushust.inject.key.TypeReference;

import java.sql.Connection;

public class MySQLDataModule implements Module {


    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeReference<DataConnection<Connection>>(){}).to(HikariConnection.class).singleton();
        binder.bind(new TypeReference<ObjectRepository<BugReport, Integer>>(){})
                .to(BugReportSQLRepository.class).singleton();
    }
}

