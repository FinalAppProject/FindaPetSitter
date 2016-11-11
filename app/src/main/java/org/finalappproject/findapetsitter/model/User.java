package org.finalappproject.findapetsitter.model;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Custom ParseUser implementation
 */
@ParseClassName("_User")
public class User extends ParseUser {

    private static final String KEY_FULL_NAME = "fullName";

    public void setFullName(String fullName) {
        put(KEY_FULL_NAME, fullName);
    }

    public String getFullName() {
        return getString(KEY_FULL_NAME);
    }

}
