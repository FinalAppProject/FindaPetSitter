package org.finalappproject.findapetsitter.model;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Custom ParseUser implementation
 */

@ParseClassName("_User")
public class User extends ParseUser {

    private static final String LOG_TAG = "User";

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

    public ParseFile getProfileImage() {

        ParseFile profileImage = null;
        try {
            profileImage = fetchIfNeeded().getParseFile(KEY_PROFILE_IMAGE);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Failed to fetch profile image file", e);
        }
        return profileImage;
    }

    public void setProfileImage(ParseFile profileImage) {
        put(KEY_PROFILE_IMAGE, profileImage);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public Address getAddress() {
        Address address = (Address) getParseObject(KEY_ADDRESS);
        if (address == null) {
            address = new Address();
            setAddress(address);
        }
        return address;
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
        List<Pet> pets = getList(KEY_PETS);

        if (pets == null) {
            pets = new ArrayList<>();
            setPets(pets);
        }
        return pets;
    }

    public void setPets(List<Pet> pets) {
        put(KEY_PETS, pets);
    }

    public void addPet(Pet pet) {
        getPets().add(pet);
        saveInBackground();
    }

    public void addPet(Pet pet, SaveCallback saveCallback) {
        getPets().add(pet);
        saveInBackground(saveCallback);
    }

    public void removePet(Pet pet) {
        getPets().remove(pet);
        saveInBackground();
    }

    public void removePet(Pet pet, SaveCallback saveCallback) {
        getPets().remove(pet);
        saveInBackground(saveCallback);
    }

    public boolean isPetSitter() {
        return getBoolean(KEY_PET_SITTER);
    }

    public void setPetSitter(boolean petSitter) {
        put(KEY_PET_SITTER, petSitter);
    }

    public static void queryUser(String objectId, GetCallback<User> findCallback) {
        // TODO verify/validate cache policy
        ParseQuery<User> userQuery = ParseQuery.getQuery(User.class);
        userQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        userQuery.getInBackground(objectId, findCallback);
    }

    public static void queryPetSittersWithinMiles(ParseGeoPoint point, long miles, FindCallback<User> findCallback)
    {
        ParseQuery<Address> nearbyAddressesQuery = ParseQuery.getQuery(Address.class).whereWithinMiles(Address.KEY_GEO_POINT, point, miles);
        ParseQuery<User> nearbyUsersQuery = ParseQuery.getQuery(User.class).whereEqualTo(KEY_PET_SITTER, true).whereMatchesQuery(KEY_ADDRESS, nearbyAddressesQuery);
        nearbyUsersQuery.findInBackground(findCallback);
    }

    public static void queryPetSitters(FindCallback<User> findCallback)
    {
        ParseQuery<User> petSittersQuery = ParseQuery.getQuery(User.class).whereEqualTo(KEY_PET_SITTER, true);
        petSittersQuery.findInBackground(findCallback);
    }
}
