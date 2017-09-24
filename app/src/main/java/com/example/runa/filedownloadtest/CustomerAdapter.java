package com.example.runa.filedownloadtest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by runa on 23.09.17.
 */

public class CustomerAdapter extends ArrayAdapter implements Filterable {

    private final Context context;
    private final ArrayList<Customer> customers;
    private ArrayList<Customer> customersFiltered;
    private TextView tvCName;
    private TextView tvCNumber;
    private CustomerFilter mFilter = new CustomerFilter();


    public CustomerAdapter(Context context, int layoutResourceId, ArrayList<Customer> customers) {
        super(context, layoutResourceId, customers);
        this.context=context;
        this.customers=customers;
        customersFiltered=customers;
    }

    public Customer getItem(int position){
        return customersFiltered.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public int getCount (){
        return customersFiltered.size();
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Log.d("position", Integer.toString(position));
        Customer customer = customersFiltered.get(position);
        Log.d("inflating customer", customer.toString());
        if (convertView == null) {
            inflater.inflate(R.layout.customer_view, null, false);
        }
        //get row view from inflater
        View rowView = inflater.inflate(R.layout.customer_view, parent, false);

        tvCName = (TextView) rowView.findViewById(R.id.tvCName);
        tvCNumber = (TextView) rowView.findViewById(R.id.tvCNumber);

        tvCName.setText(customer.getName());
        tvCNumber.setText(customer.getNumber().toString());
        
        return rowView;


    }

    public Filter getFilter() {
        return mFilter;
    }

    private class CustomerFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();

                final ArrayList<Customer> allCustomers = customers;

                int count = allCustomers.size();
                final ArrayList<Customer> filteredCustomers = new ArrayList<Customer>(count);

                Customer customer;

                for (int i = 0; i < count; i++) {
                    customer=allCustomers.get(i);
                    if (customer.getName().contains(filterString)
                            || customer.getNumber().toString().contains(filterString)) {
                        filteredCustomers.add(customer);
                    }
                }

                results.values = filteredCustomers;
                results.count = filteredCustomers.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                customersFiltered = (ArrayList<Customer>) results.values;
                notifyDataSetChanged();
            }

    }


}
