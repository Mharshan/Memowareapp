package uk.ac.tees.aad.w9569965.memowareapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import uk.ac.tees.aad.w9569965.memowareapp.R;
import uk.ac.tees.aad.w9569965.memowareapp.login.lists.UserLists;
import uk.ac.tees.aad.w9569965.memowareapp.login.helper.InputHelper;

public class UpdateProfileActivity extends AppCompatActivity {
  /* Authentication. */
  private final FirebaseAuth auth = FirebaseAuth.getInstance();
  private final FirebaseUser user = auth.getCurrentUser();

  /* Collections */ UserLists userLists = new UserLists();

  /* View elements. */
  private Button btnUpdate;
  private EditText inputName;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_profile);

    /* Initialize view elements. */
    btnUpdate = findViewById(R.id.btnUpdate);
    inputName = findViewById(R.id.inputName);

    /* Customize action bar. */
    try {
      customizeActionBar();
    } catch (NullPointerException e) {
      Log.i("actionBar", e.getMessage(), e);
    }


    /* When Next Button is clicked. */
    btnUpdate.setOnClickListener(view -> {
      try {
        String name = InputHelper.getRequiredInput(inputName);

        UserProfileChangeRequest profileUpdates
            = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

        /* Update the user profiles. */
        user.updateProfile(profileUpdates).addOnCompleteListener(memo -> {
          if (memo.isSuccessful()) {
            /* Save to user collection. */
            userLists.save(user.getUid(), user.getEmail(), user.getDisplayName())
                .addOnCompleteListener(memo1 -> {
                  if (memo1.isSuccessful()) {
                    /* Go to main page. */
                    startActivity(new Intent(this, MainActivity.class));
                    finish();    // Finish this activity.
                  } else {
                    Toast.makeText(this, "Failed adding profile to collection.", Toast.LENGTH_SHORT)
                        .show();
                    Log.i("updateProfile", "Failed adding profile to collection.",
                        memo.getException());
                  }
                });
          } else {
            Toast.makeText(this, "Failed updating profile.", Toast.LENGTH_SHORT).show();
            Log.i("updateProfile", "Failed updating profile.", memo.getException());
          }
        });
      } catch (NullPointerException e) {
        Log.i("inputName", e.getMessage(), e);
      }
    });
  }


  /**
   * Customize the action bar for this activity here.
   */
  private void customizeActionBar() {
    ActionBar actionBar = getSupportActionBar();

    if (actionBar == null)
      throw new NullPointerException("There are no action bar in this activity.");

    /* Set title. */
    actionBar.setTitle("Update Profile");
  }
}
