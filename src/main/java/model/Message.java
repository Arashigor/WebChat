package model;

import java.util.Calendar;

public class Message {

    private String message;
    private String sender;
    private String userColor;
    private String receiver;
    private String date;

    private transient final static Calendar now = Calendar.getInstance();

    public Message() {
    }

    public Message(Message m) {
        this.message = m.message;
        this.sender = m.sender;
        this.userColor = m.userColor;
        this.receiver = m.receiver;
        this.date = m.date;
    }

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
        this.date = now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE)
                    + " " + now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH);
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean hasReceiver() {
        return receiver.length() > 0;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", userColor='" + userColor + '\'' +
                ", receiver='" + receiver + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
