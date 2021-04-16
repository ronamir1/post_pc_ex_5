package exercise.android.reemh.todo_items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  public TodoItemsHolder holder = null;
  public EditText editTextInsertTask;
  public FloatingActionButton buttonCreateTodoItem;
  public RecyclerView recyclerTodoItemsList;
  public ToDoItemsAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    editTextInsertTask = findViewById(R.id.editTextInsertTask);
    buttonCreateTodoItem = findViewById(R.id.buttonCreateTodoItem);
    recyclerTodoItemsList = findViewById(R.id.recyclerTodoItemsList);
    if (holder == null) {
      holder = new TodoItemsHolderImpl();
    }

    adapter = new ToDoItemsAdapter(holder);
    recyclerTodoItemsList.setAdapter(adapter);
    recyclerTodoItemsList.setLayoutManager(new LinearLayoutManager(this));

    buttonCreateTodoItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String description = editTextInsertTask.getText().toString();
        if (!editTextInsertTask.getText().toString().equals("")) {
          holder.addNewInProgressItem(description);
          adapter.notifyItemInserted(0);
          adapter.notifyDataSetChanged();
          editTextInsertTask.setText("");
        }
      }
    });




    // TODO: implement the specs as defined below
    //    (find all UI components, hook them up, connect everything you need)
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    TodoItemsHolderState state = new TodoItemsHolderState();
    state.holder = holder;
    state.text = editTextInsertTask.getText().toString();
    outState.putSerializable("current state", state);
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    TodoItemsHolderState state = (TodoItemsHolderState) savedInstanceState.getSerializable("current state");
    holder = state.holder;
    editTextInsertTask.setText(state.text);
    adapter = new ToDoItemsAdapter(holder);
    recyclerTodoItemsList.setAdapter(adapter);
    recyclerTodoItemsList.setLayoutManager(new LinearLayoutManager(this));
  }

  private static class TodoItemsHolderState implements Serializable {
    TodoItemsHolder holder;
    String text;
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
      holder.checkboxButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          int pos = holder.getLayoutPosition();
          TodoItem item = todoItemsHolder.getCurrentItems().get(pos);
          if (holder.checkboxButton.isChecked()){
            int new_pos = todoItemsHolder.markItemDone(item);
            notifyItemMoved(pos, new_pos);
          }
          else {
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
    }

    @Override
    public int getItemCount() {
      return todoItemsHolder.getCurrentItems().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
      private final TextView toDoItemDescription;
      private final CheckBox checkboxButton;
      private final ConstraintLayout toDoItemRow;

      public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.toDoItemDescription = itemView.findViewById(R.id.toDoItemDescription);
        this.checkboxButton = itemView.findViewById(R.id.checkboxButton);
        this.toDoItemRow = itemView.findViewById(R.id.toDoItemRow);
      }
    }
  }
}

/*

SPECS:

- the screen starts out empty (no items shown, edit-text input should be empty)
- every time the user taps the "add TODO item" button:
    * if the edit-text is empty (no input), nothing happens
    * if there is input:
        - a new TodoItem (checkbox not checked) will be created and added to the items list
        - the new TodoItem will be shown as the first item in the Recycler view
        - the edit-text input will be erased
- the "TodoItems" list is shown in the screen
  * every operation that creates/edits/deletes a TodoItem should immediately be shown in the UI
  * the order of the TodoItems in the UI is:
    - all DONE items are shown first. items are sorted by creation time,
      where the last-created item is the first item in the list
    - all IN-PROGRESS items are shown afterwards, no particular sort is needed (decide what's the best UX when sorting them)
  * every item shows a checkbox and a description. you can decide to show other data as well (creation time, etc)
  * DONE items should show the checkbox as checked, and the description with a strike-through text
  * IN-PROGRESS items should show the checkbox as not checked, and the description text normal
  * upon click on the checkbox, flip the TodoItem's state (if was DONE will be IN-PROGRESS, and vice versa)
  * add a functionality to remove a TodoItem. either by a button, long-click or any other UX as you want
- when a screen rotation happens (user flips the screen):
  * the UI should still show the same list of TodoItems
  * the edit-text should store the same user-input (don't erase input upon screen change)

Remarks:
- you should use the `holder` field of the activity
- you will need to create a class extending from RecyclerView.Adapter and use it in this activity
- notice that you have the "row_todo_item.xml" file and you can use it in the adapter
- you should add tests to make sure your activity works as expected. take a look at file `MainActivityTest.java`



(optional, for advanced students:
- save the TodoItems list to file, so the list will still be in the same state even when app is killed and re-launched
)

*/