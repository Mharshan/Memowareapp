package uk.ac.tees.aad.w9569965.memowareapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.aad.w9569965.memowareapp.R;
import uk.ac.tees.aad.w9569965.memowareapp.login.helper.ActivityHelper;
import uk.ac.tees.aad.w9569965.memowareapp.login.helper.InputHelper;


public class RegisterActivity extends AppCompatActivity {
  /* Authentication. */
  private final FirebaseAuth auth = FirebaseAuth.getInstance();

  /* View elements. */
  private Button btnLogin, btnRegister;
  private TextInputEditText inputEmail, inputPassword;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    /* Initialize view elements. */
    btnLogin = findViewById(R.id.btnLogin);
    btnRegister = findViewById(R.id.btnRegister);
    inputEmail = findViewById(R.id.inputEmail);
    inputPassword = findViewById(R.id.inputPassword);


    // Hide the action bar.
    ActivityHelper.hideActionBar(this);


    /* When Login Button is clicked. */
    btnLogin.setOnClickListener(view -> {
      /* Go to login page. */
      startActivity(new Intent(this, LoginActivity.class));
      finish();
    });


    /* When Register Button is clicked. */
    btnRegister.setOnClickListener(view -> {
      try {
        String email = InputHelper.getRequiredInput(inputEmail);
        String password = InputHelper.getRequiredInput(inputPassword);



        /* Create new account. */
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
          /* If success, send email verification. */
          try {
            FirebaseUser user = authResult.getUser();

            if (user == null)
              throw new FirebaseAuthInvalidUserException("404", "User not found.");

            sendEmailVerification(user);
          } catch (FirebaseAuthInvalidUserException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
          }
        }).addOnFailureListener(e -> {
          /* If failed, show the error message. */
          try {
            throw e;
          } catch (FirebaseAuthUserCollisionException exception) {
            inputEmail.setError("This email is already registered. Please use another email.");
            Log.e("register", exception.getMessage(), exception);
          } catch (FirebaseAuthWeakPasswordException exception) {
            inputPassword.setError("Password should be at least 6 characters.");
            Log.e("register", exception.getMessage(), exception);
          } catch (Exception exception) {
            Log.e("register", exception.getMessage(), exception);
          }
        });
      } catch (Exception e) {
        /* If email is invalid. */
        Log.i("inputValidation", e.getMessage(), e);
        inputEmail.setError(e.getMessage());
      }
    });
  }


  private void sendEmailVerification(FirebaseUser user) {
    user.sendEmailVerification().addOnCompleteListener(memo -> {
      if (memo.isSuccessful()) {
        /* Go to Email Verification Page. */
        startActivity(new Intent(this, VerifyEmailActivity.class));
        finish();
      } else {
        Log.e("verifyEmail", "Email verification failed", memo.getException());
        Toast.makeText(this, "Failed to send email verification", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
