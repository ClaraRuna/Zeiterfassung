package com.example.runa.filedownloadtest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by runa on 02.10.17.
 * has two Lists with tasks:
 * If no filter string is given, only the customerTasks should be shown in the ListView
 * Otherwise the filtered allTasks should be shown
 */

public class TaskAdapter extends ArrayAdapter implements Filterable {

    private TaskAdapter.TaskFilter mFilter = new TaskAdapter.TaskFilter();
    private Context context;
    //given task lists
    private ArrayList<TaskTemplate> allTasks;
    private ArrayList<TaskTemplate> customerTasks;
    //resulting tasks
    private ArrayList<TaskTemplate> filteredTasks;


    public TaskAdapter(Context context, int layoutResourceId, ArrayList<TaskTemplate> customerTasks, ArrayList<TaskTemplate> allTasks) {
        super(context, layoutResourceId, customerTasks);
        this.context=context;
        this.customerTasks=customerTasks;
        this.allTasks=allTasks;
        this.filteredTasks=customerTasks;
    }

    public TaskTemplate getItem(int position){
        return filteredTasks.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public int getCount (){
        return filteredTasks.size();
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Log.d("position", Integer.toString(position));
        TaskTemplate task = filteredTasks.get(position);
        if (convertView == null) {
            inflater.inflate(R.layout.customer_view, null, false);
        }
        //get row view from inflater
        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        TextView tv = (TextView) rowView.findViewById(android.R.id.text1);
        tv.setText(task.getName());

        return rowView;
    }


    public Filter getFilter() {
        return mFilter;
    }

    private class TaskFilter extends Filter {

        /**
         *
         * @param constraint the string to filter
         * @return results: customerTasks, if no filterString was given
         *                  filtered allTasks if there was a filter constraint
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //preprocess the constraint for filtering
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            //no filter string is given -> result = customerTasks
            Log.d("filterString.length()", Integer.toString(filterString.length()));
            if (filterString.length()==0){
                Log.d("results", "should be customerTasks now");
                filteredTasks=customerTasks;
                results.values=customerTasks;
                results.count=customerTasks.size();
            }
            //some filter string is given-> result = filtered allTasks
            else{
                //undo last filtering
                filteredTasks.clear();
                //check allTasks if they contain the filtered string
                for (TaskTemplate t : allTasks){
                    if (t.getName().toLowerCase().contains(filterString)){
                        filteredTasks.add(t);
                    }
                }
                results.values=filteredTasks;
                results.count=filteredTasks.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredTasks = (ArrayList<TaskTemplate>) results.values;
            notifyDataSetChanged();
        }

    }
}
