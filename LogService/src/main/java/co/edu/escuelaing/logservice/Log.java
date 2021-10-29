package co.edu.escuelaing.logservice;

public class Log{

    private String log;
    private String date;

    public Log(){}

    public Log(String log, String date){
        this.log = log;
        this.date = date;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString(){
        return "{log:" + this.log + ", date: " + this.date + "}";
    }
}
