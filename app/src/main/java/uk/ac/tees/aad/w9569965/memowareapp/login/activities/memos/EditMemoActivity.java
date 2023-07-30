package uk.ac.tees.aad.w9569965.memowareapp.login.activities.memos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.util.Calendar;

import uk.ac.tees.aad.w9569965.memowareapp.R;
import uk.ac.tees.aad.w9569965.memowareapp.login.MainActivity;
import uk.ac.tees.aad.w9569965.memowareapp.login.lists.MemoLists;
import uk.ac.tees.aad.w9569965.memowareapp.login.helper.DateTimePickerDialog;
import uk.ac.tees.aad.w9569965.memowareapp.login.helper.InputHelper;
import uk.ac.tees.aad.w9569965.memowareapp.login.models.MemoModel;

public class EditMemoActivity extends AppCompatActivity {
  private final Calendar calendar = Calendar.getInstance();
  private final MemoLists memoLists = new MemoLists();
  private MemoModel memoModel;

  /* View elements. */
  private Button btnEditTask;
  private EditText inputTitle, inputDescription, inputDeadline;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_task);

    /* Initialize view elements. */
    btnEditTask = findViewById(R.id.btnEditTask);
    inputTitle = findViewById(R.id.inputTitle);
    inputDescription = findViewById(R.id.inputDescription);
    inputDeadline = findViewById(R.id.inputDeadline);


    /* When Deadline Input is clicked. */
    inputDeadline.setOnClickListener(view -> {
      /* Initialize date time picker. */
      DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog(this, calendar,
          (year, month, dayOfMonth, hourOfDay, minute) -> {
            /* Set calendar with deadline input. */
            calendar.set(year, month, dayOfMonth, hourOfDay, minute);
            /* Format the date, then put to input field. */
            inputDeadline.setText(InputHelper.dateToInput(calendar.getTime()));
          });

      /* Show date time picker. */
      dateTimePickerDialog.show();
    });


    /* When Edit Button is clicked. */
    btnEditTask.setOnClickListener(view -> {
      /* Edit the task. */
      try {
        String title = InputHelper.getRequiredInput(inputTitle);
        String description = InputHelper.getRequiredInput(inputDescription);
        String strDeadline = InputHelper.getRequiredInput(inputDeadline);
        Timestamp deadline = new Timestamp(InputHelper.inputToDate(strDeadline));

        memoModel.setTitle(title);
        memoModel.setDescription(description);
        memoModel.setDeadline(deadline);

        /* Edit new task. */
        memoLists.update(memoModel).addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            /* If success, finish this activity. */
            Toast.makeText(this, "Memo successfully edited.", Toast.LENGTH_SHORT).show();
            finish();
          } else {
            Toast.makeText(this, "Failed to edit Memo.", Toast.LENGTH_SHORT).show();
            Log.e("editMemo", "Failed to edit Memo.", task.getException());
          }
        });
      } catch (NullPointerException e) {
        Log.i("inputValidation", e.getMessage(), e);
      } catch (ParseException e) {
        Log.e("inputValidation", "Failed to parse deadline.", e);
        inputDeadline.setError("Invalid deadline format");
      }
    });
  }


  @Override
  protected void onStart() {
    super.onStart();

    /* Get the task. */
    memoLists.findOne(getIntent().getStringExtra(MainActivity.TASK_ID_KEY))
        .addOnSuccessListener(documentSnapshot -> {
          /* Initialize task. */
          memoModel = new MemoModel(documentSnapshot);

          /* Set calendar to deadline value. */
          calendar.setTime(memoModel.getDeadline().toDate());

          /* Display the task data. */
          inputTitle.setText(memoModel.getTitle());
          inputDescription.setText(memoModel.getDescription());
          inputDeadline.setText(InputHelper.dateToInput(memoModel.getDeadline().toDate()));
        });
  }


  @Override
  public boolean navigateUpTo(Intent upIntent) {
    upIntent.putExtra("taskId", memoModel.getId());
    return super.navigateUpTo(upIntent);
  }
}
