package com.tylerjette.growmindv05;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class currentVarietiesAdapter extends RecyclerView.Adapter<currentVarietiesAdapter.ViewHolder> {

    public static String TAG = currentVarietiesAdapter.class.getSimpleName();
    private ArrayList<variety> currentVarietiesList;
    private Context context;

    public currentVarietiesAdapter(Context context, ArrayList<variety> currentVarietiesList){
        this.context = context;
        this.currentVarietiesList = currentVarietiesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView varName;
        TextView botanicalName;
        ImageView varImage;
        LinearLayout currentVarietyFrame;

        public ViewHolder(View view){
            super(view);
            varName = view.findViewById(R.id.currentVarietyName);
            botanicalName = view.findViewById(R.id.currentVarietyBotanicalName);
            varImage = view.findViewById(R.id.currentVarietyImage);
            currentVarietyFrame = view.findViewById(R.id.currentVarietyItemTitleFrame);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View eventView = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_variety_listitem, parent, false);
        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        final variety var = currentVarietiesList.get(position);
        holder.varName.setText(var.getName());
        holder.botanicalName.setText(var.getBotanicalName());
        holder.varImage.setImageResource(var.getVarietyImage());
        //do the rest of these

        Picasso.get()
                .load(var.getVarietyImage())
                .fit()
                .centerCrop()
                .into(holder.varImage);

        holder.currentVarietyFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goTolearnMoreIntent = new Intent(context, learnMoreAboutXVariety.class);
                goTolearnMoreIntent.putExtra("varietyID", var.getZoneVarID());
                goTolearnMoreIntent.putExtra("varietyName", var.getName());
                goTolearnMoreIntent.putExtra("previousActivity", "currentVarieties");
                context.startActivity(goTolearnMoreIntent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return currentVarietiesList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView rv){
        super.onAttachedToRecyclerView(rv);
    }

}
