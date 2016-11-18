package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.finalappproject.findapetsitter.activities.UserProfileActivity.EXTRA_USER_OBJECT_ID;

/**
 * Nearby Sitters Activity uses a GoogleMap fragment to show pet sitter near a location
 */
public class NearbySittersActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "NearbySittersActivity";

    SupportMapFragment mapFragment;
    GoogleMap map;

    LatLng mCurrentLocation;
    Map<String, User> mNearbyPetSitterMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_sitters);

        mNearbyPetSitterMarkers = new HashMap<>();
        setupMapFragment();
    }

    private void searchNearbySitters() {
        final ParseGeoPoint currentLocationGeoPoint = new ParseGeoPoint();
        currentLocationGeoPoint.setLatitude(mCurrentLocation.latitude);
        currentLocationGeoPoint.setLongitude(mCurrentLocation.longitude);

        User.queryPetSittersWithinMiles(currentLocationGeoPoint, 10, new FindCallback<User>() {
            @Override
            public void done(List<User> petSitters, ParseException e) {
                if (e == null) {
                    mNearbyPetSitterMarkers.clear();
                    addNearbySitterMarkers(petSitters);

                } else {
                    Log.e(TAG, "Failed to retrieve users", e);
                    Toast.makeText(NearbySittersActivity.this, "Failed to query nearby users", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addNearbySitterMarkers(List<User> petSitters) {
        for (User nearbySitter : petSitters) {
            try {
                Address nearbySitterAddress = nearbySitter.getAddress().fetchIfNeeded();
                ParseGeoPoint geoPoint = nearbySitterAddress.getGeoPoint();
                Marker marker = addPetSitterMarker(nearbySitter.getNickName(), new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
                mNearbyPetSitterMarkers.put(marker.getId(), nearbySitter);
            } catch (ParseException e) {
                Log.e(TAG, "Failed to add user marker", e);
            }
        }

    }

    private Marker addPetSitterMarker(String sitterNickName, LatLng petSitterLocation) {
        BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
        return map.addMarker(new MarkerOptions().position(petSitterLocation).title(sitterNickName).snippet("xxxx").icon(defaultMarker));
    }

    void setupMapFragment() {
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    setupMap(map);
                    zoomIntoUserAddress();
                    searchNearbySitters();
                }
            });
        } else {
            Toast.makeText(this, "ERROR - Map NOT found", Toast.LENGTH_SHORT).show();
        }
    }

    void setupMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {

            map.setOnMapLongClickListener(this);

            map.setOnMarkerClickListener(this);

        } else {
            Toast.makeText(this, "ERROR - Map NOT loaded", Toast.LENGTH_SHORT).show();
        }
    }

    void zoomIntoUserAddress() {
        User currentUser = (User) User.getCurrentUser();
        try {

            Address userAddress = currentUser.getAddress().fetchIfNeeded();
            ParseGeoPoint point = userAddress.getGeoPoint();
            mCurrentLocation = new LatLng(point.getLatitude(), point.getLongitude());
            //
            BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
            Marker marker = map.addMarker(new MarkerOptions().position(mCurrentLocation).title(currentUser.getFullName()).snippet(currentUser.getNickName()).icon(defaultMarker));

            // Zoom into the user's address latLng coordinates
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 12);
            map.animateCamera(cameraUpdate);
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch user address coordinates", e);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // TODO
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        User petSitter = mNearbyPetSitterMarkers.get(marker.getId());
        startUserProfileActivity(petSitter);
        return true;
    }

    private void startUserProfileActivity(User petSitter) {
        Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
        userProfileIntent.putExtra(EXTRA_USER_OBJECT_ID, petSitter.getObjectId());
        startActivity(userProfileIntent);
    }

}
