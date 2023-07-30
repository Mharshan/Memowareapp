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

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoListViewHolder> {
  /**
   * List of task.
   */
  private final ArrayList<MemoItem> memoItems;

  /**
   * Closure to handle actions of task item.
   */
  private final OnItemListener onItemListener;


  public MemoAdapter(ArrayList<MemoItem> memoItems, OnItemListener onItemListener) {
    this.memoItems = memoItems;
    this.onItemListener = onItemListener;
  }


  @NonNull
  @Override
  public MemoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_task, parent, false);
    return new MemoListViewHolder(itemView, onItemListener);
  }


  @Override
  public void onBindViewHolder(@NonNull MemoListViewHolder holder, int position) {
    MemoItem memoItem = memoItems.get(position);

    // Set display of memo items.
    holder.tvTitle.setText(memoItem.getTitle());
    holder.tvDeadline.setText(InputHelper.dateToString(memoItem.getDeadline()));

    // Display the name of memo owner if not null.
    if (memoItem.getOwnerName() != null) {
      holder.tvOwner.setText(memoItem.getOwnerName());
      holder.ownerField.setVisibility(View.VISIBLE);
    }
  }


  @Override
  public int getItemCount() {
    return memoItems.size();
  }


  public interface OnItemListener {
    /**
     * Set actions when item is clicked.
     *
     * @param position The position of item.
     */
    void onItemClick(int position);
  }


  static class MemoListViewHolder extends RecyclerView.ViewHolder {
    // View elements.
    protected final TextView tvTitle;
    protected final TextView tvDeadline;
    protected final TextView tvOwner;
    protected final ConstraintLayout ownerField;

    /**
     * Closure to handle actions of task item.
     */
    final OnItemListener onItemListener;


    public MemoListViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
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
