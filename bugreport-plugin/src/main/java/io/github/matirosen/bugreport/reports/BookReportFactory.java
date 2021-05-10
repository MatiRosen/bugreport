package io.github.matirosen.bugreport.reports;

import me.yushust.inject.assisted.ValueFactory;

public interface BookReportFactory extends ValueFactory {
    BookReport create(BugReport bugReport);
}
