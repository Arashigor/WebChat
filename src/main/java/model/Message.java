package model;

import java.util.Calendar;
import java.util.TimeZone;

public class Message {
    private String message;
    private String sender;
    private String userColor;
    private String whom;

    private String date;
    private transient final static Calendar now = Calendar.getInstance();

    public String getColor() {
        return userColor;
    }

    public void setColor(String color) {
        this.userColor = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate() {
        this.date = now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH) + " " +
                now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE);
    }

    public String getUserColor() {
        return userColor;
    }

    public void setUserColor(String userColor) {
        this.userColor = userColor;
    }

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

    public String getWhom() {
        return whom;
    }

    public void setWhom(String whom) {
        this.whom = whom;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", userColor='" + userColor + '\'' +
                ", whom='" + whom + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
