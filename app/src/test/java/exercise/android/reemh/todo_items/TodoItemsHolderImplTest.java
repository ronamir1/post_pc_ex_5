package exercise.android.reemh.todo_items;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class TodoItemsHolderImplTest {
  @Test
  public void when_addingTodoItem_then_callingListShouldHaveThisItem(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());
  }
  @Test
  public void when_two_items_then_the_last_is_first(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    holderUnderTest.addNewInProgressItem("did shopping");

    // verify
    Assert.assertEquals("did shopping", holderUnderTest.getCurrentItems().get(0).text);
  }
  @Test
  public void when_done_pressed_item_goes_down(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    holderUnderTest.addNewInProgressItem("did shopping");
    holderUnderTest.markItemDone(holderUnderTest.getCurrentItems().get(0));

    // verify
    Assert.assertEquals("do shopping", holderUnderTest.getCurrentItems().get(0).text);
  }
  @Test
  public void when_remove_is_pressed_size_goes_down(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    holderUnderTest.addNewInProgressItem("did shopping");
    holderUnderTest.deleteItem(holderUnderTest.getCurrentItems().get(0));

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());
  }
  @Test
  public void when_remove_is_pressed_correct_item_is_deleted(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    holderUnderTest.addNewInProgressItem("did shopping");
    holderUnderTest.deleteItem(holderUnderTest.getCurrentItems().get(0));

    // verify
    Assert.assertEquals("do shopping", holderUnderTest.getCurrentItems().get(0).text);
  }
  @Test
  public void done_then_in_progress_handled_correctly(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    holderUnderTest.addNewInProgressItem("did shopping");
    holderUnderTest.markItemDone(holderUnderTest.getCurrentItems().get(0));

    // verify
    Assert.assertEquals("do shopping", holderUnderTest.getCurrentItems().get(0).text);
    holderUnderTest.markItemInProgress(holderUnderTest.getCurrentItems().get(1));
    Assert.assertEquals("did shopping", holderUnderTest.getCurrentItems().get(0).text);
  }
  @Test
  public void remove_from_empty_holder_is_not_crashing(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    TodoItem t = new TodoItem("sddsd", TodoItem.IN_PROGRESS);
    // test
    holderUnderTest.deleteItem(t);
  }
  @Test
  public void two_in_progress_dose_not_change_location(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    holderUnderTest.addNewInProgressItem("did shopping");

    // verify
    Assert.assertEquals("did shopping", holderUnderTest.getCurrentItems().get(0).text);
    holderUnderTest.markItemInProgress(holderUnderTest.getCurrentItems().get(0));
    Assert.assertEquals("did shopping", holderUnderTest.getCurrentItems().get(0).text);
  }
  @Test
  public void three_items_ordered_as_expected(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();

    // test
    holderUnderTest.addNewInProgressItem("1");
    holderUnderTest.addNewInProgressItem("2");
    holderUnderTest.addNewInProgressItem("3");

    // verify
    Assert.assertEquals("3", holderUnderTest.getCurrentItems().get(0).text);
    Assert.assertEquals("2", holderUnderTest.getCurrentItems().get(1).text);
    Assert.assertEquals("1", holderUnderTest.getCurrentItems().get(2).text);
  }
  @Test
  public void three_items_and_done_works_as_expected(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();

    // test
    holderUnderTest.addNewInProgressItem("1");
    holderUnderTest.addNewInProgressItem("2");
    holderUnderTest.addNewInProgressItem("3");

    holderUnderTest.markItemDone(holderUnderTest.getCurrentItems().get(1));

    // verify
    Assert.assertEquals("3", holderUnderTest.getCurrentItems().get(0).text);
    Assert.assertEquals("1", holderUnderTest.getCurrentItems().get(1).text);
    Assert.assertEquals("2", holderUnderTest.getCurrentItems().get(2).text);
  }
  @Test
  public void three_items_done_and_inProgress_works_as_expected(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();

    // test
    holderUnderTest.addNewInProgressItem("1");
    holderUnderTest.addNewInProgressItem("2");
    holderUnderTest.addNewInProgressItem("3");

    holderUnderTest.markItemDone(holderUnderTest.getCurrentItems().get(1));
    holderUnderTest.markItemInProgress(holderUnderTest.getCurrentItems().get(2));

    // verify
    Assert.assertEquals("3", holderUnderTest.getCurrentItems().get(0).text);
    Assert.assertEquals("2", holderUnderTest.getCurrentItems().get(1).text);
    Assert.assertEquals("1", holderUnderTest.getCurrentItems().get(2).text);
  }



  // TODO: add at least 10 more tests to verify correct behavior of your implementation of `TodoItemsHolderImpl` class
}