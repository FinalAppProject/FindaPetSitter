package org.finalappproject.findapetsitter.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * User address
 */
@ParseClassName("Address")
public class Address extends ParseObject {
    private static final String KEY_LINE1 = "line1";
    private static final String KEY_LINE2 = "line2";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_ZIP_CODE = "zipCode";

    public String getLine1() {
        return getString(KEY_LINE1);
    }

    public void setLine1(String line1) {
        put(KEY_LINE1, line1);
    }

    public String getLine2() {
        return getString(KEY_LINE2);
    }

    public void setLine2(String line2) {
        put(KEY_LINE2, line2);
    }

    public String getCity() {
        return getString(KEY_CITY);
    }

    public void setCity(String city) {
        put(KEY_CITY, city);
    }

    public String getState() {
        return getString(KEY_STATE);
    }

    public void setState(String state) {
        put(KEY_STATE, state);
    }

    public String getZipCode() {
        return getString(KEY_ZIP_CODE);
    }

    public void setZipCode(String zipCode) {
        put(KEY_ZIP_CODE, zipCode);
    }

}
