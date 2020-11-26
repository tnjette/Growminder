package com.tylerjette.growmindv05;

/**
 * Created by tylerjette on 11/16/17.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class varietyAdapter extends RecyclerView.Adapter<varietyAdapter.ViewHolder> {

    //private static final String TAG = varietyAdapter.class.getSimpleName();
    //private List<variety> varietyList; //try with static keyword
    private static List<variety> varietyList;

    private static ArrayList<String> checkedVarietyList = new ArrayList<>(); //this needs to be sent back to the activity eventually onSave() click
    private String userZone;

    //to be replaced by...
    private ArrayList<String> varietiesInDB = new ArrayList<>(); //moved to onBindViewHolder function below
    private ArrayList<String> passedCheckList = new ArrayList<>();
    //static ArrayList<String> varietiesInDB = new ArrayList<>();
    private int numberOfAvailableVarieties;
    TextView selectAllButton;

    private Context context;
    public varietyAdapter(Context context, List<variety> varietyList, ArrayList varietiesInDB, String userZone, int numberOfAvailableVarieties, TextView selectAllButton){
        this.varietyList = varietyList;
        this.context = context;
        this.varietiesInDB = varietiesInDB;
        this.userZone = userZone;
        this.numberOfAvailableVarieties = numberOfAvailableVarieties;
        this.selectAllButton = selectAllButton;

        //Log.d(TAG, "from variety adapter, " + varietyList.toString());

        //Log.d(TAG, "***************this is the varietiesInDB object from within the varietyAdapter, which will also perform as the checklist object being passed after the savedInstanceState : " + varietiesInDB.toString());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView varietyImage;
        TextView varietyName;
        TextView varietyDescription;
        SwitchCompat varietySwitch;
        LinearLayout varietyClickable;

        public ViewHolder (View v)
        {
            super( v );
            varietyImage = v.findViewById(R.id.variety_image);
            varietyName = v.findViewById(R.id.variety_name);
            varietyDescription = v.findViewById(R.id.variety_description);
            varietySwitch = v.findViewById(R.id.variety_switch);
            varietyClickable = v.findViewById(R.id.variety_clickable);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        View varietyView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.variety_item, parent, false);

        return new ViewHolder(varietyView);
    }

    @Override
    public void onBindViewHolder (final ViewHolder holder, int position)
    {
        /**this is called each time the recycler is called, which is done every time you scroll through
         * the list of available varieties. so you need to remove the dependence of the recycler on the
         * varietiesinDB variable, since it just resets itself to whatever was there before based on
         * what is in the db. For example, if you select something, then at another time, deselect it,
         * then scroll down the list, and the recycler kicks back in, that original switch will be active again,
         * since it is being called on by the recycler onBindView, which references the original varietiesInDB,
         * in which it is active, not inactive.
         */

        //Log.d(TAG, "onBindViewHolder() CALLED, POSITION: " + position);
        //Log.d(TAG, "current status of varietiesInDB : " + varietiesInDB.toString());
        //Log.d(TAG, "current status of addVarietiesActivity.checklist: " + addVarietiesActivity.checkList);
        final variety variety = varietyList.get( position );
        holder.varietyName.setText(variety.getName());
        //Log.d(TAG, "varietyName : from position " + position + " is " + variety.getName());
        holder.varietyDescription.setText(variety.getBotanicalName()); //this should eventually be the description or scientific name
        //holder.varietySwitch.setTag(variety.getZoneVarID()+"_switch"); //you can then use this tag via .getTag() to retreive which variety
        holder.varietySwitch.setTag(variety.getZoneVarID()); //now the tag and the id are the same
        //holder.varietySwitch.setOnCheckedChangeListener(null);
        holder.varietySwitch.setId(position); //so position should reflect the position in the varietyList
        holder.varietySwitch.setClickable(true);

        /*holder.varietyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String varietyName = variety.getName();
                String varietyID = variety.getZoneVarID();
                learnMore(varietyName, varietyID, varietiesInDB);
            }
        });*/

        holder.varietyClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String varietyName = variety.getName();
                String varietyID = variety.getZoneVarID();
                learnMore(varietyName, varietyID, varietiesInDB);
            }
        });

        if(varietiesInDB != null){
            //holder.varietySwitch.getId();
            //Log.d(TAG, holder.varietySwitch.getTag().toString());
            //Log.d(TAG, "current status of varietiesInDB : " + varietiesInDB.toString());
            //Log.d(TAG, "current status of addVarietiesActivity.checklist: " + addVarietiesActivity.checkList);


            if(holder.varietySwitch.getTag().toString().equals("artichoke" + userZone)){
                //Log.d(TAG, "artichoke" + userZone + "present");
                if (varietiesInDB.contains("artichoke" + userZone) && checkCheckList("artichoke" + userZone)){
                    //Log.d(TAG, "varietiesInDB and checklist contains artichoke" + userZone +" :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("artichoke" + userZone) && !checkCheckList("artichoke" + userZone)){
                    //Log.d(TAG, "only varietiesInDB contains artichoke" + userZone + " :should turn off switch");
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("artichoke" + userZone) && checkCheckList("artichoke" + userZone)){
                    //Log.d(TAG, "only checklist contains artichoke" + userZone + " :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("artichoke" + userZone) && !checkCheckList("artichoke" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the artichoke switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("asparagus" + userZone)){
                //Log.d(TAG, "asparagus" + userZone + "present");
                if (varietiesInDB.contains("asparagus" + userZone) && checkCheckList("asparagus" + userZone)){
                    //Log.d(TAG, "varietiesInDB and checklist contains asparagus" + userZone +" :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("asparagus" + userZone) && !checkCheckList("asparagus" + userZone)){
                    //Log.d(TAG, "only varietiesInDB contains asparagus" + userZone + " :should turn off switch");
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("asparagus" + userZone) && checkCheckList("asparagus" + userZone)){
                    //Log.d(TAG, "only checklist contains asparagus" + userZone + " :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("asparagus" + userZone) && !checkCheckList("asparagus" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the asparagus switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("beansshell" + userZone)) {
                //Log.d(TAG, "beanssnap" + userZone + "present");
                if (varietiesInDB.contains("beansshell" + userZone) && checkCheckList("beansshell" + userZone)){
                    //Log.d(TAG, "varietiesInDB and checklist contains beanssnap" + userZone +" :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("beansshell" + userZone) && !checkCheckList("beansshell" + userZone)){
                    //Log.d(TAG, "only varietiesInDB contains beanssnap" + userZone + " :should turn off switch");
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("beansshell" + userZone) && checkCheckList("beansshell" + userZone)){
                    //Log.d(TAG, "only checklist contains beanssnap" + userZone + " :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                }else if (!varietiesInDB.contains("beansshell" + userZone) && !checkCheckList("beansshell" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the beanssnap switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("beanssnap" + userZone)) {
                //Log.d(TAG, "beanssnap" + userZone + "present");
                if (varietiesInDB.contains("beanssnap" + userZone) && checkCheckList("beanssnap" + userZone)){
                    //Log.d(TAG, "varietiesInDB and checklist contains beanssnap" + userZone +" :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("beanssnap" + userZone) && !checkCheckList("beanssnap" + userZone)){
                    //Log.d(TAG, "only varietiesInDB contains beanssnap" + userZone + " :should turn off switch");
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("beanssnap" + userZone) && checkCheckList("beanssnap" + userZone)){
                    //Log.d(TAG, "only checklist contains beanssnap" + userZone + " :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                }else if (!varietiesInDB.contains("beanssnap" + userZone) && !checkCheckList("beanssnap" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the beanssnap switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("beets" + userZone)) {
                //Log.d(TAG, "beets" + userZone + "present");
                if (varietiesInDB.contains("beets" + userZone) && checkCheckList("beets" + userZone)){
                    //Log.d(TAG, "varietiesInDB and checklist contains beets" + userZone +" :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("beets" + userZone) && !checkCheckList("beets" + userZone)){
                    //Log.d(TAG, "only varietiesInDB contains beets" + userZone + " :should turn off switch");
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("beets" + userZone) && checkCheckList("beets" + userZone)){
                    //Log.d(TAG, "only checklist contains beets" + userZone + " :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                }else if (!varietiesInDB.contains("beets" + userZone) && !checkCheckList("beets" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the beets switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("broccoli" + userZone)) {
                //Log.d(TAG, "broccoli" + userZone + "present");
                if (varietiesInDB.contains("broccoli" + userZone) && checkCheckList("broccoli" + userZone)){
                    //Log.d(TAG, "varietiesInDB and checklist contains broccoli" + userZone +" :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("broccoli" + userZone) && !checkCheckList("broccoli" + userZone)){
                    //Log.d(TAG, "only varietiesInDB contains broccoli" + userZone + " :should turn off switch");
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("broccoli" + userZone) && checkCheckList("broccoli" + userZone)){
                    //Log.d(TAG, "only checklist contains broccoli" + userZone + " :should turn on switch");
                    holder.varietySwitch.setChecked(true);
                }else if (!varietiesInDB.contains("broccoli" + userZone) && !checkCheckList("broccoli" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the broccoli switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("brusselssprouts" + userZone)) {

                if (varietiesInDB.contains("brusselssprouts" + userZone) && checkCheckList("brusselssprouts" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("brusselssprouts" + userZone) && !checkCheckList("brusselssprouts" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("brusselssprouts" + userZone) && checkCheckList("brusselssprouts" + userZone)){
                    holder.varietySwitch.setChecked(true);
                }else if (!varietiesInDB.contains("brusselssprouts" + userZone) && !checkCheckList("brusselssprouts" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the brusselssprouts switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("cabbage" + userZone)) {

                if (varietiesInDB.contains("cabbage" + userZone) && checkCheckList("cabbage" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("cabbage" + userZone) && !checkCheckList("cabbage" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("cabbage" + userZone) && checkCheckList("cabbage" + userZone)){
                    holder.varietySwitch.setChecked(true);
                }else if (!varietiesInDB.contains("cabbage" + userZone) && !checkCheckList("cabbage" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the cabbage switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("cantaloupes" + userZone)) {

                if (varietiesInDB.contains("cantaloupes" + userZone) && checkCheckList("cantaloupes" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("cantaloupes" + userZone) && !checkCheckList("cantaloupes" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("cantaloupes" + userZone) && checkCheckList("cantaloupes" + userZone)){
                    holder.varietySwitch.setChecked(true);
                }else if (!varietiesInDB.contains("cantaloupes" + userZone) && !checkCheckList("cantaloupes" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the cantaloupes switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("carrots" + userZone)) {

                if (varietiesInDB.contains("carrots" + userZone) && checkCheckList("carrots" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("carrots" + userZone) && !checkCheckList("carrots" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("carrots" + userZone) && checkCheckList("carrots" + userZone)){
                    holder.varietySwitch.setChecked(true);
                }else if (!varietiesInDB.contains("carrots" + userZone) && !checkCheckList("carrots" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the carrots switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("cauliflower" + userZone)) {

                if (varietiesInDB.contains("cauliflower" + userZone) && checkCheckList("cauliflower" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("cauliflower" + userZone) && !checkCheckList("cauliflower" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("cauliflower" + userZone) && checkCheckList("cauliflower" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("cauliflower" + userZone) && !checkCheckList("cauliflower" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the cauliflower switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("celery" + userZone)) {

                if (varietiesInDB.contains("celery" + userZone) && checkCheckList("celery" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("celery" + userZone) && !checkCheckList("celery" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("celery" + userZone) && checkCheckList("celery" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("celery" + userZone) && !checkCheckList("celery" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the celery switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("chard" + userZone)) {

                if (varietiesInDB.contains("chard" + userZone) && checkCheckList("chard" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("chard" + userZone) && !checkCheckList("chard" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("chard" + userZone) && checkCheckList("chard" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("chard" + userZone) && !checkCheckList("chard" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the chard switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("chinesecabbage" + userZone)) {

                if (varietiesInDB.contains("chinesecabbage" + userZone) && checkCheckList("chinesecabbage" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("chinesecabbage" + userZone) && !checkCheckList("chinesecabbage" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("chinesecabbage" + userZone) && checkCheckList("chinesecabbage" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("chinesecabbage" + userZone) && !checkCheckList("chinesecabbage" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the chinesecabbage switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("chives" + userZone)) {

                if (varietiesInDB.contains("chives" + userZone) && checkCheckList("chives" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("chives" + userZone) && !checkCheckList("chives" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("chives" + userZone) && checkCheckList("chives" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("chives" + userZone) && !checkCheckList("chives" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the chives switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("collardgreens" + userZone)) {

                if (varietiesInDB.contains("collardgreens" + userZone) && checkCheckList("collardgreens" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("collardgreens" + userZone) && !checkCheckList("collardgreens" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("collardgreens" + userZone) && checkCheckList("collardgreens" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("collardgreens" + userZone) && !checkCheckList("collardgreens" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the collardgreens switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("cornsweet" + userZone)) {

                if (varietiesInDB.contains("cornsweet" + userZone) && checkCheckList("cornsweet" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("cornsweet" + userZone) && !checkCheckList("cornsweet" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("cornsweet" + userZone) && checkCheckList("cornsweet" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("cornsweet" + userZone) && !checkCheckList("cornsweet" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the cornsweet switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("cucumbersslicing" + userZone)) {

                if (varietiesInDB.contains("cucumbersslicing" + userZone) && checkCheckList("cucumbersslicing" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("cucumbersslicing" + userZone) && !checkCheckList("cucumbersslicing" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("cucumbersslicing" + userZone) && checkCheckList("cucumbersslicing" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("cucumbersslicing" + userZone) && !checkCheckList("cucumbersslicing" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the cucumbersslicing switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("cucumberspickle" + userZone)) {

                if (varietiesInDB.contains("cucumberspickle" + userZone) && checkCheckList("cucumberspickle" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("cucumberspickle" + userZone) && !checkCheckList("cucumberspickle" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("cucumberspickle" + userZone) && checkCheckList("cucumberspickle" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("cucumberspickle" + userZone) && !checkCheckList("cucumberspickle" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the cucumberspickle switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("dill" + userZone)) {

                if (varietiesInDB.contains("dill" + userZone) && checkCheckList("dill" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("dill" + userZone) && !checkCheckList("dill" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("dill" + userZone) && checkCheckList("dill" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("dill" + userZone) && !checkCheckList("dill" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the dill switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("eggplants" + userZone)) {

                if (varietiesInDB.contains("eggplants" + userZone) && checkCheckList("eggplants" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("eggplants" + userZone) && !checkCheckList("eggplants" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("eggplants" + userZone) && checkCheckList("eggplants" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("eggplants" + userZone) && !checkCheckList("eggplants" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the eggplants switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("endive" + userZone)) {

                if (varietiesInDB.contains("endive" + userZone) && checkCheckList("endive" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("endive" + userZone) && !checkCheckList("endive" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("endive" + userZone) && checkCheckList("endive" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("endive" + userZone) && !checkCheckList("endive" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the endive switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("garlic" + userZone)) {

                if (varietiesInDB.contains("garlic" + userZone) && checkCheckList("garlic" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("garlic" + userZone) && !checkCheckList("garlic" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("garlic" + userZone) && checkCheckList("garlic" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("garlic" + userZone) && !checkCheckList("garlic" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the garlic switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("kale" + userZone)) {

                if (varietiesInDB.contains("kale" + userZone) && checkCheckList("kale" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("kale" + userZone) && !checkCheckList("kale" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("kale" + userZone) && checkCheckList("kale" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("kale" + userZone) && !checkCheckList("kale" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the kale switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("kohlrabi" + userZone)) {

                if (varietiesInDB.contains("kohlrabi" + userZone) && checkCheckList("kohlrabi" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("kohlrabi" + userZone) && !checkCheckList("kohlrabi" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("kohlrabi" + userZone) && checkCheckList("kohlrabi" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("kohlrabi" + userZone) && !checkCheckList("kohlrabi" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the kohlrabi switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("leeks" + userZone)) {

                if (varietiesInDB.contains("leeks" + userZone) && checkCheckList("leeks" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("leeks" + userZone) && !checkCheckList("leeks" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("leeks" + userZone) && checkCheckList("leeks" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("leeks" + userZone) && !checkCheckList("leeks" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the leeks switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("lettucehead" + userZone)) {

                if (varietiesInDB.contains("lettucehead" + userZone) && checkCheckList("lettucehead" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("lettucehead" + userZone) && !checkCheckList("lettucehead" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("lettucehead" + userZone) && checkCheckList("lettucehead" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("lettucehead" + userZone) && !checkCheckList("lettucehead" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the lettucehead switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("lettuceleaf" + userZone)) {

                if (varietiesInDB.contains("lettuceleaf" + userZone) && checkCheckList("lettuceleaf" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("lettuceleaf" + userZone) && !checkCheckList("lettuceleaf" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("lettuceleaf" + userZone) && checkCheckList("lettuceleaf" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("lettuceleaf" + userZone) && !checkCheckList("lettuceleaf" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the lettuceleaf switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("okra" + userZone)) {

                if (varietiesInDB.contains("okra" + userZone) && checkCheckList("okra" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("okra" + userZone) && !checkCheckList("okra" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("okra" + userZone) && checkCheckList("okra" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("okra" + userZone) && !checkCheckList("okra" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the okra switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("onions" + userZone)) {

                if (varietiesInDB.contains("onions" + userZone) && checkCheckList("onions" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("onions" + userZone) && !checkCheckList("onions" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("onions" + userZone) && checkCheckList("onions" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("onions" + userZone) && !checkCheckList("onions" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the onions switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("parsley" + userZone)) {

                if (varietiesInDB.contains("parsley" + userZone) && checkCheckList("parsley" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("parsley" + userZone) && !checkCheckList("parsley" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("parsley" + userZone) && checkCheckList("parsley" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("parsley" + userZone) && !checkCheckList("parsley" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the parsley switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("parsnips" + userZone)) {

                if (varietiesInDB.contains("parsnips" + userZone) && checkCheckList("parsnips" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("parsnips" + userZone) && !checkCheckList("parsnips" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("parsnips" + userZone) && checkCheckList("parsnips" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("parsnips" + userZone) && !checkCheckList("parsnips" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the parsnips switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("peas" + userZone)) {

                if (varietiesInDB.contains("peas" + userZone) && checkCheckList("peas" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("peas" + userZone) && !checkCheckList("peas" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("peas" + userZone) && checkCheckList("peas" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("peas" + userZone) && !checkCheckList("peas" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the peas switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("pepperssweet" + userZone)) {

                if (varietiesInDB.contains("pepperssweet" + userZone) && checkCheckList("pepperssweet" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("pepperssweet" + userZone) && !checkCheckList("pepperssweet" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("pepperssweet" + userZone) && checkCheckList("pepperssweet" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("pepperssweet" + userZone) && !checkCheckList("pepperssweet" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the pepperssweet switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("peppershot" + userZone)) {

                if (varietiesInDB.contains("peppershot" + userZone) && checkCheckList("peppershot" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("peppershot" + userZone) && !checkCheckList("peppershot" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("peppershot" + userZone) && checkCheckList("peppershot" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("peppershot" + userZone) && !checkCheckList("peppershot" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the peppershot switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("potatoessweet" + userZone)) {

                if (varietiesInDB.contains("potatoessweet" + userZone) && checkCheckList("potatoessweet" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("potatoessweet" + userZone) && !checkCheckList("potatoessweet" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("potatoessweet" + userZone) && checkCheckList("potatoessweet" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("potatoessweet" + userZone) && !checkCheckList("potatoessweet" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the potatoessweet switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("potatoeswhite" + userZone)) {

                if (varietiesInDB.contains("potatoeswhite" + userZone) && checkCheckList("potatoeswhite" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("potatoeswhite" + userZone) && !checkCheckList("potatoeswhite" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("potatoeswhite" + userZone) && checkCheckList("potatoeswhite" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("potatoeswhite" + userZone) && !checkCheckList("potatoeswhite" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the potatoeswhite switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("pumpkins" + userZone)) {

                if (varietiesInDB.contains("pumpkins" + userZone) && checkCheckList("pumpkins" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("pumpkins" + userZone) && !checkCheckList("pumpkins" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("pumpkins" + userZone) && checkCheckList("pumpkins" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("pumpkins" + userZone) && !checkCheckList("pumpkins" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the pumpkins switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("radish" + userZone)) {

                if (varietiesInDB.contains("radish" + userZone) && checkCheckList("radish" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("radish" + userZone) && !checkCheckList("radish" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("radish" + userZone) && checkCheckList("radish" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("radish" + userZone) && !checkCheckList("radish" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the radish switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("rhubarb" + userZone)) {

                if (varietiesInDB.contains("rhubarb" + userZone) && checkCheckList("rhubarb" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("rhubarb" + userZone) && !checkCheckList("rhubarb" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("rhubarb" + userZone) && checkCheckList("rhubarb" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("rhubarb" + userZone) && !checkCheckList("rhubarb" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the rhubarb switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("rutabagas" + userZone)) {

                if (varietiesInDB.contains("rutabagas" + userZone) && checkCheckList("rutabagas" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("rutabagas" + userZone) && !checkCheckList("rutabagas" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("rutabagas" + userZone) && checkCheckList("rutabagas" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("rutabagas" + userZone) && !checkCheckList("rutabagas" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the rutabagas switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("spinach" + userZone)) {

                if (varietiesInDB.contains("spinach" + userZone) && checkCheckList("spinach" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("spinach" + userZone) && !checkCheckList("spinach" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("spinach" + userZone) && checkCheckList("spinach" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("spinach" + userZone) && !checkCheckList("spinach" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the spinach switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("squashsummer" + userZone)) {

                if (varietiesInDB.contains("squashsummer" + userZone) && checkCheckList("squashsummer" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("squashsummer" + userZone) && !checkCheckList("squashsummer" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("squashsummer" + userZone) && checkCheckList("squashsummer" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("squashsummer" + userZone) && !checkCheckList("squashsummer" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the squashsummer switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("squashwinter" + userZone)) {

                if (varietiesInDB.contains("squashwinter" + userZone) && checkCheckList("squashwinter" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("squashwinter" + userZone) && !checkCheckList("squashwinter" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("squashwinter" + userZone) && checkCheckList("squashwinter" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("squashwinter" + userZone) && !checkCheckList("squashwinter" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the squashwinter switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("tomatoes" + userZone)) {

                if (varietiesInDB.contains("tomatoes" + userZone) && checkCheckList("tomatoes" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("tomatoes" + userZone) && !checkCheckList("tomatoes" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("tomatoes" + userZone) && checkCheckList("tomatoes" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("tomatoes" + userZone) && !checkCheckList("tomatoes" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the tomatoes switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("tomatillos" + userZone)) {

                if (varietiesInDB.contains("tomatillos" + userZone) && checkCheckList("tomatillos" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("tomatillos" + userZone) && !checkCheckList("tomatillos" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("tomatillos" + userZone) && checkCheckList("tomatillos" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("tomatillos" + userZone) && !checkCheckList("tomatillos" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the tomatillos switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("turnips" + userZone)) {

                if (varietiesInDB.contains("turnips" + userZone) && checkCheckList("turnips" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("turnips" + userZone) && !checkCheckList("turnips" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("turnips" + userZone) && checkCheckList("turnips" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("turnips" + userZone) && !checkCheckList("turnips" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the turnips switch");
                }
            }else if(holder.varietySwitch.getTag().toString().equals("watermelons" + userZone)) {
                if (varietiesInDB.contains("watermelons" + userZone) && checkCheckList("watermelons" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (varietiesInDB.contains("watermelons" + userZone) && !checkCheckList("watermelons" + userZone)){
                    holder.varietySwitch.setChecked(false);
                } else if (!varietiesInDB.contains("watermelons" + userZone) && checkCheckList("watermelons" + userZone)){
                    holder.varietySwitch.setChecked(true);
                } else if (!varietiesInDB.contains("watermelons" + userZone) && !checkCheckList("watermelons" + userZone)){
                    holder.varietySwitch.setChecked(false);
                    //Log.d(TAG, "do nothing to the watermelons switch");
                }
            }
        }  else {
            holder.varietySwitch.setChecked(false);
            //Log.d(TAG, "everything went to false");
        }

        /**trying another something new*/
        /*if(varietiesInDB != null) {

            if (varietiesInDB.contains(variety.getZoneVarID()) && checkCheckList(variety.getZoneVarID())) {
                //variety.setState(true); //trying with a state Boolean in the variety itself
                if(holder.varietySwitch.getTag() == variety.getZoneVarID()+"_switch"){
                    holder.varietySwitch.setChecked(true);
                } else {
                    //if the switch is not the same, then don't turn it on
                }

                //addVarietiesActivity.checkList.add(variety.getZoneVarID());//trying something new
                //checkedVarietyList.add(variety.getZoneVarID());

            } else if (varietiesInDB.contains(variety.getZoneVarID()) && !checkCheckList(variety.getZoneVarID())){
                if(holder.varietySwitch.getTag() == variety.getZoneVarID()+"_switch"){
                    holder.varietySwitch.setChecked(false);
                } else {
                    //if the switch is not the same, then don't turn it on
                }

                //state of the variety should already be false
            } else if (!varietiesInDB.contains(variety.getZoneVarID()) && checkCheckList(variety.getZoneVarID())){
                if(holder.varietySwitch.getTag() == variety.getZoneVarID()+"_switch"){
                    holder.varietySwitch.setChecked(true);
                } else {
                    //if the switch is not the same, then don't turn it on
                }
            }

        } else { //if all are unchecked...
            holder.varietySwitch.setChecked(false); //set all of the variety switches to unchecked
            //Log.d(TAG, "apparently there are no varieties in thedatabase");
            //checkedVarietyList.add(variety.getZoneVarID());
            //Log.d(TAG, variety.getZoneVarID()+"checked");
        }*/


        /***trying something new*/
        /*if(varietiesInDB != null) {

            if (varietiesInDB.contains(variety.getZoneVarID()) && checkCheckList(variety.getZoneVarID())) {
                //variety.setState(true); //trying with a state Boolean in the variety itself
                holder.varietySwitch.setChecked(true);
                //addVarietiesActivity.checkList.add(variety.getZoneVarID());//trying something new
                //checkedVarietyList.add(variety.getZoneVarID());

            } else if (varietiesInDB.contains(variety.getZoneVarID()) && !checkCheckList(variety.getZoneVarID())){
                holder.varietySwitch.setChecked(false);
                //state of the variety should already be false
            } else if (!varietiesInDB.contains(variety.getZoneVarID()) && checkCheckList(variety.getZoneVarID())){
                holder.varietySwitch.setChecked(true);
            }
        } else {
            holder.varietySwitch.setChecked(false); //set all of the variety switches to unchecked
            //Log.d(TAG, "apparently there are no varieties in thedatabase");
            //checkedVarietyList.add(variety.getZoneVarID());
            //Log.d(TAG, variety.getZoneVarID()+"checked");
        }*/





        /*holder.varietySwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //some code....
                        break;
                    case MotionEvent.ACTION_UP:
                        view.performClick();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });*/





        // Listen to touch events on the Switch and, when a tap or drag is starting (ACTION_DOWN),
        // disallow the interception of touch events on the ViewParent (valid until an ACTION_UP).
        holder.varietySwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //nothing
                        break;
                    case MotionEvent.ACTION_UP:
                        //v.performClick();
                            if(checkCheckList(variety.getZoneVarID())){
                                addVarietiesActivity.checkList.remove(variety.getZoneVarID());
                                holder.varietySwitch.setChecked(false);
                                //Log.d(TAG, "removing " + variety.getZoneVarID() + " from the checklist");
                                if(addVarietiesActivity.checkList.size() < numberOfAvailableVarieties){
                                    //Log.d(TAG, "Checklist is not all selected.");
                                    selectAllButton.setText(R.string.select_all_button);
                                    selectAllButton.setBackground(context.getDrawable(R.drawable.rounded_corner_button_sm_allselect));
                                }

                            } else if (!checkCheckList(variety.getZoneVarID())){
                                addVarietiesActivity.checkList.add(variety.getZoneVarID());
                                holder.varietySwitch.setChecked(true);

                                /**create a conditional statement here that compares the checked list
                                 * to the numberOfAvailableVarieties*/
                                if(addVarietiesActivity.checkList.size() == numberOfAvailableVarieties){
                                    //change the UI
                                    //TextView selectAllText = findViewById(R.id.selectAllButton);
                                    //Log.d(TAG, "checklist is all selected!");
                                    selectAllButton.setText(R.string.deselect_all_button);
                                    selectAllButton.setBackground(context.getDrawable(R.drawable.rounded_corner_button_sm_alldeselect));

                                }

                                //Log.d(TAG, "adding " + variety.getZoneVarID() + " tothe checklist");
                            }


                        //read the AddVarietyActivity
                        //Log.d(TAG, "oncheckedchange (isChecked) for " + variety.getZoneVarID());
                        //addVarietiesActivity.checkList.add(variety.getZoneVarID());
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        Picasso.get()
                .load(variety.getVarietyImage())
                .fit()
                .centerCrop()
                .into(holder.varietyImage);

        //Log.d(TAG, "FROM VARIETY ADAPTER: variety.getImage() with " +variety.getName() +" = " + variety.getVarietyImage());
        //Log.d(TAG, "state of the addVarietiesActivity.checklist from the varietyAdapter: " + addVarietiesActivity.checkList.toString());
    }

    @Override
    public int getItemCount ()
    {
        return varietyList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView rv){
        super.onAttachedToRecyclerView(rv);
    }



    public Boolean checkCheckList(String varZoneID){
        if(addVarietiesActivity.checkList.contains(varZoneID)){
            //Log.d(TAG, "********checkCheckList called from within VarietyAdapter for : " + varZoneID);
            return true;
        } else {
            return false;
        }
    }

    public void learnMore(String varietyName, String varietyID, ArrayList varietiesInDBPass){
        Intent intent = new Intent(context, learnMoreAboutXVariety.class);
        intent.putExtra("varietyName", varietyName);
        intent.putExtra("varietyID", varietyID);
        intent.putExtra("previousActivity", "addVarieties");
        //intent.putStringArrayListExtra("varietiesInDBPass", varietiesInDBPass);
        intent.putStringArrayListExtra("transferChecklist", varietiesInDBPass);
        context.startActivity(intent); //clever...
    }
}
