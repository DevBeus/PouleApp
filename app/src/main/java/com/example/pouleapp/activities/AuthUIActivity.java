package com.example.pouleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.example.pouleapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import static com.example.pouleapp.data.GlobalData.ACTIVITY_SELECT_TOURNAMENT;
import static com.example.pouleapp.data.GlobalData.NEXT_ACTIVITY;

public class AuthUIActivity extends AppCompatActivity {

    private FirebaseAuth mFBAuth;
    private FirebaseUser mFBUser;

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_ui);

        mFBAuth = FirebaseAuth.getInstance();
        mFBUser = mFBAuth.getCurrentUser();


        if (mFBUser != null){
            String activity = getIntent().getStringExtra(NEXT_ACTIVITY);

            if (activity.equals(ACTIVITY_SELECT_TOURNAMENT)){
                startActivity(new Intent(this, SelectTournamentActivity.class));
                return;
            }

            startActivity(new Intent(this, FollowTournamentActivity.class));
            return;
        }

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setLogo(R.drawable.welcome)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
//                                new AuthUI.IdpConfig.FacebookBuilder().build()))

//                        .setAvailableProviders(
//                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
//                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))

//                        .setTosUrl(GOOGLE_TOS_URL)
                        .setIsSmartLockEnabled(true, true)
//                        .setAllowNewEmailAccounts(true)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                String activity = getIntent().getStringExtra(NEXT_ACTIVITY);

                if (activity.equals(ACTIVITY_SELECT_TOURNAMENT)){
                    startActivity(new Intent(this, SelectTournamentActivity.class));
                    finish();
                    return;
                }

                startActivity(new Intent(this, FollowTournamentActivity.class));
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showToast(getString(R.string.sign_in_cancelled));
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showToast(getString(R.string.no_internet_connection));
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showToast(getString(R.string.unknown_error));
                    return;
                }
            }

            showToast(getString(R.string.unknown_sign_in_response));
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
