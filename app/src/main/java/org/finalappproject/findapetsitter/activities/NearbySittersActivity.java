package org.finalappproject.findapetsitter.activities;

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
import com.parse.ParseGeoPoint;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.User;

/**
 * Nearby Sitters Activity uses a GoogleMap fragment to show pet sitter near a location
 */
public class NearbySittersActivity extends AppCompatActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private static final String TAG = "NearbySittersActivity";

    SupportMapFragment mapFragment;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_sitters);

        setupMapFragment();

    }

    void setupMapFragment() {
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    setupMap(map);
                    zoomIntoUserAddress();
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

            map.setOnMarkerDragListener(this);

        } else {
            Toast.makeText(this, "ERROR - Map NOT loaded", Toast.LENGTH_SHORT).show();
        }
    }

    void zoomIntoUserAddress() {
        User currentUser = (User) User.getCurrentUser();
        try {

            Address userAddress = currentUser.getAddress().fetchIfNeeded();
            ParseGeoPoint point = userAddress.getGeoPoint();
            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
            //
            BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
            Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(currentUser.getFullName()).snippet(currentUser.getNickName()).icon(defaultMarker));

            // Zoom into the user's address latLng coordinates
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
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
    public void onMarkerDragStart(Marker marker) {
        // TODO
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // TODO
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // TODO
    }


}
