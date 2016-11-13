package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.finalappproject.findapetsitter.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * User profile activity
 */
public class UserProfileActivity extends AppCompatActivity {

    // TODO Bind other views

    @BindView(R.id.btAddPet)
    Button btAddPet;

    @BindView(R.id.btSave)
    Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Bind views
        ButterKnife.bind(this);
        // Setup add pet button
        setupAddPetButton();
        // Setup save button
        setupSaveButton();
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
                // TODO save UserProfile parse object, which will contain the pets information
            }
        });
    }

    /**
     * Starts the pet profile activity
     */
    private void startPetProfileActivity() {
        Intent petProfileIntent = new Intent(this, PetProfileActivity.class);
        startActivity(petProfileIntent);

        // TODO when the pet profile activity returns, get the pet parse object
        // TODO add the pet parse object to the user object
        // TODO add the pet parse object to the list of pets and display in the recycler view
    }
}
