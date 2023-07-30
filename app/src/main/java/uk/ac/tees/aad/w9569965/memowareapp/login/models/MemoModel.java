package uk.ac.tees.aad.w9569965.memowareapp.login.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class MemoModel {
  public static final String DEADLINE_FIELD = "deadline";
  public static final String DESCRIPTION_FIELD = "description";
  public static final String TITLE_FIELD = "title";
  public static final String OWNER_ID_FIELD = "ownerId";

  private String id;
  private String title;
  private String description;
  private Timestamp deadline;
  private String ownerId;


  public MemoModel() {}


  public MemoModel(
      String id, String title, String description, Timestamp deadline, String ownerId
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.deadline = deadline;
    this.ownerId = ownerId;
  }


  public MemoModel(DocumentSnapshot memo) {
    this.id = memo.getId();
    this.title = memo.getString(MemoModel.TITLE_FIELD);
    this.description = memo.getString(MemoModel.DESCRIPTION_FIELD);
    this.deadline = memo.getTimestamp(MemoModel.DEADLINE_FIELD);
    this.ownerId = memo.getString(MemoModel.OWNER_ID_FIELD);
  }


  public String getId() {
    return id;
  }


  public void setId(String id) {
    this.id = id;
  }


  public Timestamp getDeadline() {
    return deadline;
  }


  public void setDeadline(Timestamp deadline) {
    this.deadline = deadline;
  }


  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }


  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public String getOwnerId() {
    return ownerId;
  }


  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }
}
