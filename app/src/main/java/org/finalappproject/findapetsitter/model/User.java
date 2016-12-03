package org.finalappproject.findapetsitter.model;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Custom ParseUser implementation
 */
@ParseClassName("_User")
public class User extends ParseUser {

    private static final String LOG_TAG = "User";

    private static final String KEY_FULL_NAME = "fullName";

    private static final String KEY_NICK_NAME = "nickName";

    private static final String KEY_PROFILE_IMAGE = "profileImage";

    private static final String KEY_FCM_TOKEN = "fcmToken";

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

    public String getProfileImageUrl() {
        return getProfileImage().getUrl();
    }

    public void setProfileImage(ParseFile profileImage) {
        put(KEY_PROFILE_IMAGE, profileImage);
    }

    public void setFcmToken(String fcmToken) {
        put(KEY_FCM_TOKEN, fcmToken);
    }

    public String getFcmToken() {
        return getString(KEY_FCM_TOKEN);
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

    public void addPet(Pet pet) throws ParseException {
        getPets().add(pet);
        save();
    }

    public void addPet(Pet pet, SaveCallback saveCallback) {
        getPets().add(pet);
        saveInBackground(saveCallback);
    }

    public void removePet(Pet pet) throws ParseException {
        getPets().remove(pet);
        save();
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

    public static void queryPetSittersWithinMiles(ParseGeoPoint point, long miles, FindCallback<User> findCallback) {
        ParseQuery<Address> nearbyAddressesQuery = ParseQuery.getQuery(Address.class).whereWithinMiles(Address.KEY_GEO_POINT, point, miles);
        ParseQuery<User> nearbyUsersQuery = ParseQuery.getQuery(User.class).whereEqualTo(KEY_PET_SITTER, true).whereMatchesQuery(KEY_ADDRESS, nearbyAddressesQuery);
        nearbyUsersQuery.findInBackground(findCallback);
    }

    /**
     * Query Pet Sitter Users
     * @param findCallback
     */
    public static void queryPetSitters(FindCallback<User> findCallback) {
        ParseQuery<User> petSittersQuery = ParseQuery.getQuery(User.class).whereEqualTo(KEY_PET_SITTER, true).addAscendingOrder(KEY_NICK_NAME);
        // Include addresses when querying pet sitters
        petSittersQuery.include(KEY_ADDRESS);
        petSittersQuery.findInBackground(findCallback);
    }

    /**
     * Query Pet Sitter Users ordered by how close they are to the current User
     * @param findCallback
     */
    public static void queryPetSittersNear(final FindCallback<User> findCallback) {
        User.getCurrentUser().fetchIfNeededInBackground(new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                final User currentUser = user;
                Address address = currentUser.getAddress();
                if (address != null) {
                    address.fetchIfNeededInBackground(new GetCallback<Address>() {
                        @Override
                        public void done(Address address, ParseException e) {
                            ParseGeoPoint addressGeoPoint = address.getGeoPoint();
                            if (addressGeoPoint != null) {
                                // Lets query addresses sorted by geo point
                                final ParseQuery<Address> addressesNearQuery = ParseQuery.getQuery(Address.class).whereNear(Address.KEY_GEO_POINT, addressGeoPoint);
                                addressesNearQuery.findInBackground(new FindCallback<Address>() {
                                    @Override
                                    public void done(List<Address> addresses, ParseException e) {
                                        if (e == null) {
                                            final List<Address> geoLocationSortedAddresses = addresses;

                                            queryPetSitters(new FindCallback<User>() {
                                                @Override
                                                public void done(List<User> sitters, ParseException e) {
                                                    if (e == null) {
                                                        List<User> sortedByAddresses = new ArrayList<User>();

                                                        // Remove sitters that contain address from the list and store into the addressSitter map
                                                        Map<String, User> addressSitterMap = new HashMap<String, User>();
                                                        Iterator<User> sitterIterator = sitters.iterator();
                                                        while (sitterIterator.hasNext()) {
                                                            User sitter = sitterIterator.next();
                                                            if (sitter == currentUser) {
                                                                sitterIterator.remove();
                                                            } else if (sitter.getAddress() != null) {
                                                                sitterIterator.remove();
                                                                addressSitterMap.put(sitter.getAddress().getObjectId(), sitter);
                                                            }
                                                        }

                                                        // Add sitters sorted by address
                                                        for (Address sortedAddress : geoLocationSortedAddresses) {
                                                            User sitter = addressSitterMap.get(sortedAddress.getObjectId());
                                                            if (sitter != null) {
                                                                sortedByAddresses.add(sitter);
                                                            }
                                                        }

                                                        // Add remaining sitters (that do not have address or geopoint)
                                                        sortedByAddresses.addAll(sitters);

                                                        findCallback.done(sortedByAddresses, null);
                                                    } else {
                                                        // Send exception to callback
                                                        findCallback.done(null, e);
                                                    }
                                                }
                                            });
                                        } else {
                                            // Send exception to callback
                                            findCallback.done(null, e);
                                        }
                                    }
                                });
                            } else {
                                // User address doesn't have geopoint, lets fallback to the simpler query
                                queryPetSitters(findCallback);
                            }
                        }
                    });
                } else {
                    // User doesn't have address, lets fallback to the simpler query
                    queryPetSitters(findCallback);
                }
            }
        });
    }
}
