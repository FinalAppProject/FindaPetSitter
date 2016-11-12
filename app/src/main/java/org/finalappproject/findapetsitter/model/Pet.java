package org.finalappproject.findapetsitter.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * User pet
 */
@ParseClassName("Pet")
public class Pet extends ParseObject {
    private static final String KEY_TYPE = "type";
    private static final String KEY_BREED = "breed";
    private static final String KEY_NAME = "name";
    private static final String KEY_PROFILE_IMAGE = "profileImage";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SPECIAL_NEEDS = "specialNeeds";
    private static final String KEY_EMERGENCY_CONTANT = "emergencyContact";

    public PetTypes getType() {
        return PetTypes.valueOf(getString(KEY_TYPE));
    }

    public void setType(PetTypes type) {
        put(KEY_TYPE, type.name());
    }

    public String getBreed() {
        return getString(KEY_BREED);
    }

    public void setBreed(String breed) {
        put(KEY_BREED, breed);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getProfileImage() {
        return getString(KEY_PROFILE_IMAGE);
    }

    public void setProfileImage(String profileImage) {
        put(KEY_PROFILE_IMAGE, profileImage);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getSpecialNeeds() {
        return getString(KEY_SPECIAL_NEEDS);
    }

    public void setSpecialNeeds(String specialNeeds) {
        put(KEY_SPECIAL_NEEDS, specialNeeds);
    }

    public EmergencyContact getEmergencyContact() {
        return (EmergencyContact) getParseObject(KEY_EMERGENCY_CONTANT);
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        put(KEY_EMERGENCY_CONTANT, emergencyContact);
    }
}
