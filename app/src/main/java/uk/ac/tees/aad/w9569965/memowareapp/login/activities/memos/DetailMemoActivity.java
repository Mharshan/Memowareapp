package uk.ac.tees.aad.w9569965.memowareapp.login.activities.memos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import uk.ac.tees.aad.w9569965.memowareapp.R;
import uk.ac.tees.aad.w9569965.memowareapp.login.lists.MemoLists;
import uk.ac.tees.aad.w9569965.memowareapp.login.helper.InputHelper;
import uk.ac.tees.aad.w9569965.memowareapp.login.models.MemoModel;

public class DetailMemoActivity extends AppCompatActivity {
  // Authentication.
  private final FirebaseAuth auth = FirebaseAuth.getInstance();
  private final FirebaseUser user = auth.getCurrentUser();

  // Collections.
  private final MemoLists memoLists = new MemoLists();

  private String taskId;

  private MemoModel task;

  // View elements.
  private TextView tvTitle, tvDescription, tvDeadline, tvCountSharedTask;
  private Menu menu;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_task);

    // Initialize view elements.
    tvTitle = findViewById(R.id.tvTitle);
    tvDescription = findViewById(R.id.tvDescription);
    tvDeadline = findViewById(R.id.tvDeadline);

    // Get task id.
    taskId = getIntent().getStringExtra("taskId");


  }


  @Override
  protected void onStart() {
    loadTask();
    super.onStart();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.detail_task_menu, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    // If Edit Item is selected.
    if (item.getItemId() == R.id.editItem)
      onEditItemSelected();

    // If Delete Item is selected.
    if (item.getItemId() == R.id.deleteItem)
      onDeleteItemSelected();

    return super.onOptionsItemSelected(item);
  }



  private void onEditItemSelected() {
    // Go to Edit Task Activity.
    Intent intent = new Intent(this, EditMemoActivity.class);
    intent.putExtra("taskId", taskId);
    startActivity(intent);
  }


  private void onDeleteItemSelected() {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

    alertDialog.setTitle("Delete Memo")
        .setMessage("Are you sure to delete this memo?")
        .setNegativeButton("Cancel", null)
        .setPositiveButton("Delete Memo", (dialogInterface, i) -> {
          // Delete task.
          memoLists.delete(taskId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              deleteRelatedSharedTasks(taskId);    // Get all related shared tasks.
              finish();    // Finish this activity.
              Toast.makeText(this, "One memo has been deleted.", Toast.LENGTH_SHORT).show();
            }
          }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to delete memo.", Toast.LENGTH_SHORT).show();
            Log.e("deleteMemo", e.getMessage(), e);
          });
        });

    // Show confirmation dialog.
    alertDialog.show();
  }


  private void loadTask() {
    memoLists.findOne(taskId).addOnSuccessListener(documentSnapshot -> {
      // Set task.
      task = new MemoModel(documentSnapshot);

      // Set access.
      setTaskAccess();

      // Display the task data.
      tvTitle.setText(task.getTitle());
      tvDescription.setText(task.getDescription());
      tvDeadline.setText(InputHelper.dateToString(task.getDeadline().toDate(),
          InputHelper.DATE_FORMAT_HUMAN_LONG_US, Locale.US));

    }).addOnFailureListener(e -> {
      Log.e("loadTask", e.getMessage(), e);
      Toast.makeText(this, "Failed to get task. Try again later", Toast.LENGTH_SHORT).show();
      finish();    // Finish the activity.
    });
  }


  private void setTaskAccess() {
    MenuItem shareItem = menu.findItem(R.id.shareItem);
    MenuItem editItem = menu.findItem(R.id.editItem);
    MenuItem deleteItem = menu.findItem(R.id.deleteItem);

    if (task.getOwnerId().equals(user.getUid())) {
      // Set access for task owner.
      shareItem.setVisible(true);     // Enable sharing access.
      editItem.setVisible(true);      // Enable edit access.
      deleteItem.setVisible(true);    // Enable delete access.
       // Enable access to Shared Task List Page.
    } else {
      // Set modifier access for shared task.

    }
  }
  private void deleteRelatedSharedTasks(String taskId) {

  }
}
