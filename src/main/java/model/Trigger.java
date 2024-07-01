package model;

public class Trigger
{
    private int id;
    private String scheduleName;
    private String scheduleInterval;
    private String scheduleIntervalOption;
    private String getScheduleTime;

    public Trigger(int id, String scheduleName, String scheduleInterval, String scheduleIntervalOption, String getScheduleTime) {
        this.id = id;
        this.scheduleName = scheduleName;
        this.scheduleInterval = scheduleInterval;
        this.scheduleIntervalOption = scheduleIntervalOption;
        this.getScheduleTime = getScheduleTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getScheduleInterval() {
        return scheduleInterval;
    }

    public void setScheduleInterval(String scheduleInterval) {
        this.scheduleInterval = scheduleInterval;
    }

    public String getScheduleIntervalOption() {
        return scheduleIntervalOption;
    }

    public void setScheduleIntervalOption(String scheduleIntervalOption) {
        this.scheduleIntervalOption = scheduleIntervalOption;
    }

    public String getGetScheduleTime() {
        return getScheduleTime;
    }

    public void setGetScheduleTime(String getScheduleTime) {
        this.getScheduleTime = getScheduleTime;
    }
}
