package org.finalappproject.findapetsitter.model;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.List;

/**
 * Custom ParseUser implementation
 */
@ParseClassName("_User")
public class User extends ParseUser {

    private static final String KEY_FULL_NAME = "fullName";

    private static final String KEY_NICK_NAME = "nickName";

    private static final String KEY_PROFILE_IMAGE = "profileImage";

    private static final String KEY_DESCRIPTION = "description";

    private static final String KEY_ADDRESS = "address";

    private static final String KEY_PHONE = "phone";

    private static final String KEY_PETS = "pets";

    private static final String KEY_PET_SITTER = "petSitter";

    public void setFullName(String fullName) {
        put(KEY_FULL_NAME, fullName);
    }

    public String getFullName() {
        return getString(KEY_FULL_NAME);
    }

    public void setNickName(String nickName) {
        put(KEY_NICK_NAME, nickName);
    }

    public String getNickName() {
        return getString(KEY_NICK_NAME);
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

    public Address getAddress() {
        return (Address) getParseObject(KEY_ADDRESS);
    }

    public void setAddress(Address address) {
        put(KEY_ADDRESS, address);
    }

    public String getPhone() {
        return getString(KEY_PHONE);
    }

    public void setPhone(String phone) {
        put(KEY_PHONE, phone);
    }

    public List<Pet> getPets() {
        return getList(KEY_PETS);
    }

    public void setPets(List<Pet> pets) {
        put(KEY_PETS, pets);
    }

    public boolean isPetSitter() {
        return getBoolean(KEY_PET_SITTER);
    }

    public void setPetSitter(boolean petSitter) {
        put(KEY_PET_SITTER, petSitter);
    }
}
