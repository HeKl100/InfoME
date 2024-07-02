package Scheduler;

import java.util.UUID;

public class TaskData
{
    private String id; // ID for every task
    private String taskType; // E-Mail or File-Creation
    private String frequency; // "daily", "weekly" or "monthly"
    private String time; // time format "HH:mm"
    private String dayOfWeek; // optional for weekly task
    private int dayOfMonth; // optional for monthly task

    public TaskData(String id, String taskType, String frequency, String time, String dayOfWeek, int dayOfMonth)
    {
        this.id = UUID.randomUUID().toString();
        this.taskType = taskType;
        this.frequency = frequency;
        this.time = time;
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
