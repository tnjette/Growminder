package com.tylerjette.growmindv05;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tylerjette on 4/7/18.
 */

public class learnMoreActivityCalendarAdapter extends RecyclerView.Adapter<learnMoreActivityCalendarAdapter.ViewHolder>{

    private static List<learnMoreCalendarEvent> learnMoreCalendarEventList;
    private Context context;

    public learnMoreActivityCalendarAdapter(Context context, List<learnMoreCalendarEvent> learnMoreCalendarEventList){
        this.context = context;
        this.learnMoreCalendarEventList = learnMoreCalendarEventList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventTitle;
        TextView eventDate;
        TextView detailsObj;
        public ViewHolder (View v){
            super( v );
            eventTitle = v.findViewById(R.id.learn_more_eventtitle);
            eventDate = v.findViewById(R.id.learn_more_date_obj);
            detailsObj = v.findViewById(R.id.learn_more_event_details);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View eventView = LayoutInflater.from(parent.getContext()).inflate(R.layout.learn_more_variety_listitem, parent, false);
        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        final learnMoreCalendarEvent calEvent = learnMoreCalendarEventList.get(position);
        String date_obj = calEvent.getEventDate();
        holder.eventDate.setText(date_obj);
        holder.eventTitle.setText(calEvent.getEventTitle());
        holder.detailsObj.setText(calEvent.getEventDescription_expand());
    }

    @Override
    public int getItemCount(){
        return learnMoreCalendarEventList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView rv){
        super.onAttachedToRecyclerView(rv);
    }
}
