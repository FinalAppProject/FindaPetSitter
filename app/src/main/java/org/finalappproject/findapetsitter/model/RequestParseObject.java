package org.finalappproject.findapetsitter.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("RequestParseObject")
public class RequestParseObject extends ParseObject{

    private static final String LOG_TAG = "Request";

    private static final String KEY_BEGIN_DATE = "begin_date";
    private static final String KEY_END_DATE = "end_date";

    //TODO replace by list of pets owned
    private static final String KEY_PET_TYPE = "pet_type";
    private static final String KEY_NOTE = "note";
    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECEIVER = "receiver";
    private static final String KEY_STATUS = "status";

    public Date getBeginDate() {
        Date date = getDate(KEY_BEGIN_DATE);
        return date;
    }

    public void setBeginDate(Date date) {
        put(KEY_BEGIN_DATE, date);
    }

    public Date getEndDate() {
        Date date = getDate(KEY_END_DATE);
        return date;
    }

    public void setEndDate(Date date) {
        put(KEY_END_DATE, date);
    }

    public PetType getType() {
        String type = getString(KEY_PET_TYPE);
        if (type != null) {
            return PetType.valueOf(type);
        }
        return null;
    }

    public void setType(PetType type) {
        if (type != null) {
            put(KEY_PET_TYPE, type.name());
        }
    }

    public String getNote() {
        return getString(KEY_NOTE);
    }

    public void setNote(String breed) {
        put(KEY_NOTE, breed);
    }

    public User getSender()  {
        return (User)getParseUser(KEY_SENDER);
    }

    public void setSender(User sender) {
        put(KEY_SENDER, sender);
    }

    public User getReceiver()  {
        return (User)getParseUser(KEY_RECEIVER);
    }

    public void setReceiver(User receiver) {
        put(KEY_RECEIVER, receiver);
    }

    public int getStatus(){
        return getInt(KEY_STATUS);
    }

    public void setStatus(int status){
        put (KEY_STATUS, status);
    }
}