//Faisal Khan
//S1828698
package com.example.trafficscotland;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter implements Filterable {

    private List<CurrentIncidents> originalData;
    private List<CurrentIncidents> filteredData = null;
    private LayoutInflater layoutInflater;
    Context context;
    int layoutId;
    ValueFilter valueFilter;

    public ListViewAdapter (Context context, int viewId, List<CurrentIncidents> currentIncidentsList)
    {
        this.context = context;
        this.layoutId = viewId;
        this.filteredData = currentIncidentsList;
        this.originalData = currentIncidentsList;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public CurrentIncidents getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        TextView textView;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = View.inflate(context, layoutId, null);

            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.locationText);
            viewHolder.imageView = convertView.findViewById(R.id.itemIcon);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int duration = filteredData.get(position).getDurationInDays();

        if (filteredData == null)
        {
            viewHolder.textView.setText("There is currently no information available");
            //Toast.makeText(context, "No Info available", Toast.LENGTH_SHORT).show();
        }
        else
        {
            viewHolder.textView.setText(filteredData.get(position).getTitle());
        }

        //Set icons for incidents
        if (MainActivity.getIsCurrentIncidents() == true)
        {
            viewHolder.imageView.setImageResource(R.drawable.currentincidentsicon);
        }
        else
        {
            //Set icons for each item depending on the duration
            if (duration >= 0 && duration <= 3)
            {
                viewHolder.imageView.setImageResource(R.drawable.roadworksgreen);
            }
            else if (duration > 3 && duration <= 9)
            {
                viewHolder.imageView.setImageResource(R.drawable.roadworksorange);
            }
            else if (duration >=9 )
            {
                viewHolder.imageView.setImageResource(R.drawable.roadworksred);
            }
        }
        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        if (valueFilter == null)
        {
            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter
    {
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();

            if (charSequence !=null && charSequence.length() >0) {
                List<CurrentIncidents> filteredList = new ArrayList<>();
                for (int i = 0; i < filteredData.size(); i++) {

                    if (filteredData.get(i).getTitle().toUpperCase().contains(charSequence.toString().toUpperCase())) {
                        CurrentIncidents item = filteredData.get(i);
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            else
                {
                    results.count = filteredData.size();
                    results.values = filteredData;
                }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results)
        {
            filteredData = (ArrayList<CurrentIncidents>)results.values;
            notifyDataSetChanged();
        }
    }
}

