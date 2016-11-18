package org.finalappproject.findapetsitter.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * User address
 */
@ParseClassName("Address")
public class Address extends ParseObject {
    static final String KEY_ADDRESS = "address";
    static final String KEY_CITY = "city";
    static final String KEY_STATE = "state";
    static final String KEY_ZIP_CODE = "zipCode";
    static final String KEY_GEO_POINT = "geoPoint";

    public String getAddress() {
        return getString(KEY_ADDRESS);
    }

    public void setAddress(String address) {
        put(KEY_ADDRESS, address);
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

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint(KEY_GEO_POINT);
    }

    public void setGeoPoint(ParseGeoPoint geoPoint) {
        put(KEY_GEO_POINT, geoPoint);
    }

}
