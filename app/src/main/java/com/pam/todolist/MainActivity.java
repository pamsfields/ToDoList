package com.pam.todolist;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        AddToDoItemFragment.NewItemCreatedListener,
        ToDoItemDetailFragment.MarkItemAsDoneListener,
        ToDoListFragment.ListItemSelectedListener {


    private static final String TODO_ITEMS_KEY = "TODO ITEMS ARRAY LIST";
    private static final String ADD_NEW_FRAG_TAG = "ADD NEW FRAGMENT";
    private static final String LIST_FRAG_TAG = "LIST FRAGMENT";
    private static final String DETAIL_FRAG_TAG = "DETAIL FRAGMENT";

    private ArrayList<ToDoItem> mTodoItems;

    private static final String TAG = "MAIN ACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mTodoItems = new ArrayList<>();

            AddToDoItemFragment addNewFragment = AddToDoItemFragment.newInstance();
            ToDoListFragment listFragment = ToDoListFragment.newInstance(mTodoItems);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.add_todo_view_container, addNewFragment, ADD_NEW_FRAG_TAG);
            ft.add(R.id.todo_list_view_container, listFragment, LIST_FRAG_TAG);

            ft.commit();
        }else{

            mTodoItems = savedInstanceState.getParcelableArrayList(TODO_ITEMS_KEY);
            Log.d(TAG, "onCreate has saved instance stat ArrayList = "+mTodoItems);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        super.onSaveInstanceState(outBundle);
        outBundle.putParcelableArrayList(TODO_ITEMS_KEY, mTodoItems);
    }

    @Override
    public void newItemCreated(ToDoItem newItem) {
        mTodoItems.add(newItem);

        Log.d(TAG, "newItemCreated = "+ mTodoItems);

        FragmentManager fm = getSupportFragmentManager();
        ToDoListFragment listFragment = (ToDoListFragment) fm.findFragmentByTag(LIST_FRAG_TAG);
        listFragment.notifyItemsChanged();
    }

    @Override
    public void itemSelected(ToDoItem selected) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //Create a new Detail fragment. Add it to the Activity.
        ToDoItemDetailFragment detailFragment = ToDoItemDetailFragment.newInstance(selected);
        ft.add(android.R.id.content, detailFragment);
        //Add to the back stack, so if user presses back button from the Detail
        //fragment, it will revert this transaction - Activity will go back to the Add+List fragments
        ft.addToBackStack(DETAIL_FRAG_TAG);
        ft.commit();
    }

    @Override
    public void todoItemDone(ToDoItem doneItem) {
        mTodoItems.remove(doneItem);

        Log.d(TAG, "newItemRemoved list is now = "+mTodoItems);

        //Find List fragment and tell it that the data has changed
        FragmentManager fm = getSupportFragmentManager();
        ToDoListFragment listFragment = (ToDoListFragment) fm.findFragmentByTag(LIST_FRAG_TAG);
        listFragment.notifyItemsChanged();

        //revert the last fragment transaction on the back stack.
        //This removes the Detail fragment from the Activity, which leaves the Add+List fragments.

        FragmentTransaction ft = fm.beginTransaction();
        fm.popBackStack();
        ft.commit();
    }
}
