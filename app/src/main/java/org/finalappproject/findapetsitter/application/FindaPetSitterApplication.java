package org.finalappproject.findapetsitter.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.interceptors.ParseLogInterceptor;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.User;

/**
 * Find a Pet Sitter application class
 */
public class FindaPetSitterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register Parse classes
        ParseUser.registerSubclass(User.class);

        // Initialize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server(getString(R.string.parse_server_url)).build());
    }
}
