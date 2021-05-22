package io.github.matirosen.bugreport.modules;

import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.storage.DataConnection;
import io.github.matirosen.bugreport.storage.connections.H2Connection;
import io.github.matirosen.bugreport.storage.repositories.BugReportSQLRepository;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import me.yushust.inject.Binder;
import me.yushust.inject.Module;
import me.yushust.inject.key.TypeReference;

import java.sql.Connection;

public class H2DataModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeReference<DataConnection<Connection>>(){}).to(H2Connection.class).singleton();
        binder.bind(new TypeReference<ObjectRepository<BugReport, Integer>>(){})
                .to(BugReportSQLRepository.class).singleton();
    }
}
