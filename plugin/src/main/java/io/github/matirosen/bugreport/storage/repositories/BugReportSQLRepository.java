package io.github.matirosen.bugreport.storage.repositories;

import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.storage.Callback;
import io.github.matirosen.bugreport.storage.DataConnection;
import io.github.matirosen.bugreport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BugReportSQLRepository implements ObjectRepository<BugReport, Integer> {


    @Inject
    private JavaPlugin plugin;
    @Inject
    private DataConnection<Connection> dataConnection;

    private Connection connection;

    private final String SELECT_REPORT = "SELECT * FROM `report_table` WHERE id=?;";
    private final String INSERT_REPORT = "INSERT INTO `report_table` " +
            "(id, player_name, report_message, time, priority, labels, solved) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private final String UPDATE_REPORT = "UPDATE `report_table` SET player_name=?, report_message=?, time=?, priority=?, " +
            "labels=?, solved=? WHERE id=?;";

    @Override
    public void start(){
        connection = dataConnection.getConnection();
    }

    @Override
    public BugReport load(Integer id) {
        try(PreparedStatement statement = connection.prepareStatement(SELECT_REPORT)){
            statement.setString(1, String.valueOf(id));

            ResultSet resultSet = statement.executeQuery();
            BugReport bugReport;
            if (resultSet.next()){
                bugReport = new BugReport(id, resultSet.getString("player_name"),
                        resultSet.getString("report_message"), resultSet.getLong("time"), true);
                bugReport.setPriority(resultSet.getInt("priority"));
                bugReport.setSolved(resultSet.getBoolean("solved"));
                for (String label : resultSet.getString("labels").split(",")){
                    bugReport.addLabel(label);
                }

            } else{
                bugReport = null;
            }
            resultSet.close();
            return bugReport;
        } catch (SQLException exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BugReport> loadAll() {
        List<BugReport> bugReportList = new ArrayList<>();
        int counter = Utils.totalReports;
        int goal = Utils.totalReports - 500;

        while (counter > goal){
            if (counter == 0) break;
            BugReport bugReport = load(counter);
            counter--;

            if (bugReport == null) {
                goal--;
                continue;
            }
            bugReportList.add(bugReport);
        }

        return bugReportList;
    }

    @Override
    public void save(BugReport bugReport) {
        if (bugReport.exists()){
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_REPORT)){
                statement.setString(1, bugReport.getPlayerName());
                statement.setString(2, bugReport.getReportMessage());
                statement.setLong(3, bugReport.getCurrentTimeMillis());
                statement.setInt(4, bugReport.getPriority());
                statement.setString(5, String.join(",", bugReport.getLabels()));
                statement.setBoolean(6, bugReport.isSolved());
                statement.setInt(7, bugReport.getId());

                statement.executeUpdate();
            } catch (SQLException exception){
                exception.printStackTrace();
            }
        } else{
            try(PreparedStatement statement = connection.prepareStatement(INSERT_REPORT)){
                statement.setInt(1, bugReport.getId());
                statement.setString(2, bugReport.getPlayerName());
                statement.setString(3, bugReport.getReportMessage());
                statement.setLong(4, bugReport.getCurrentTimeMillis());
                statement.setInt(5, bugReport.getPriority());
                statement.setString(6, String.join(",", bugReport.getLabels()));
                statement.setBoolean(7, bugReport.isSolved());

                statement.executeUpdate();
            } catch (SQLException exception){
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void saveAsync(BugReport bugReport, Callback<BugReport> callback){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            save(bugReport);
            callback.call(bugReport);
        });
    }

    @Override
    public void loadAsync(Integer id, Callback<BugReport> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            BugReport bugReport = load(id);
            callback.call(bugReport);
        });
    }

}
