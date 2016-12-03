package org.finalappproject.findapetsitter.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.interceptors.ParseLogInterceptor;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.EmergencyContact;
import org.finalappproject.findapetsitter.model.Pet;
import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.model.Review;
import org.finalappproject.findapetsitter.model.User;

/**
 * Find a Pet Sitter application class
 */
public class FindaPetSitterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register Parse sub-classes
        ParseUser.registerSubclass(User.class);
        // ParseObject sub-classes
        ParseObject.registerSubclass(Address.class);
        ParseObject.registerSubclass(EmergencyContact.class);
        ParseObject.registerSubclass(Pet.class);
        ParseObject.registerSubclass(Request.class);
        ParseObject.registerSubclass(Review.class);


        // Initialize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server(getString(R.string.parse_server_url)).build());
    }
}