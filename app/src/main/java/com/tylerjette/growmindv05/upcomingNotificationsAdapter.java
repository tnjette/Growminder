package com.tylerjette.growmindv05;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tylerjette on 12/23/17.
 */

public class upcomingNotificationsAdapter extends RecyclerView.Adapter<upcomingNotificationsAdapter.ViewHolder> {

    //private static final String TAG = upcomingNotificationsAdapter.class.getSimpleName();
    private Context context;

    List<varietyCal> varietyCalList; //haven't made any varietyCal class yet...
    //HashMap<String, String> varietyCalList;


    public upcomingNotificationsAdapter(Context context, List<varietyCal> varietyCalList){
        this.varietyCalList = varietyCalList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder //implements View.OnClickListener
    {
        ImageView upcomingNotificationVarietyImage;
        TextView upcomingNotificationVarietyName;
        TextView upcomingNotificationVarietyEventTitle;
        TextView varietyCycleSubtitle;

        public ViewHolder (View v)
        {
            super( v );
            upcomingNotificationVarietyImage = (ImageView) v.findViewById(R.id.dashboard_variety_image);
            upcomingNotificationVarietyName = (TextView) v.findViewById(R.id.dashboard_variety_name);
            upcomingNotificationVarietyEventTitle = (TextView) v.findViewById(R.id.dashboard_variety_event_title);
            varietyCycleSubtitle = (TextView) v.findViewById(R.id.variety_cycle_subtitle);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        View dashboardVarietyView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_notification_item_row, parent, false);

        return new ViewHolder(dashboardVarietyView);
    }

    @Override
    public void onBindViewHolder (final ViewHolder holder, int position)
    {
        final varietyCal varietyCal = varietyCalList.get( position );
        holder.upcomingNotificationVarietyName.setText(varietyCal.getName());

        final Boolean eventCycle = varietyCal.getIsFallCycle();
        //Log.d(TAG, "is Fall Cycle for " + varietyCal.getName() + "? : " + String.valueOf(eventCycle));

        /**set the subtitle (cycle title)*/
        String eventTitleStr = varietyCal.getEventTitle();
        String fallCycle = "Fall cycle";
        Boolean isFallCycle = varietyCal.getIsFallCycle();
        if (isFallCycle){
            String reset = context.getString(R.string.fall_cycle);
            holder.varietyCycleSubtitle.setText(reset);
            holder.upcomingNotificationVarietyEventTitle.setText(varietyCal.getEventTitle());
        }else {
            holder.varietyCycleSubtitle.setVisibility(View.GONE);
            holder.upcomingNotificationVarietyEventTitle.setText(varietyCal.getEventTitle());
        }
        final int[] img_width = new int[1];
        ViewTreeObserver vto = holder.upcomingNotificationVarietyImage.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.upcomingNotificationVarietyImage.getViewTreeObserver().removeOnPreDrawListener(this);
                img_width[0] = holder.upcomingNotificationVarietyImage.getMeasuredWidth();
                return true; //return false?
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String baselineID = varietyCal.getbaselineID();
                String varietyName = varietyCal.getName();
                String eventTitle = varietyCal.getEventTitle();
                String eventDetail = varietyCal.getCalendarEvent();
                Intent goToVarietyCalendarIntent = new Intent(context, eventsCalendarActivity.class);
                goToVarietyCalendarIntent.putExtra("varietySpec", baselineID);
                goToVarietyCalendarIntent.putExtra("varietyName", varietyName);
                goToVarietyCalendarIntent.putExtra("upcomingEventTitle", eventTitle);
                goToVarietyCalendarIntent.putExtra("eventDetail", eventDetail); //this is kind of obsolete, other than if you intend to
                goToVarietyCalendarIntent.putExtra("eventCycle",eventCycle);
                goToVarietyCalendarIntent.putExtra("previousActivity", "upcomingNotifications");
                context.startActivity(goToVarietyCalendarIntent);
            }

        });

        Picasso.get()
                .load(varietyCal.getVarietyImage())
                .fit().centerCrop()
                .into(holder.upcomingNotificationVarietyImage);
    }

    @Override
    public int getItemCount ()
    {
        return varietyCalList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView rv){
        super.onAttachedToRecyclerView(rv);
    }

}

