package org.finalappproject.findapetsitter.model;

import java.util.Date;

public class Request {

    private Date beginDate;
    private Date endDate;
    private PetType type;
    private String note;
    private User sender;
    private User receiver;
    private int status;

    //These should maybe go in the utils contants folder
    public static final int REQUEST_PENDING=0, REQUEST_REJECTED=1, REQUEST_ACCEPTED=2;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PetType: " + this.getType() + "\nNote : " + this.getNote()  + "\nDate: " + this.getBeginDate().toString() + " ---- " + this.getEndDate().toString();
    }
}