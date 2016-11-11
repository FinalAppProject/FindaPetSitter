package org.finalappproject.findapetsitter.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

@Parcel(analyze={UserProfile.class})
@ParseClassName("UserProfile")
public class UserProfile extends ParseObject {

    private static final String KEY_USER_ID = "userId";
    private static final String KEY_FULL_NAME = "fullName";
    // TODO add more properties
    // private static final String KEY_PHONE_NUMBER = "phoneNumber";


    public void setUserId(String userId) {
        put(KEY_USER_ID, userId);
    }

    public String getUserId() {
        return getString(KEY_USER_ID);
    }

    public void setFullName(String fullName) {
        put(KEY_FULL_NAME, fullName);
    }

    public String getFullName() {
        return getString(KEY_FULL_NAME);
    }

}
