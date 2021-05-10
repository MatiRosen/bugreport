package io.github.matirosen.bugreport.modules;

import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.storage.repositories.BugReportYamlRepository;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import me.yushust.inject.Binder;
import me.yushust.inject.Module;
import me.yushust.inject.key.TypeReference;

public class YamlDataModule implements Module {


    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeReference<ObjectRepository<BugReport, Integer>>(){})
                .to(BugReportYamlRepository.class).singleton();
    }
}