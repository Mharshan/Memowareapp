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
import uk.ac.tees.aad.w9569965.memowareapp.login.activities.memos.DetailMemoActivity;
import uk.ac.tees.aad.w9569965.memowareapp.login.adapters.MemoAdapter;
import uk.ac.tees.aad.w9569965.memowareapp.login.adapters.MemoItem;
import uk.ac.tees.aad.w9569965.memowareapp.login.lists.MemoLists;
import uk.ac.tees.aad.w9569965.memowareapp.login.lists.UserLists;
import uk.ac.tees.aad.w9569965.memowareapp.login.models.MemoModel;

public class MemoListFragment extends Fragment implements MemoAdapter.OnItemListener {
  // Collections.
  private final MemoLists memoLists = new MemoLists();
  private final UserLists userLists = new UserLists();

  // List of task.
  private final ArrayList<MemoItem> memoItems = new ArrayList<>();

  // The logged user.
  private FirebaseUser user;

  // Task adapter.
  private MemoAdapter memoAdapter;

  // View elements.
  private RecyclerView rvTask;


  public MemoListFragment() {
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
    memoAdapter = new MemoAdapter(memoItems, this);

    // Set the adapter to displaying task list.
    rvTask.setAdapter(memoAdapter);
    rvTask.setLayoutManager(new LinearLayoutManager(getContext()));
    rvTask.setItemAnimator(new DefaultItemAnimator());
  }


  @Override
  public void onItemClick(int position) {
    // Go to Detail Task Page with bring task id.
    Intent intent = new Intent(getContext(), DetailMemoActivity.class);
    intent.putExtra("taskId", memoItems.get(position).getId());
    startActivity(intent);
  }


  @SuppressLint("NotifyDataSetChanged")
  private void loadTasks() {
    // If tasks is not empty.
    if (!memoItems.isEmpty()) {
      memoItems.clear();
      memoAdapter.notifyDataSetChanged();
    }

    // Retrieve own task data.
    memoLists.findAll(user.getUid()).addOnSuccessListener(queryDocumentSnapshots -> {
      for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
        MemoModel task = new MemoModel(document);
        MemoItem memoItem = new MemoItem(task);

        // Add the task to list, then refresh the adapter on data changed.
        if (memoItems.add(memoItem))
          memoAdapter.notifyItemInserted(memoItems.indexOf(memoItem));
      }
    });

  }
}
