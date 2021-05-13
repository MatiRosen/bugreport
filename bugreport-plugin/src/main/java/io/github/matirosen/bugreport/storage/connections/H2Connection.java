package io.github.matirosen.bugreport.storage.connections;

import io.github.matirosen.bugreport.storage.DataConnection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

public class H2Connection implements DataConnection<Connection> {

    @Inject
    private JavaPlugin plugin;

    private JdbcConnectionPool connectionPool;

    private final String REPORT_TABLE = "CREATE TABLE IF NOT EXISTS `report_table` (`id` INTEGER NOT NULL AUTO_INCREMENT, " +
            "`player_name` VARCHAR (16) NOT NULL, " +
            "`report_message` VARCHAR (1250) NOT NULL, " +
            "`time` BIGINT NOT NULL," +
            "`priority` INT NOT NULL DEFAULT 0, " +
            "`solved` SMALLINT NOT NULL DEFAULT 0," +
            "PRIMARY KEY (`id`));";

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
        ConfigurationSection config = plugin.getConfig();
        String user = config.getString("storage.username");
        String password =  config.getString("storage.password");
        String database = config.getString("storage.database");

        connectionPool = JdbcConnectionPool.create("jdbc:h2:~/" + database, user, password);

        try {
            Connection connection = connectionPool.getConnection();
            connection.createStatement().executeUpdate(REPORT_TABLE);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void disconnect(){

    }
}
