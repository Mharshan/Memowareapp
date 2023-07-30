package uk.ac.tees.aad.w9569965.memowareapp.login.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.tees.aad.w9569965.memowareapp.R;
import uk.ac.tees.aad.w9569965.memowareapp.login.helper.InputHelper;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskListViewHolder> {
  /**
   * List of task.
   */
  private final ArrayList<TaskItem> taskItems;

  /**
   * Closure to handle actions of task item.
   */
  private final OnItemListener onItemListener;


  public TaskAdapter(ArrayList<TaskItem> taskItems, OnItemListener onItemListener) {
    this.taskItems = taskItems;
    this.onItemListener = onItemListener;
  }


  @NonNull
  @Override
  public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_task, parent, false);
    return new TaskListViewHolder(itemView, onItemListener);
  }


  @Override
  public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
    TaskItem taskItem = taskItems.get(position);

    // Set display of task items.
    holder.tvTitle.setText(taskItem.getTitle());
    holder.tvDeadline.setText(InputHelper.dateToString(taskItem.getDeadline()));

    // Display the name of task owner if not null.
    if (taskItem.getOwnerName() != null) {
      holder.tvOwner.setText(taskItem.getOwnerName());
      holder.ownerField.setVisibility(View.VISIBLE);
    }
  }


  @Override
  public int getItemCount() {
    return taskItems.size();
  }


  public interface OnItemListener {
    /**
     * Set actions when item is clicked.
     *
     * @param position The position of item.
     */
    void onItemClick(int position);
  }


  static class TaskListViewHolder extends RecyclerView.ViewHolder {
    // View elements.
    protected final TextView tvTitle;
    protected final TextView tvDeadline;
    protected final TextView tvOwner;
    protected final ConstraintLayout ownerField;

    /**
     * Closure to handle actions of task item.
     */
    final OnItemListener onItemListener;


    public TaskListViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
      super(itemView);

      // Initialize view elements.
      tvTitle = itemView.findViewById(R.id.tvTitle);
      tvDeadline = itemView.findViewById(R.id.tvDeadline);
      tvOwner = itemView.findViewById(R.id.tvOwner);
      ownerField = itemView.findViewById(R.id.ownerField);

      // Initialize the itemListener closure.
      this.onItemListener = onItemListener;

      // Set actions when item is clicked
      itemView.setOnClickListener(view -> onItemListener.onItemClick(getAdapterPosition()));
    }
  }
}
