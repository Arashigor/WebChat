package model;

import java.util.Calendar;

public class Message {
    private String message;
    private String sender;
    private String date = now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH) + " " +
            now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE);

    private transient final static Calendar now = Calendar.getInstance();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
