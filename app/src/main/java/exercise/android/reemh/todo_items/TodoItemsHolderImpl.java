package exercise.android.reemh.todo_items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// TODO: implement!
public class TodoItemsHolderImpl implements TodoItemsHolder {
  LinkedList<TodoItem> items = new LinkedList<>();
  @Override
  public List<TodoItem> getCurrentItems() { return new ArrayList<TodoItem>(this.items); }

  @Override
  public void addNewInProgressItem(String description) {
    TodoItem newItem = new TodoItem(description, TodoItem.IN_PROGRESS);
    this.items.addFirst(newItem);
  }

  @Override
  public int markItemDone(TodoItem item) {
    item.status = TodoItem.DONE;
    Collections.sort(this.items);
    return this.items.indexOf(item);
  }

  @Override
  public int markItemInProgress(TodoItem item) {
    item.status = TodoItem.IN_PROGRESS;
    Collections.sort(this.items);
    return this.items.indexOf(item);
  }

  @Override
  public void deleteItem(TodoItem item) {
    this.items.remove(item);
  }
}
