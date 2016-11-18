package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.adapters.PetsAdapter;
import org.finalappproject.findapetsitter.application.AppConstants;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.Pet;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;
import org.finalappproject.findapetsitter.util.recyclerview.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.finalappproject.findapetsitter.activities.PetProfileActivity.EXTRA_PET_OBJECT_ID;
import static org.finalappproject.findapetsitter.activities.PetProfileActivity.EXTRA_PET_POSITION;
import static org.finalappproject.findapetsitter.activities.PetProfileActivity.REQUEST_CODE_ADD_PET;
import static org.finalappproject.findapetsitter.activities.PetProfileActivity.REQUEST_CODE_EDIT_PET;
import static org.finalappproject.findapetsitter.activities.PetProfileActivity.RESULT_CODE_SAVE_FAILURE;
import static org.finalappproject.findapetsitter.model.User.queryUser;

/**
 * User profile activity
 */
public class UserProfileActivity extends AppCompatActivity implements SaveCallback, GetCallback<User> {

    private static final String LOG_TAG = "UserProfileActivity";

    public static final String EXTRA_USER_OBJECT_ID = "EXTRA_USER_OBJECT_ID";

    @BindView(R.id.cbPetSitter)
    CheckBox cbPetSitter;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @BindView(R.id.etFullName)
    EditText etFullName;

    @BindView(R.id.etNickName)
    EditText etNickName;

    @BindView(R.id.etDescription)
    EditText etDescription;

    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.etZipCode)
    EditText etZipCode;

    @BindView(R.id.etCity)
    EditText etCity;

    @BindView(R.id.etState)
    EditText etState;

    @BindView(R.id.rvPets)
    RecyclerView rvPets;

    @BindView(R.id.btAddPet)
    Button btAddPet;

    @BindView(R.id.btSave)
    Button btSave;

    User mUser;

    List<Pet> mPets;

    PetsAdapter mPetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        // Bind views
        ButterKnife.bind(this);
        setupPetsRecyclerView();
        retrieveUserExtra();
        setupProfileImage();
        setupAddPetButton();
        setupSaveButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppConstants.CODE_IMAGE_PICKER:
                setProfileImage(data);
                break;
            case REQUEST_CODE_EDIT_PET:
                onPetEdited(resultCode, data);
                break;
            case REQUEST_CODE_ADD_PET:
                onPetAdded(resultCode, data);
                break;
        }
    }

    void setupProfileImage() {
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageHelper.startImagePickerActivity(UserProfileActivity.this);
            }
        });
    }


    private void setupAddPetButton() {
        btAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPetProfileActivity();
            }
        });
    }

    private void setupSaveButton() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });
    }

    private void setupPetsRecyclerView() {
        mPets = new ArrayList<>();
        mPetsAdapter = new PetsAdapter(this, mPets);
        rvPets.setAdapter(mPetsAdapter);
        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(this);
        linerLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        rvPets.setLayoutManager(linerLayoutManager);

        ItemClickSupport.addTo(rvPets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        startPetProfileActivity(position);
                    }
                }
        );
    }

    void retrieveUserExtra() {
        mUser = null;

        Intent userProfileIntent = getIntent();
        String userObjectId = userProfileIntent.getStringExtra(EXTRA_USER_OBJECT_ID);

        if (userObjectId != null && !userObjectId.isEmpty()) {
            queryUser(userObjectId, this);
        } else {
            mUser = (User) User.getCurrentUser();
            loadUser();
        }
    }

    @Override
    public void done(User user, ParseException e) {
        if (e == null) {
            mUser = user;
            loadUser();
        } else {
            Log.e(LOG_TAG, "Failed to fetch user", e);
            Toast.makeText(this, "Failed to fetch user", Toast.LENGTH_LONG).show();
        }
    }


    void loadUser() {

        ImageHelper.loadImage(this, mUser.getProfileImage(), R.drawable.account_plus, ivProfileImage);

        cbPetSitter.setChecked(mUser.isPetSitter());
        etFullName.setText(mUser.getFullName());
        etNickName.setText(mUser.getNickName());
        etDescription.setText(mUser.getDescription());
        etPhoneNumber.setText(mUser.getPhone());

        mUser.getAddress().fetchIfNeededInBackground(new GetCallback<Address>() {
            @Override
            public void done(Address address, ParseException e) {
                if (e == null) {
                    setupAddressViews(address);
                } else {
                    Log.e(LOG_TAG, "Failed to fetch user address data", e);
                    Toast.makeText(UserProfileActivity.this, "Failed to fetch user address", Toast.LENGTH_LONG).show();
                }
            }
        });

        List<Pet> userPets = mUser.getPets();
        if (userPets != null) {
            mPets.addAll(userPets);
            mPetsAdapter.notifyItemRangeInserted(0, userPets.size());
        }

    }

    void setupAddressViews(Address address) {
        etAddress.setText(address.getAddress());
        etZipCode.setText(address.getZipCode());
        etCity.setText(address.getCity());
        etState.setText(address.getState());
    }

    void setProfileImage(Intent data) {
        if (data != null) {
            Glide.with(this).fromUri().asBitmap().load(data.getData()).placeholder(R.drawable.account_plus).into(ivProfileImage);
        }
    }

    /**
     * Starts the pet profile activity
     */
    private void startPetProfileActivity(int position) {
        Pet pet = mPets.get(position);
        Intent petProfileIntent = new Intent(this, PetProfileActivity.class);
        petProfileIntent.putExtra(EXTRA_PET_POSITION, position);
        petProfileIntent.putExtra(EXTRA_PET_OBJECT_ID, pet.getObjectId());
        startActivityForResult(petProfileIntent, REQUEST_CODE_EDIT_PET);
    }

    private void startPetProfileActivity() {
        Intent petProfileIntent = new Intent(this, PetProfileActivity.class);
        startActivityForResult(petProfileIntent, REQUEST_CODE_ADD_PET);
    }

    void onPetAdded(int resultCode, Intent data) {
        if (resultCode == PetProfileActivity.RESULT_CODE_SAVE_SUCCESS) {
            String petObjectId = data.getStringExtra(EXTRA_PET_OBJECT_ID);

            ParseQuery<Pet> query = ParseQuery.getQuery(Pet.class);
            // TODO verify/validate cache policy
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.getInBackground(petObjectId, new GetCallback<Pet>() {
                @Override
                public void done(Pet pet, ParseException e) {
                    if (e == null) {
                        addPet(pet);
                    } else {
                        Log.e(LOG_TAG, "Failed to load pet", e);
                        Toast.makeText(UserProfileActivity.this, "Failed to fetch added pet", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (resultCode == RESULT_CODE_SAVE_FAILURE) {
            Toast.makeText(this, "Failed to add pet", Toast.LENGTH_LONG).show();
        }
    }

    void addPet(Pet pet) {
        int size = mPets.size();
        mPets.add(pet);
        mPetsAdapter.notifyItemInserted(size);
    }

    void onPetEdited(int resultCode, Intent data) {
        if (resultCode == PetProfileActivity.RESULT_CODE_SAVE_SUCCESS) {
            final int position = data.getIntExtra(EXTRA_PET_POSITION, -1);

            if (position != -1) {
                Pet pet = mPets.get(position);
                pet.fetchInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            mPetsAdapter.notifyItemChanged(position);
                        } else {
                            Log.e(LOG_TAG, "Failed to fetch edited pet", e);
                            Toast.makeText(UserProfileActivity.this, "Failed to fetch pet changes", Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        } else if (resultCode == RESULT_CODE_SAVE_FAILURE) {
            Toast.makeText(this, "Failed to edit pet", Toast.LENGTH_LONG).show();
        }
    }

    void saveUser() {

        // Profile Image
        Drawable profileImageDrawable = ivProfileImage.getDrawable();

        if (profileImageDrawable != null) {
            if (profileImageDrawable instanceof BitmapDrawable) {
                Bitmap profileBitmap = ((BitmapDrawable) profileImageDrawable).getBitmap();
                ParseFile imageFile = ImageHelper.createParseFile(mUser.getObjectId(), profileBitmap);
                mUser.setProfileImage(imageFile);
            }
            // TODO handle failure to retrieve profile image from drawable
        }

        mUser.setPetSitter(cbPetSitter.isChecked());
        mUser.setFullName(etFullName.getText().toString());
        mUser.setNickName(etNickName.getText().toString());
        mUser.setDescription(etDescription.getText().toString());
        mUser.setPhone(etPhoneNumber.getText().toString());

        // User address
        Address userAddress = mUser.getAddress();

        String address = etAddress.getText().toString();
        String city = etCity.getText().toString();
        String state = etState.getText().toString();
        String zipCode = etZipCode.getText().toString();

        userAddress.setAddress(address);
        userAddress.setZipCode(zipCode);
        userAddress.setCity(city);
        userAddress.setState(state);

        // Retrieve the address geolocation and save the user
        String formattedAddress = String.format("%s, %s, %s, %s", address, city, state, zipCode);
        GeoApiContext context = new GeoApiContext().setApiKey(getString(R.string.api_key_google_maps));
        try {
            // TODO await/synchronous, is this a bad practice ?
            GeocodingResult[] results = GeocodingApi.geocode(context, formattedAddress).await();

            if (results != null) {
                ParseGeoPoint geoPoint = new ParseGeoPoint();
                geoPoint.setLatitude(results[0].geometry.location.lat);
                geoPoint.setLongitude(results[0].geometry.location.lng);
                userAddress.setGeoPoint(geoPoint);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to retrieve user's address geo location", e);
        }

        mUser.saveInBackground(this);
    }

    @Override
    public void done(ParseException e) {
        if (e == null) {
            // TODO return result ?
        } else {
            Log.e(LOG_TAG, "Failed to save user", e);
            // TODO return result ?
            // setResult(RESULT_CODE_SAVE_FAILURE);
        }
        finish();
    }
}
