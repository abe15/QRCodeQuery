package com.abe.android.floorcodes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.abe.android.floorcodes.AppDatabase;
import com.abe.android.floorcodes.R;
import com.abe.android.floorcodes.models.StackingFilter;

import java.util.ArrayList;
import java.util.List;





public class FilterListAdapter extends ArrayAdapter {

    private List<StackingFilter> dataList;
    private Context mContext;
    private int itemLayout;
    private ItemClickListener itemClickListener;

    private FilterListAdapter.ListFilter listFilter = new FilterListAdapter.ListFilter();
    //private AppDatabase db;

    public FilterListAdapter(Context context, int resource, List<StackingFilter> objects) {
        super(context, resource, objects);
        dataList = objects;
        itemLayout = resource;
        mContext = context;
      //  itemClickListener = i;


    }

    @Override
    public int getCount() {
        return dataList.size();
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }


    @Override
    public StackingFilter getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName =  view.findViewById(R.id.filter);
        strName.setText(getItem(position).getStackingFilter());
        //strName.setOnClickListener(itemClickListener);
        return view;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = new ArrayList<String>();
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = prefix.toString();

                //Call to database to get matching records using room
                List<StackingFilter> matchValues =
                        //localDatabaseRepo.getStoreInfo(mContext, searchStrLowerCase);
                        AppDatabase.getAppDatabase(mContext).myDao().findCodeMultiple(searchStrLowerCase);

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<StackingFilter>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }


    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
