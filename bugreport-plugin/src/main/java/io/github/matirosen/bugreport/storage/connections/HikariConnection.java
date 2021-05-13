package io.github.matirosen.bugreport.storage.connections;

import com.zaxxer.hikari.HikariDataSource;
import io.github.matirosen.bugreport.storage.DataConnection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnection implements DataConnection<Connection> {

    private HikariDataSource dataSource;

    @Inject
    private JavaPlugin plugin;


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
        try{
            return dataSource.getConnection();
        } catch (SQLException exception){
            exception.printStackTrace();
            connect();
            return null;
        }
    }

    @Override
    public void connect(){
        ConfigurationSection config = plugin.getConfig();
        String ip = config.getString("storage.ip") + ":" + config.getInt("storage.port");
        String user = config.getString("storage.username");
        String password =  config.getString("storage.password");
        String database = config.getString("storage.database");

        dataSource = new HikariDataSource();

        dataSource.setPoolName("BugReport - Connection pool");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://" + ip + "/" + database + "?characterEncoding=utf8");
        dataSource.addDataSourceProperty("cachePrepStmts", "true");
        dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource.addDataSourceProperty("characterEncoding", "utf8");
        dataSource.addDataSourceProperty("encoding", "UTF-8");
        dataSource.addDataSourceProperty("useUnicode", "true");
        dataSource.addDataSourceProperty("useSSL", "false");
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setMinimumIdle(10);
        dataSource.setMaximumPoolSize(10);

        try {
            Connection connection = dataSource.getConnection();
            connection.createStatement().executeUpdate(REPORT_TABLE);
            //connection.createStatement().executeUpdate(LABEL_TABLE);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void disconnect(){
        dataSource.close();
    }
}
