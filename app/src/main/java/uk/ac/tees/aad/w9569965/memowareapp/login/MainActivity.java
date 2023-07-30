package uk.ac.tees.aad.w9569965.memowareapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import uk.ac.tees.aad.w9569965.memowareapp.R;
import uk.ac.tees.aad.w9569965.memowareapp.login.activities.memos.AddMemoActivity;
import uk.ac.tees.aad.w9569965.memowareapp.login.fragments.EmptyMemoListFragment;
import uk.ac.tees.aad.w9569965.memowareapp.login.lists.MemoLists;
import uk.ac.tees.aad.w9569965.memowareapp.login.fragments.MemoListFragment;
import uk.ac.tees.aad.w9569965.memowareapp.login.weather.WeatherActivity;
import uk.ac.tees.aad.w9569965.memowareapp.login.maps.MapsActivity;

public class MainActivity extends AppCompatActivity {

  public static final String TASK_ID_KEY = "taskId";

  /* Authentication. */
  private final FirebaseAuth auth = FirebaseAuth.getInstance();
  private final FirebaseUser user = auth.getCurrentUser();

  // Collections.
  private final MemoLists memoLists = new MemoLists();

  // The fragment manager.
  private FragmentManager fragmentManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      // Set fragment for first time.
      fragmentManager = getSupportFragmentManager();
    }
  }


  @Override
  protected void onStart() {
    super.onStart();

    // Set the fragment.
    setFragment();

    /* Go to Update Profile Page if display name is unset, */
    if (Objects.equals(user.getDisplayName(), "")) {
      startActivity(new Intent(this, UpdateProfileActivity.class));
      finish();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    if (item.getItemId() == R.id.addTaskItem) {
      startActivity(new Intent(this, AddMemoActivity.class));
    }
    if (item.getItemId() == R.id.weather) {
      startActivity(new Intent(this, WeatherActivity.class));
    }
    if (item.getItemId() == R.id.maps) {
      startActivity(new Intent(this, MapsActivity.class));
    }
    /* If Logout Item is selected. */
    if (item.getItemId() == R.id.logoutItem) {
      /* Show confirmation dialog. */
      new AlertDialog.Builder(this).setTitle("Logout")
          .setMessage("Are you sure to logout?")
          /* Cancel action. */
          .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
          /* Sign out then redirect to login activity. */
          .setPositiveButton("Logout", (dialogInterface, i) -> {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
          })
          .show();
    }
    return super.onOptionsItemSelected(item);
  }


  /**
   * Set fragment to the fragment container view. Will set {@code EmptyMemoListFragment} if there
   * are no memos. Otherwise, {@code MemoListFragment} will be set.
   */
  private void setFragment() {
    memoLists.findAll(user.getUid())
        .addOnSuccessListener(tasksSnapshot -> memoLists.findOne(user.getUid())
            .addOnSuccessListener(sharedTasksSnapshot -> {
              if (tasksSnapshot.isEmpty()) {
                fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, EmptyMemoListFragment.class, null)
                    .commit();
              } else {
                fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, MemoListFragment.class, null)
                    .commit();
              }
            }));
  }
}
