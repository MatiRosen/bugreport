package io.github.matirosen.bugreport.storage.connections;

import io.github.matirosen.bugreport.managers.FileManager;
import io.github.matirosen.bugreport.storage.DataConnection;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.inject.Inject;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class H2Connection implements DataConnection<Connection> {

    @Inject
    private FileManager fileManager;

    private JdbcConnectionPool connectionPool;

    private final String REPORT_TABLE = "CREATE TABLE IF NOT EXISTS `report_table` (`id` INTEGER NOT NULL AUTO_INCREMENT, " +
            "`player_name` VARCHAR (16) NOT NULL, " +
            "`report_message` VARCHAR (1250) NOT NULL, " +
            "`time` BIGINT NOT NULL," +
            "`priority` INT NOT NULL DEFAULT 0, " +
            "`solved` SMALLINT NOT NULL DEFAULT 0," +
            "PRIMARY KEY (`id`));";
    private final String LABEL_TABLE = "CREATE TABLE IF NOT EXISTS `label_table` (`report_id` INTEGER NOT NULL, " +
            "`label` VARCHAR(30) NOT NULL, " +
            "PRIMARY KEY (`report_id`, `label`), " +
            "FOREIGN KEY (`report_id`) REFERENCES report_table(id));";

    @Override
    public Connection getConnection(){
        try {
            return connectionPool.getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void connect(){
        File file = new File(fileManager.getReportsFolder(), "h2-file.db");
        connectionPool = JdbcConnectionPool.create("jdbc:h2:file:" + file.getAbsolutePath(), "", "");

        try {
            Connection connection = connectionPool.getConnection();
            connection.createStatement().executeUpdate(REPORT_TABLE);
            //TODO label table
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void disconnect(){
    }
}
