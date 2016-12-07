package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.application.AppConstants;
import org.finalappproject.findapetsitter.model.Pet;
import org.finalappproject.findapetsitter.model.PetType;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Pet profile activity allows the user to add/modify its pets and visualize others users pets
 */
public class PetProfileActivity extends AppCompatActivity implements GetCallback<Pet>, SaveCallback {

    private static final String LOG_TAG = "PetProfileActivity";

    public static final int REQUEST_CODE_ADD_PET = 10;

    public static final int REQUEST_CODE_EDIT_PET = 20;

    public static final int RESULT_CODE_SAVE_SUCCESS = 10;

    public static final int RESULT_CODE_SAVE_FAILURE = 11;

    public static final String EXTRA_PET_POSITION = "EXTRA_PET_POSITION";

    public static final String EXTRA_PET_OBJECT_ID = "EXTRA_PET_OBJECT_ID";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etDescription)
    EditText etDescription;

//    @BindView(R.id.rgType)
//    RadioGroup rgType;
//
//    @BindView(R.id.rbDog)
//    RadioButton rbDog;
//
//    @BindView(R.id.rbCat)
//    RadioButton rbCat;

    @BindView(R.id.spPetType)
    Spinner spType;

    @BindView(R.id.etBreed)
    EditText etBreed;

    @BindView(R.id.etSpecialNeeds)
    EditText etSpecialNeeds;

    int mPosition;
    Pet mPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);
        // Bind views
        ButterKnife.bind(this);
        // Set support toolbar
        toolbar.setTitle(R.string.pet_profile_title);
        setSupportActionBar(toolbar);
        // Retrieve extra
        retrievePetExtra();
        // Setup views
        setupProfileImage();
        // Setup Spinner
        setupPetTypeSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSave:
                savePet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void setupProfileImage() {
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageHelper.startImagePickerActivity(PetProfileActivity.this);
            }
        });
    }

    void retrievePetExtra() {
        mPosition = -1;
        mPet = null;
        Intent petProfileIntent = getIntent();

        String petObjectId = petProfileIntent.getStringExtra(EXTRA_PET_OBJECT_ID);
        if (petObjectId != null && !petObjectId.isEmpty()) {
            mPosition = petProfileIntent.getIntExtra(EXTRA_PET_POSITION, -1);
            loadPet(petObjectId);
        }
    }

    void setupPetTypeSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.pet_type_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(spinnerAdapter);
        spType.setSelection(0, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppConstants.CODE_IMAGE_PICKER:
                setProfileImage(data);
                break;
        }
    }

    void loadPet(String objectId) {
        ParseQuery<Pet> query = ParseQuery.getQuery(Pet.class);
        // TODO verify/validate cache policy
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.getInBackground(objectId, this);
    }

    @Override
    public void done(Pet pet, ParseException e) {
        if (e == null) {
            mPet = pet;

            // Profile Image
            ImageHelper.loadImage(this, pet.getProfileImage(), R.drawable.cat, ivProfileImage);

            // Pet Type


            PetType type = pet.getType();
            if (type != null) {
                switch (type) {
                    case Dog:
                        spType.setSelection(0);
                        // rgType.check(R.id.rbDog);
                        break;
                    case Cat:
                        spType.setSelection(1);
                        // rgType.check(R.id.rbCat);
                        break;
                }
            }
//            pet.setType(type);
            // Remaining attributes
            etName.setText(pet.getName());
            etDescription.setText(pet.getDescription());
            etBreed.setText(pet.getBreed());
            etSpecialNeeds.setText(pet.getSpecialNeeds());

        } else {
            Log.e(LOG_TAG, "Failed to load pet", e);
            Toast.makeText(this, "Failed to load pet information", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    void setProfileImage(Intent data) {
        if (data != null) {
            // asBitmap is required to force the use of a BitmapDrawable into the ImageView
            // This may eventually fail e.g. Animated GIFs
            // for more info: https://github.com/bumptech/glide/issues/1083
            // TODO look for a better solution
            Glide.with(this).fromUri().asBitmap().load(data.getData()).placeholder(R.drawable.cat).into(ivProfileImage);
        }
    }

    void savePet() {
        boolean newPet = false;
        Pet pet = mPet;

        if (pet == null) {
            newPet = true;
            pet = new Pet();
            mPet = pet;
        }

        // Profile Image
        Drawable profileImageDrawable = ivProfileImage.getDrawable();

        if (profileImageDrawable != null) {
            if (profileImageDrawable instanceof BitmapDrawable) {
                Bitmap profileBitmap = ((BitmapDrawable) profileImageDrawable).getBitmap();
                ParseFile imageFile = ImageHelper.createParseFile(pet.getObjectId(), profileBitmap);
                pet.setProfileImage(imageFile);
            }
            // TODO handle failure to retrieve profile image from drawable
        }

        // Pet Type
        mPet.setType(PetType.valueOf(spType.getSelectedItem().toString()));
//        PetType type = null;
//        switch (rgType.getCheckedRadioButtonId()) {
//            case R.id.rbDog:
//                type = PetType.Dog;
//                break;
//            case R.id.rbCat:
//                type = PetType.Cat;
//                break;
//        }
//        pet.setType(type);

        // Remaining attributes
        pet.setName(etName.getText().toString());
        pet.setDescription(etDescription.getText().toString());
        pet.setBreed(etBreed.getText().toString());
        pet.setSpecialNeeds(etSpecialNeeds.getText().toString());

        // TODO pet.setEmergencyContact();

        if (newPet) {
            User appUser = (User) ParseUser.getCurrentUser();
            appUser.addPet(pet, this);
        } else {
            pet.saveInBackground(this);
        }
    }

    @Override
    public void done(ParseException e) {
        if (e == null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_PET_POSITION, mPosition);
            resultIntent.putExtra(EXTRA_PET_OBJECT_ID, mPet.getObjectId());
            setResult(RESULT_CODE_SAVE_SUCCESS, resultIntent);
        } else {
            Log.e(LOG_TAG, "Failed to save pet", e);
            setResult(RESULT_CODE_SAVE_FAILURE);
        }
        finish();
    }
}