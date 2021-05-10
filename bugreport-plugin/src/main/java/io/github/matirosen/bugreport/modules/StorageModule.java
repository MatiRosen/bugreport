package io.github.matirosen.bugreport.modules;

import io.github.matirosen.bugreport.ReportPlugin;
import me.yushust.inject.Binder;
import me.yushust.inject.Module;

public class StorageModule implements Module {

    private final ReportPlugin reportPlugin;

    public StorageModule(ReportPlugin reportPlugin){
        this.reportPlugin = reportPlugin;
    }

    @Override
    public void configure(Binder binder){
        String storageType = reportPlugin.getConfig().getString("storage.type");
        storageType = storageType == null ? "flatFile" : storageType;


        if (storageType.equalsIgnoreCase("mongoDB")){
            binder.install(new MongoDbDataModule());
        } else if (storageType.equalsIgnoreCase("MySQL")){
            binder.install(new MySQLDataModule());
        } else if (storageType.equalsIgnoreCase("h2")){
            binder.install(new H2DataModule());
        } else binder.install(new YamlDataModule());
    }
}