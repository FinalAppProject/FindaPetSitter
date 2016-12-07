package org.finalappproject.findapetsitter.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
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
 * <p>
 * Activities that contain this fragment must implement the
 * {link NearbySittersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * <p>
 * Use the {@link NearbySittersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearbySittersFragment extends Fragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraIdleListener {

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

    private static final String STATE_PAN_ENABLED = "PAN_ENABLED";
    private static final String STATE_CAMERA_POSITION = "CAMERA_POSITION";

    private NearbySittersFragmentListener mListener;

    private Unbinder mUnbinder;

    @BindView(R.id.mapView)
    MapView mMapView;

    @BindView(R.id.ibPanTool)
    ImageButton ibPanTool;

    GoogleMap mMap;

    CameraPosition mCameraSavedState;

    boolean mPanEnabled;

    Map<Marker, User> mNearbyPetSitterMarkers;

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
     *
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
        setupPanTool();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mPanEnabled = savedInstanceState.getBoolean(STATE_PAN_ENABLED, false);
            if (savedInstanceState.containsKey(STATE_CAMERA_POSITION)) {
                mCameraSavedState = savedInstanceState.getParcelable(STATE_CAMERA_POSITION);
            } else {
                mCameraSavedState = null;
            }
            onPanToolStateChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_PAN_ENABLED, mPanEnabled);

        if (mMap != null) {
            outState.putParcelable(STATE_CAMERA_POSITION, mMap.getCameraPosition());
        }
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
                zoomCamera();
            }
        });

    }

    void setupMap(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {

            onPanToolStateChanged();

            mMap.setOnMapLongClickListener(this);
            mMap.setOnMarkerClickListener(this);
            mMap.setOnCameraIdleListener(this);

        } else {
            Toast.makeText(getContext(), "ERROR - Map NOT loaded", Toast.LENGTH_SHORT).show();
            ibPanTool.setVisibility(View.INVISIBLE);
        }
    }

    void setupPanTool() {
        ibPanTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanEnabled = !mPanEnabled;
                onPanToolStateChanged();
            }
        });
    }

    private void onPanToolStateChanged() {
        int buttonImageResource = R.drawable.ic_pan_tool;
        boolean scrollGestureEnabled = false;

        if (mPanEnabled) {
            buttonImageResource = R.drawable.ic_pan_tool_lock;
            scrollGestureEnabled = true;
        }

        // Setup pan tool button
        if (ibPanTool != null) {
            ibPanTool.setImageResource(buttonImageResource);
        }

        // Setup map settings
        if (mMap != null) {
            UiSettings settings = mMap.getUiSettings();
            settings.setScrollGesturesEnabled(scrollGestureEnabled);
        }
    }

    private void addNearbySitterMarkers(List<User> petSitters) {
        for (User nearbySitter : petSitters) {
            // Prevent itself from showing up in the map, TODO should probably have filtered the query
            if (nearbySitter.getObjectId().equals(User.getCurrentUser().getObjectId())) {
                continue;
            }
            //
            if (mMap != null) {
                Address nearbySitterAddress = nearbySitter.getAddress();
                ParseGeoPoint geoPoint = nearbySitterAddress.getGeoPoint();
                addPetSitterMarker(nearbySitter, new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
            }
        }
    }

    private void addPetSitterMarker(final User sitter, final LatLng petSitterLocation) {
        final IconGenerator iconGenerator = new IconGenerator(getContext());
        final ImageView profileImageView = new ImageView(getContext());
        profileImageView.setMinimumWidth(20);
        if (sitter.getProfileImage() != null) {
//            sitter.getProfileImage().getDataInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    Glide.with(getContext()).load(data).centerCrop().placeholder(R.drawable.cat).dontAnimate().into(profileImageView);
//                }
//            });

            sitter.getProfileImage().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Glide.with(getContext())
                                .load(data).asBitmap()
                                .centerCrop()
                                .placeholder(R.drawable.cat)
                                .dontAnimate()
                                .into(new SimpleTarget<Bitmap>(100, 100) {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                        profileImageView.setImageBitmap(bitmap);
                                        iconGenerator.setContentView(profileImageView);
                                        BitmapDescriptor profileMarker = BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon());
                                        Marker marker = mMap.addMarker(new MarkerOptions().position(petSitterLocation).title(sitter.getNickName()).snippet("snippet").icon(profileMarker));
                                        mNearbyPetSitterMarkers.put(marker, sitter);
                                    }
                                });
                    } else {
                        Glide.with(getContext()).load(data).centerCrop().placeholder(R.drawable.cat).dontAnimate().into(profileImageView);
                        BitmapDescriptor profileMarker = BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon());
                        Marker marker = mMap.addMarker(new MarkerOptions().position(petSitterLocation).title(sitter.getNickName()).snippet("snippet").icon(profileMarker));
                        mNearbyPetSitterMarkers.put(marker, sitter);
                    }
                }
            });

//                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                profileImageView.setImageBitmap(bmp);

        }

    }

    void zoomCamera() {

        final User currentUser = (User) User.getCurrentUser();
        currentUser.fetchIfNeededInBackground(new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                if (e == null) {
                    currentUser.getAddress().fetchIfNeededInBackground(new GetCallback<Address>() {
                        @Override
                        public void done(Address userAddress, ParseException e) {
                            ParseGeoPoint point = userAddress.getGeoPoint();
                            if (e != null || userAddress.getGeoPoint() == null) {
                                // TODO try using the currend device location
                                return;
                            }
                            LatLng userAddressLatLng = new LatLng(point.getLatitude(), point.getLongitude());
                            BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                            Marker marker = mMap.addMarker(new MarkerOptions().position(userAddressLatLng).title(currentUser.getFullName()).snippet(currentUser.getNickName()).icon(defaultMarker));

                            if (mCameraSavedState != null) {
                                // Restore camera state
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraSavedState));
                            } else {
                                // Zoom into the user's address latLng coordinates
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userAddressLatLng, 12);
                                mMap.animateCamera(cameraUpdate);
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "Failed to fetch current user", e);
                    if (mCameraSavedState != null) {
                        // Restore camera state
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraSavedState));
                    }
                }
            }
        });
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // TODO
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        User petSitter = mNearbyPetSitterMarkers.get(marker);
        // Sitter will be null if the user clicks on its own marker
        if (petSitter != null) {
            startUserProfileActivity(petSitter);
        }
        return true;
    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = mMap.getCameraPosition();

        LatLng target = cameraPosition.target;
        LatLng ne = mMap.getProjection().getVisibleRegion().latLngBounds.northeast;
        LatLng sw = mMap.getProjection().getVisibleRegion().latLngBounds.southwest;

        final ParseGeoPoint targetGeoPoint = new ParseGeoPoint(target.latitude, target.longitude);
        ParseGeoPoint neGeoPoint = new ParseGeoPoint(ne.latitude, ne.longitude);
        ParseGeoPoint swGeoPoint = new ParseGeoPoint(sw.latitude, sw.longitude);

        double milesNe = targetGeoPoint.distanceInMilesTo(neGeoPoint);
        double milesSw = targetGeoPoint.distanceInMilesTo(swGeoPoint);

        double distance = milesNe;
        if (milesSw > milesNe) {
            distance = milesSw;
        }

        User.queryPetSittersWithinMiles(targetGeoPoint, distance, new FindCallback<User>() {
            @Override
            public void done(List<User> petSitters, ParseException e) {
                if (e == null) {
                    // Remove existing markers
                    for (Marker marker : mNearbyPetSitterMarkers.keySet()) {
                        marker.remove();
                    }
                    mNearbyPetSitterMarkers.clear();

                    addNearbySitterMarkers(petSitters);
                } else {
                    Log.e(TAG, "Failed to retrieve users", e);
                    Toast.makeText(getContext(), "Failed to query nearby users", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void startUserProfileActivity(User petSitter) {
        ((HomeActivity) getContext()).showUserProfileFragment(petSitter.getObjectId());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
