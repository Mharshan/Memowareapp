package uk.ac.tees.aad.w9569965.memowareapp.login.lists;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.aad.w9569965.memowareapp.login.models.MemoModel;

public class MemoLists {
  public static final String COLLECTION_PATH = "Tasks";
  private final CollectionReference collection = FirebaseFirestore.getInstance()
      .collection(COLLECTION_PATH);


  public MemoLists() {}


  public Task<DocumentSnapshot> findOne(String taskId) {
    return collection.document(taskId).get();
  }


  public Task<QuerySnapshot> findAll(String ownerId) {
    return collection.whereEqualTo(MemoModel.OWNER_ID_FIELD, ownerId).get();
  }


  public Task<QuerySnapshot> findAll(String ownerId, String orderByField, boolean isAscending) {
    return collection.whereEqualTo(MemoModel.OWNER_ID_FIELD, ownerId)
        .orderBy(orderByField, isAscending ? Query.Direction.ASCENDING : Query.Direction.DESCENDING)
        .get();
  }


  public Task<DocumentReference> insert(
      String title, String description, Timestamp deadline, String ownerId
  ) {
    Map<String, Object> data = new HashMap<>();
    data.put(MemoModel.TITLE_FIELD, title);
    data.put(MemoModel.DESCRIPTION_FIELD, description);
    data.put(MemoModel.DEADLINE_FIELD, deadline);
    data.put(MemoModel.OWNER_ID_FIELD, ownerId);

    return collection.add(data);
  }


  public Task<Void> update(MemoModel task) {
    return collection.document(task.getId())
        .update(MemoModel.TITLE_FIELD, task.getTitle(), MemoModel.DESCRIPTION_FIELD,
            task.getDescription(), MemoModel.DEADLINE_FIELD, task.getDeadline());
  }


  public Task<Void> delete(String taskId) {
    return collection.document(taskId).delete();
  }
}
