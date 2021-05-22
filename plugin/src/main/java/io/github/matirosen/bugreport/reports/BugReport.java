package io.github.matirosen.bugreport.reports;

import java.util.ArrayList;
import java.util.List;

public class BugReport {

    private int id;
    private final String playerName, reportMessage;
    private final long currentTimeMillis;
    private final List<String> labels = new ArrayList<>();
    private int priority = 0;
    private boolean solved;
    private boolean exist;

    public BugReport(String playerName, String reportMessage, long currentTimeMillis, boolean exist){
        this.exist = exist;
        this.playerName = playerName;
        this.reportMessage = reportMessage;
        this.currentTimeMillis = currentTimeMillis;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public void addLabel(String label){
        labels.add(label);
    }

    public void removeLabel(String label){
        labels.remove(label);
    }

    public void setSolved(boolean solved){
        this.solved = solved;
    }

    public void setExist(boolean exists){
        this.exist = exists;
    }

    public int getId(){
        return id;
    }

    public String getPlayerName(){
        return playerName;
    }

    public String getReportMessage(){
        return reportMessage;
    }

    public long getCurrentTimeMillis(){
        return currentTimeMillis;
    }

    public int getPriority(){
        return priority;
    }

    public List<String> getLabels(){
        return labels;
    }

    public boolean isSolved(){
        return solved;
    }

    public boolean exists(){
        return exist;
    }
}
