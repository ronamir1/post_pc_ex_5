package exercise.android.reemh.todo_items;

import android.content.Intent;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;



@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class ToDoItemActivityTest {

  private static final String DEFAULT_CALCULATOR_OUTPUT = "~~~ ready to start ~~~";

  private ActivityController<ToDoItemActivity> activityController;
  private ToDoItemActivity activityUnderTest;
  private TodoItemsHolderImpl holder;


  @Before
  public void setup(){

    holder = new TodoItemsHolderImpl();
    holder.addNewInProgressItem("greatest app of my life");
    activityController = Robolectric.buildActivity(ToDoItemActivity.class);
    activityUnderTest = activityController.get();
    activityUnderTest.holder = holder;
    activityController.create().start().resume();
  }

  @Test
  public void check_description_is_as_expected(){
    EditText editText = activityUnderTest.findViewById(R.id.editTextDescription);
    assertEquals(editText.getText().toString(), "greatest app of my life");
  }

  @Test
  public void check_creation_time_is_as_expected(){
    TextView time = activityUnderTest.findViewById(R.id.creationDateTime);
    assertEquals(time.getText().toString(), "0 minutes ago");
  }

  @Test
  public void check_modification_time_is_as_expected(){
    TextView time = activityUnderTest.findViewById(R.id.lastModifiedTime);
    assertEquals(time.getText().toString(), "0 minutes ago");
  }

  @Test
  public void check_status_is_as_expected(){
    RadioButton inProgress = activityUnderTest.findViewById(R.id.radioButtonInProgress);
    assertTrue(inProgress.isChecked());
  }

  @Test
  public void  check_status_is_changed_correctly(){
    RadioButton done = activityUnderTest.findViewById(R.id.radioButtonDone);
    RadioButton inProgress = activityUnderTest.findViewById(R.id.radioButtonInProgress);
    activityUnderTest.onRadioButtonClicked(done);
    assertTrue(done.isChecked());
    assertFalse(inProgress.isChecked());
  }

}