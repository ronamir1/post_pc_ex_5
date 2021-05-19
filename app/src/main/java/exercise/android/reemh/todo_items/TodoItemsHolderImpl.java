package exercise.android.reemh.todo_items;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

// TODO: implement!
public class TodoItemsHolderImpl extends Activity implements TodoItemsHolder {
  LinkedList<TodoItem> items;
  SharedPreferences sp;

  TodoItemsHolderImpl(){
    items = new LinkedList<>();
  }

  @Override
  public List<TodoItem> getCurrentItems() { return new ArrayList<>(this.items); }

  public void setSp(SharedPreferences sp){
    this.sp = sp;
  }

  public void recoverItems(){
    if (sp == null){
      items = new LinkedList<>();
      return;
    }
    String stateString = this.sp.getString("holder", "");
    if (!stateString.equals("") && !stateString.equals("{\"items\":[]}")){
      Gson gson = new Gson();
      TodoItem[] recoveredItems = gson.fromJson(stateString, TodoItem[].class);
      this.items = new LinkedList<>(Arrays.asList(recoveredItems));
    }
    else {
      items = new LinkedList<>();
    }
    saveItems();
  }

  public void saveItems(){
    SharedPreferences.Editor editor = sp.edit();
    Gson gson = new Gson();
    String stateString = gson.toJson(getCurrentItems().toArray());
    editor.putString("holder", stateString);
    editor.apply();
  }

  @Override
  public void addNewInProgressItem(String description) {
    Date current_time = Calendar.getInstance().getTime();
    TodoItem newItem = new TodoItem(description, TodoItem.IN_PROGRESS, current_time.getTime());
    newItem.modificationTime = newItem.creationTime;
    this.items.addFirst(newItem);
    if (sp != null){
      saveItems();
    }

  }

  @Override
  public int markItemDone(TodoItem item) {
    item.status = TodoItem.DONE;
    Date current_time = Calendar.getInstance().getTime();
    item.modificationTime = current_time.getTime();
    Collections.sort(this.items);
    if (sp != null){
      saveItems();
    }
    return this.items.indexOf(item);
  }

  @Override
  public int markItemInProgress(TodoItem item) {
    item.status = TodoItem.IN_PROGRESS;
    Date current_time = Calendar.getInstance().getTime();
    item.modificationTime = current_time.getTime();
    Collections.sort(this.items);
    if (sp != null){
      saveItems();
    }
    return this.items.indexOf(item);
  }

  @Override
  public void deleteItem(TodoItem item)
  {
    this.items.remove(item);
    if (sp != null){
      saveItems();
    }
  }

  public static class ToDoItemsAdapter extends RecyclerView.Adapter<ToDoItemsAdapter.ViewHolder> {

    private final TodoItemsHolder todoItemsHolder;


    ToDoItemsAdapter(TodoItemsHolder data) {
      this.todoItemsHolder = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).
              inflate(R.layout.row_todo_item, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoItemsAdapter.ViewHolder holder, int position) {
      holder.toDoItemRow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(holder.context, ToDoItemActivity.class);
          int pos = holder.getLayoutPosition();
          TodoItem item = todoItemsHolder.getCurrentItems().get(pos);
          intent.putExtra("position", pos);
          intent.putExtra("creation time", item.creationTime);
          intent.putExtra("modification time", item.modificationTime);
          intent.putExtra("status", item.status);
          intent.putExtra("description", item.text);
          holder.context.startActivity(intent);
        }
      });
      holder.checkboxButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          int pos = holder.getLayoutPosition();
          TodoItem item = todoItemsHolder.getCurrentItems().get(pos);
          if (holder.checkboxButton.isChecked()){
            holder.toDoItemDescription.setPaintFlags(holder.toDoItemDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            int new_pos = todoItemsHolder.markItemDone(item);
            notifyItemMoved(pos, new_pos);
          }
          else {
            holder.toDoItemDescription.setPaintFlags(0);
            int new_pos =todoItemsHolder.markItemInProgress(item);
            notifyItemMoved(pos, new_pos);
          }
        }
      });
      holder.toDoItemRow.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
          int pos = holder.getLayoutPosition();
          TodoItem item = todoItemsHolder.getCurrentItems().get(pos);
          todoItemsHolder.deleteItem(item);
          notifyItemRangeRemoved(pos, 1);
          return true;
        }
      });
      holder.toDoItemDescription.setText(todoItemsHolder.getCurrentItems().get(position).text);
      int status = todoItemsHolder.getCurrentItems().get(position).status;
      holder.checkboxButton.setChecked(status == TodoItem.DONE);
      if (status == TodoItem.DONE){
        holder.toDoItemDescription.setPaintFlags(holder.toDoItemDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      }
    }

    @Override
    public int getItemCount() {
      return todoItemsHolder.getCurrentItems().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
      private final TextView toDoItemDescription;
      private final CheckBox checkboxButton;
      private final ConstraintLayout toDoItemRow;
      private final Context context;

      public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.toDoItemDescription = itemView.findViewById(R.id.toDoItemDescription);
        this.checkboxButton = itemView.findViewById(R.id.checkboxButton);
        this.toDoItemRow = itemView.findViewById(R.id.toDoItemRow);
        this.context = itemView.getContext();
      }
    }
  }
}


