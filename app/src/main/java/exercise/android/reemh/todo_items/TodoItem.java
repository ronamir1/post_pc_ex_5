package exercise.android.reemh.todo_items;

import java.io.Serializable;


public class TodoItem implements Serializable, Comparable<TodoItem> {
    String text;
    int status;
    int id;
    final long creationTime;
    long modificationTime = 0;
    final static int IN_PROGRESS = 1;
    final static int DONE = 2;
    static int items_created = 0;

    TodoItem(String text,int status, long creationTime){
        this.text = text;
        this.status = status;
        this.id = items_created;
        this.creationTime = creationTime;
        items_created += 1;
    }

    @Override
    public int compareTo(TodoItem todoItem) {
        if (this.status > todoItem.status){
            return 1;
        }
        else if(this.status < todoItem.status){
            return -1;
        }

        else if (this.creationTime < todoItem.creationTime){
            return 1;
        }

        return -1;
    }
    // TODO: edit this class as you want
}


