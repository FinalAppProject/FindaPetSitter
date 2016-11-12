package org.finalappproject.findapetsitter.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * User Pets or Pet emergency contact
 */
@ParseClassName("EmergencyContact")
public class EmergencyContact extends ParseObject {
    private static final String KEY_HOSPITAL = "hospital";
    private static final String KEY_VETERINARY = "veterinary";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";

    public String getHospital() {
        return getString(KEY_HOSPITAL);
    }

    public void setHospital(String hospital) {
        put(KEY_HOSPITAL, hospital);
    }

    public String getVeterinary() {
        return getString(KEY_VETERINARY);
    }

    public void setVeterinary(String veterinary) {
        put(KEY_VETERINARY, veterinary);
    }

    public String getPhone() {
        return getString(KEY_PHONE);
    }

    public void setPhone(String phone) {
        put(KEY_PHONE, phone);
    }

    public Address getAddress() {
        return (Address) getParseObject(KEY_ADDRESS);
    }

    public void setAddress(Address address) {
        put(KEY_ADDRESS, address);
    }
}
