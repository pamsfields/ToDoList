package com.pam.todolist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Displays a list of to do items. Responds to list updates.
 */

public class ToDoListFragment extends Fragment {

    private ListItemSelectedListener mItemSelectedListener;

    private static final String TAG = "TODO LIST FRAGMENT" ;
    private static final String TODO_LIST_ARGS = "to do list arguments";
    private ListItemSelectedListener mListItemSelectedListener;

    private ToDoListArrayAdapter mListAdapter;

    //created newInstance method to send arguments to this Fragment

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ToDoListFragment newInstance(ArrayList todoItems){
        final Bundle args = new Bundle();
        args.putParcelableArrayList(TODO_LIST_ARGS, todoItems);
        final ToDoListFragment fragment = new ToDoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        ListView mListView = (ListView) view.findViewById(R.id.to_do_listview);
        ArrayList<ToDoItem> listItems = getArguments().getParcelableArrayList(TODO_LIST_ARGS);

        Log.d(TAG, "onCreateView, ArrayList: "+ listItems);

        mListAdapter = new ToDoListArrayAdapter(getActivity(), R.layout.todo_list_item_list_element, listItems);

        mListView.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //Notify the listener that the user has clicked on a list item. Send the item clicked on.
                Log.d(TAG, "List item" + position + " clicked, the todo item is "+ mListAdapter.getItem(position));
                mItemSelectedListener.itemSelected(mListAdapter.getItem(position));
            }
        });

        //todo Configured ListView with list of ToDo items
        //todo Use custom ToDoListArrayAdapter

        return view;
    }

    //todo create a method for notifying this Fragment about list updates

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListItemSelectedListener) {
            mItemSelectedListener = (ListItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemSelectedListener = null;
    }

    public interface ListItemSelectedListener {
        void itemSelected(ToDoItem selected);
    }
    public void notifyItemsChanged(){
        Log.d(TAG, "Notified that the list of to do items has changed, update view");
        //tell the list to update.
        mListAdapter.notifyDataSetChanged();
    }
}
