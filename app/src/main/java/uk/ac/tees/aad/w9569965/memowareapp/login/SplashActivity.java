package uk.ac.tees.aad.w9569965.memowareapp.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import uk.ac.tees.aad.w9569965.memowareapp.R;
import uk.ac.tees.aad.w9569965.memowareapp.login.helper.ActivityHelper;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    // Hide the action bar.
    ActivityHelper.hideActionBar(this);

    /* Initialize handler */
    Handler handler = new Handler();

    /* Start handler for 1 second */
    handler.postDelayed(() -> {
      /* Go to Login Page */
      startActivity(new Intent(this, LoginActivity.class));

      /* Stop this splash activity */
      finish();
    }, 1000);
  }
}
