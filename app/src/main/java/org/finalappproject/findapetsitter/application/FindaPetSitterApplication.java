package org.finalappproject.findapetsitter.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.UserProfile;

/**
 *
 */
public class FindaPetSitterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register Model classes
        ParseObject.registerSubclass(UserProfile.class);

        // set applicationId and server based on the values in the Heroku settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server(getString(R.string.parse_server_url)).build());
    }
}
