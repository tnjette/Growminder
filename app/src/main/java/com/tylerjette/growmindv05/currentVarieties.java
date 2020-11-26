package com.tylerjette.growmindv05;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class currentVarieties extends AppCompatActivity {


    /**this activity will be accessible from the dashboard toolbar menu, and will include simply a recyclerview presenting a list of the user's chosen
     *  varieties. The list will enable navigation to each of the learnMoreAboutVarietyX activities, per variety, and subsequently the calendar for each variety*/

    private ArrayList<String> currentVarietiesList = new ArrayList<>();
    private currentVarietiesAdapter curVarAdapter;
    private RecyclerView recyclerView;
    Context context;
    public static String TAG = currentVarieties.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_varieties_activity);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.currentVarietiesToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView title = findViewById(R.id.currentVarietiesTitle);
        title.setText(R.string.currentVarietiesTitle);

        context = getApplicationContext();
        //read the database

        SQLiteHelper helper = SQLiteHelper.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            String[] varietyProjection = {varietyContract.varietyEntry.COLUMN_VARIETY_ID};
            Cursor cursor = db.query(
                    varietyContract.varietyEntry.TABLE_NAME,
                    varietyProjection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                currentVarietiesList.add(cursor.getString(cursor.getColumnIndexOrThrow(varietyContract.varietyEntry.COLUMN_VARIETY_ID)));
            }
            cursor.close();

        }catch(SQLException ex){
            ex.printStackTrace();
        }

        recyclerView = findViewById(R.id.currentVarietiesRecyclerView);
        Button obsoleteBtn = findViewById(R.id.currentVarieties_nothingToSeeHere_msg_bottom);
        TextView nothingToSeeHereTop = findViewById(R.id.currentVarieties_nothingToSeeHere_msg_top);

        if(currentVarietiesList.size() > 0){
            //set the recyclerView

            ArrayList<variety> listOfVarieties = new ArrayList<>();

            for(String var : currentVarietiesList){
                String baselineID = var.replaceAll("[0-9]", "");
                String varietyName = new getReadableName(baselineID, context).getName();
                int varietyImage = new getImage(baselineID).getImage();
                String botanicalName = new getBotanicalName(baselineID, context).getFinalName();
                variety variety = new variety(varietyName, var, botanicalName, varietyImage,null);
                listOfVarieties.add(variety);
            }

            nothingToSeeHereTop.setVisibility(View.GONE);
            obsoleteBtn.setVisibility(View.GONE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            curVarAdapter = new currentVarietiesAdapter(this, listOfVarieties); //need to create this list from the zoneX json file
            recyclerView.setAdapter(curVarAdapter);

        }else{
            //set the UI to notify the user that there are no varieties to see.
            recyclerView.setVisibility(View.GONE);

            nothingToSeeHereTop.setText(R.string.nothing_to_see_here);
            nothingToSeeHereTop.setText(R.string.nothing_to_see_here);
            obsoleteBtn.setText(R.string.nothing_to_see_here_user_has_no_varieties);
            obsoleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSetVarietiesActivity();
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home :
                Intent intent = new Intent(this, Dashboard.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void goToSetVarietiesActivity(){
        Intent intent = new Intent(this, addVarietiesActivity.class);
        intent.putExtra("previousActivity", "Dashboard");
        startActivity(intent);
    }
}
