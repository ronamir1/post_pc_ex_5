package exercise.android.reemh.todo_items;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ToDoItemActivity extends AppCompatActivity {
    EditText description;
    RadioButton radioButtonDone ;
    RadioButton radioButtonInProgress;
    TextView creationTimeTextView;
    TextView lastModifiedTextView;
    int pos;
    SharedPreferences sp;
    TodoItemsHolderImpl holder;
    TodoItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item);
        description = findViewById(R.id.editTextDescription);
        radioButtonDone = findViewById(R.id.radioButtonDone);
        radioButtonInProgress = findViewById(R.id.radioButtonInProgress);
        creationTimeTextView = findViewById(R.id.creationDateTime);
        lastModifiedTextView = findViewById(R.id.lastModifiedTime);
        Context context = ToDoItemActivity.this;
        sp = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if (holder == null){
            holder = new TodoItemsHolderImpl();
            holder.setSp(sp);
            holder.recoverItems();
        }


        Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);
        item = holder.getCurrentItems().get(pos);

        description.setText(item.text);
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (item.text.equals(editable.toString())){
                    return;
                }
                Calendar currentTime = Calendar.getInstance();
                item.modificationTime = currentTime.getTime().getTime();
                item.text = editable.toString();
                holder.saveItems();
                lastModifiedTextView.setText(get_time_message(item.modificationTime));
                lastModifiedTextView.invalidate();
            }
        });

        creationTimeTextView.setText(get_time_message(item.creationTime));
        lastModifiedTextView.setText(get_time_message(item.modificationTime));

        if (item.status == TodoItem.IN_PROGRESS){
            radioButtonInProgress.toggle();
        }
        else {
            radioButtonDone.toggle();
        }
    }

    private String get_time_message(long time){
        Calendar currentTime = Calendar.getInstance();
        Calendar originalTime = Calendar.getInstance();
        originalTime.setTime(new Date(time));

        if (currentTime.get(Calendar.YEAR) == originalTime.get(Calendar.YEAR)){
            if (currentTime.get(Calendar.DAY_OF_YEAR) == originalTime.get(Calendar.DAY_OF_YEAR)){
                if (currentTime.get(Calendar.HOUR_OF_DAY) == originalTime.get(Calendar.HOUR_OF_DAY)){
                    int minutes = currentTime.get(Calendar.MINUTE) - originalTime.get(Calendar.MINUTE);
                    return String.valueOf(minutes) + " minutes ago";
                }
                else if (currentTime.get(Calendar.HOUR_OF_DAY) - originalTime.get(Calendar.HOUR_OF_DAY) == 1){
                    int original_minutes =  originalTime.get(Calendar.MINUTE);
                    int current_minutes = currentTime.get(Calendar.MINUTE);
                    int minutes = original_minutes - current_minutes;
                    if (minutes > 0){
                        return String.valueOf(minutes) + " minutes ago";
                    }
                }
                int hour = originalTime.get(Calendar.HOUR_OF_DAY);
                return "Today at " + String.valueOf(hour);
            }
        }
        String month = String.valueOf(originalTime.get(Calendar.MONTH));
        String year = String.valueOf(originalTime.get(Calendar.YEAR));
        String day = String.valueOf(originalTime.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(originalTime.get(Calendar.HOUR));
        return day + "/" + month + "/" + year + " at " + hour;
    }

    public void onRadioButtonClicked(View v){
        Calendar currentTime = Calendar.getInstance();
        item.modificationTime = currentTime.getTime().getTime();
        lastModifiedTextView.setText(get_time_message(item.modificationTime));

        if (findViewById(R.id.radioButtonInProgress).getId() == v.getId()){
            radioButtonInProgress.toggle();
            holder.markItemInProgress(item);
        }
        else {
            radioButtonDone.toggle();
            holder.markItemDone(item);
        }

        lastModifiedTextView.invalidate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        holder.saveItems();
        startActivity(intent);
    }

    public void hideKeyBoard(View view) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
        description.setCursorVisible(false);
    }
}