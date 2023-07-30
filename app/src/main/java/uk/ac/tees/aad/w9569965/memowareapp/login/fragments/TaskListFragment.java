package uk.ac.tees.aad.w9569965.memowareapp.login.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import uk.ac.tees.aad.w9569965.memowareapp.R;
import uk.ac.tees.aad.w9569965.memowareapp.login.activities.tasks.DetailTaskActivity;
import uk.ac.tees.aad.w9569965.memowareapp.login.adapters.TaskAdapter;
import uk.ac.tees.aad.w9569965.memowareapp.login.adapters.TaskItem;
import uk.ac.tees.aad.w9569965.memowareapp.login.lists.TaskCollection;
import uk.ac.tees.aad.w9569965.memowareapp.login.lists.UserCollection;
import uk.ac.tees.aad.w9569965.memowareapp.login.models.TaskModel;
import uk.ac.tees.aad.w9569965.memowareapp.login.models.UserModel;

public class TaskListFragment extends Fragment implements TaskAdapter.OnItemListener {
  // Collections.
  private final TaskCollection taskCollection = new TaskCollection();
  private final UserCollection userCollection = new UserCollection();

  // List of task.
  private final ArrayList<TaskItem> taskItems = new ArrayList<>();

  // The logged user.
  private FirebaseUser user;

  // Task adapter.
  private TaskAdapter taskAdapter;

  // View elements.
  private RecyclerView rvTask;


  public TaskListFragment() {
    // Required empty public constructor
    super(R.layout.fragment_task_list);
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get logged user.
    user = FirebaseAuth.getInstance().getCurrentUser();
  }


  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
  ) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_task_list, container, false);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // Initialize view elements.
    rvTask = view.findViewById(R.id.rvTask);

    // Initialize task adapter.
    initTaskAdapter();

    // Call super method.
    super.onViewCreated(view, savedInstanceState);
  }


  @Override
  public void onStart() {
    loadTasks();    // Load the tasks.
    super.onStart();    // Call super method.
  }


  /**
   * Initialize task adapter & recycler view.
   */
  private void initTaskAdapter() {
    // Initialize task adapter.
    taskAdapter = new TaskAdapter(taskItems, this);

    // Set the adapter to displaying task list.
    rvTask.setAdapter(taskAdapter);
    rvTask.setLayoutManager(new LinearLayoutManager(getContext()));
    rvTask.setItemAnimator(new DefaultItemAnimator());
  }


  @Override
  public void onItemClick(int position) {
    // Go to Detail Task Page with bring task id.
    Intent intent = new Intent(getContext(), DetailTaskActivity.class);
    intent.putExtra("taskId", taskItems.get(position).getId());
    startActivity(intent);
  }


  @SuppressLint("NotifyDataSetChanged")
  private void loadTasks() {
    // If tasks is not empty.
    if (!taskItems.isEmpty()) {
      taskItems.clear();
      taskAdapter.notifyDataSetChanged();
    }

    // Retrieve own task data.
    taskCollection.findAll(user.getUid()).addOnSuccessListener(queryDocumentSnapshots -> {
      for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
        TaskModel task = new TaskModel(document);
        TaskItem taskItem = new TaskItem(task);

        // Add the task to list, then refresh the adapter on data changed.
        if (taskItems.add(taskItem))
          taskAdapter.notifyItemInserted(taskItems.indexOf(taskItem));
      }
    });

  }
}
