package org.finalappproject.findapetsitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.HomeActivity;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Nearby Sitters fragment implementation uses a GoogleMap fragment to show pet sitter near a location
 *
 * Activities that contain this fragment must implement the
 * {link NearbySittersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * Use the {@link NearbySittersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearbySittersFragment extends Fragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href="http://developer.android.com/training/basics/fragments/communicating.html">
     * Communicating with Other Fragments</a> for more information.
     */
    public interface NearbySittersFragmentListener {

    }

    private static final String TAG = "NearbySittersFragment";

    private NearbySittersFragmentListener mListener;

    private Unbinder mUnbinder;

    @BindView(R.id.mapView)
    MapView mMapView;

    GoogleMap mMap;

    LatLng mCurrentLocation;

    Map<String, User> mNearbyPetSitterMarkers;

    /**
     * Required empty public constructor.
     * Use the {@link NearbySittersFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    @Deprecated
    public NearbySittersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NearbySittersFragment.
     */
    public static NearbySittersFragment newInstance() {
        NearbySittersFragment fragment = new NearbySittersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize members
        mNearbyPetSitterMarkers = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and bind views
        View view = inflater.inflate(R.layout.fragment_nearby_sitters, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMapFragment(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unbind views
        mUnbinder.unbind();
    }

    void setupMapFragment(View parentView, @Nullable Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        // needed to get the map to display immediately
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                setupMap(map);
                zoomIntoUserAddress();
                searchNearbySitters();
            }
        });

    }

    void setupMap(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {

            mMap.setOnMapLongClickListener(this);

            mMap.setOnMarkerClickListener(this);

        } else {
            Toast.makeText(getContext(), "ERROR - Map NOT loaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchNearbySitters() {
        final ParseGeoPoint currentLocationGeoPoint = new ParseGeoPoint();
        if (mCurrentLocation != null) {
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
                        Toast.makeText(getContext(), "Failed to query nearby users", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void addNearbySitterMarkers(List<User> petSitters) {
        for (User nearbySitter : petSitters) {
            // Prevent itself from showing up in the map, TODO should probably have filtered the query
            if (nearbySitter.getObjectId().equals(User.getCurrentUser().getObjectId())) {
                continue;
            }
            //
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
        return mMap.addMarker(new MarkerOptions().position(petSitterLocation).title(sitterNickName).snippet("xxxx").icon(defaultMarker));
    }

    void zoomIntoUserAddress() {
        User currentUser = (User) User.getCurrentUser();
        try {
            currentUser.fetchIfNeeded();
        } catch (ParseException e) {
            Log.e(TAG, "Failed to fetch user", e);
        }
        try {

            Address userAddress = currentUser.getAddress().fetchIfNeeded();
            ParseGeoPoint point = userAddress.getGeoPoint();
            mCurrentLocation = new LatLng(point.getLatitude(), point.getLongitude());
            //
            BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
            Marker marker = mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title(currentUser.getFullName()).snippet(currentUser.getNickName()).icon(defaultMarker));

            // Zoom into the user's address latLng coordinates
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 12);
            mMap.animateCamera(cameraUpdate);
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
        // Sitter will be null if the user clicks on its own marker
        if (petSitter != null) {
            startUserProfileActivity(petSitter);
        }
        return true;
    }

    private void startUserProfileActivity(User petSitter) {
        ((HomeActivity) getContext()).showUserProfileFragment(petSitter.getObjectId());
    }

    @Override
    public void onResume() {
        super.onResume();
        zoomIntoUserAddress();
        searchNearbySitters();
    }
}
