package com.tylerjette.growmindv05;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tylerjette on 2/27/18.
 */

public class dashboardAdapter extends RecyclerView.Adapter<dashboardAdapter.EventsViewHolder>{

    private static List<notificationEvent> eventsList;
    private Context context;

    public dashboardAdapter(Context context, List<notificationEvent> eventsList){
        this.context = context;
        this.eventsList = eventsList;
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder{

        ImageView varietyImage;
        TextView varietyName;
        TextView varietyEvent;
        TextView varietyEventTitle;
        TextView cycle;

        RelativeLayout rel;

        public EventsViewHolder (View v){
            super(v);
            varietyImage = v. findViewById(R.id.notification_variety_image);
            rel = v.findViewById(R.id.notification_event_item);
            varietyName = v.findViewById(R.id.notification_variety_name);
            varietyEventTitle = v.findViewById(R.id.notification_variety_title);
            varietyEvent = v.findViewById(R.id.notification_variety_event);
            cycle = v.findViewById(R.id.notification_cycle);
        }
    }

    @Override
    public dashboardAdapter.EventsViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        View varietyEventView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);//this is the layout for the actual item in the notification

        return new EventsViewHolder(varietyEventView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder (final EventsViewHolder holder, int position){
        final notificationEvent ne = eventsList.get(position);
        holder.varietyName.setText(ne.getName());
        holder.varietyEvent.setText(ne.getVarietyEvent());

        final Boolean eventCycle = ne.getIsFallCycle();

        holder.varietyEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //values
                String baselineID = ne.getBaselineID();
                String varietyName = ne.getName();
                String eventTitle = ne.getEventTitle();
                String eventDetail = ne.getVarietyEvent();

                //intent + extras
                Intent goToVarietyCalendarIntent = new Intent(context, eventsCalendarActivity.class);
                goToVarietyCalendarIntent.putExtra("varietySpec", baselineID);
                goToVarietyCalendarIntent.putExtra("varietyName", varietyName);
                goToVarietyCalendarIntent.putExtra("currentEventTitle", eventTitle);
                goToVarietyCalendarIntent.putExtra("eventDetail", eventDetail);
                goToVarietyCalendarIntent.putExtra("eventCycle", eventCycle);
                goToVarietyCalendarIntent.putExtra("previousActivity", "dashboard");

                context.startActivity(goToVarietyCalendarIntent);
            }
        });

        if(eventCycle){
            //if true, then it's the fall cycle and needs to be differentiated as such
            holder.cycle.setText(context.getString(R.string.fall_cycle));
        } else {
            holder.cycle.setText("");
        }

        holder.varietyEventTitle.setText(ne.getEventTitle());

        Picasso.get()
                .load(ne.getVarietyImage())
                .centerCrop()
                .fit()
                .into(holder.varietyImage);

    }

    @Override
    public int getItemCount ()
    {
        return eventsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView rv){
        super.onAttachedToRecyclerView(rv);
    }
}
