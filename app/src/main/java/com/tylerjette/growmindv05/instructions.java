package com.tylerjette.growmindv05;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tylerjette on 3/27/18.
 */

public class instructions extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.instructions);

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){

            TextView instructionsTitle =findViewById(R.id.instructionsTitle);
            ConstraintLayout.MarginLayoutParams instructionsTitleLayoutParams= new ConstraintLayout.LayoutParams(instructionsTitle.getLayoutParams());
            instructionsTitleLayoutParams.setMargins(0,0,0,0);

            TextView instructionsWelcomeMsg = findViewById(R.id.instructionsWelcomeMsg);
            instructionsWelcomeMsg.setPadding(32,16,32,16);

            LinearLayout step1LinearLayout = findViewById(R.id.instruction_activity_step1_linearLayout);
            LinearLayout step2LinearLayout = findViewById(R.id.instruction_activity_step2_linearLayout);
            LinearLayout step3LinearLayout = findViewById(R.id.instruction_activity_step3_linearLayout);

            //change the orientation for each of these...

            step1LinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            step2LinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            step3LinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            //...continue...

            step1LinearLayout.setGravity(Gravity.CENTER_VERTICAL);
            step2LinearLayout.setGravity(Gravity.CENTER_VERTICAL);
            step3LinearLayout.setGravity(Gravity.CENTER_VERTICAL);


            //1
            //title textView
            TextView step1TitleTextView = findViewById(R.id.instructions_step1_title);
            LinearLayout.MarginLayoutParams step1TitleMargs = new LinearLayout.LayoutParams(step1TitleTextView.getLayoutParams());
            step1TitleTextView.setPadding(60,0,0,0);
            step1TitleTextView.setLayoutParams(step1TitleMargs);
            LinearLayout.LayoutParams step1TitleLayoutParams = new LinearLayout.LayoutParams(step1TitleTextView.getLayoutParams());
            step1TitleLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            step1TitleTextView.setLayoutParams(step1TitleLayoutParams);

            //body textView
            TextView step1BodyTextView = findViewById(R.id.instructions_step1_body);
            step1BodyTextView.setGravity(Gravity.NO_GRAVITY);
            step1BodyTextView.setPadding(32,25,60,25);
            LinearLayout.LayoutParams step1BodyLayoutParams = new LinearLayout.LayoutParams(step1BodyTextView.getLayoutParams());
            step1BodyLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            step1BodyTextView.setLayoutParams(step1BodyLayoutParams);

            //2
            //title textView
            TextView step2TitleTextView = findViewById(R.id.instructions_step2_title);
            LinearLayout.MarginLayoutParams step2TitleMargs = new LinearLayout.LayoutParams(step2TitleTextView.getLayoutParams());
            step2TitleTextView.setPadding(60,0,0,0);
            step2TitleTextView.setLayoutParams(step2TitleMargs);
            LinearLayout.LayoutParams step2TitleLayoutParams = new LinearLayout.LayoutParams(step2TitleTextView.getLayoutParams());
            step2TitleLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            step2TitleTextView.setLayoutParams(step2TitleLayoutParams);

            //body textView
            TextView step2BodyTextView = findViewById(R.id.instructions_step2_body);
            step2BodyTextView.setGravity(Gravity.NO_GRAVITY);
            step2BodyTextView.setPadding(32,25,60,25);
            LinearLayout.LayoutParams step2BodyLayoutParams = new LinearLayout.LayoutParams(step2BodyTextView.getLayoutParams());
            step2BodyLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            step2BodyTextView.setLayoutParams(step2BodyLayoutParams);

            //3
            //title textView
            TextView step3TitleTextView = findViewById(R.id.instructions_step3_title);
            LinearLayout.MarginLayoutParams step3TitleMargs = new LinearLayout.LayoutParams(step3TitleTextView.getLayoutParams());
            step3TitleTextView.setPadding(60,0,0,0);
            step3TitleTextView.setLayoutParams(step3TitleMargs);
            LinearLayout.LayoutParams step3TitleLayoutParams = new LinearLayout.LayoutParams(step3TitleTextView.getLayoutParams());
            step3TitleLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            step3TitleTextView.setLayoutParams(step3TitleLayoutParams);

            //body textView
            TextView step3BodyTextView = findViewById(R.id.instructions_step3_body);
            step3BodyTextView.setGravity(Gravity.NO_GRAVITY);
            step3BodyTextView.setPadding(32,25,60,25);
            LinearLayout.LayoutParams step3BodyLayoutParams = new LinearLayout.LayoutParams(step3BodyTextView.getLayoutParams());
            step3BodyLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            step3BodyTextView.setLayoutParams(step3BodyLayoutParams);
        }
        Intent activityIntent = getIntent();
        checkMessages(activityIntent);
    }

    public void checkMessages(Intent intent){

        if(intent.hasExtra("previousActivity")){
            if(intent.getStringExtra("previousActivity").equals("Dashboard")){
                Toolbar customToolbar = findViewById(R.id.custom_instructions_toolbar);
                setSupportActionBar(customToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                Button beginButton = findViewById(R.id.begin_button);
                beginButton.setVisibility(View.GONE);
            }
        } else {
            //here is setting the UI as per the beginning of the User's experience
            Toolbar customToolbar = findViewById(R.id.custom_instructions_toolbar);
            setSupportActionBar(customToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    public void beginFromWelcome(View view){
        Intent intent = new Intent(this, Dashboard.class);
        intent.putExtra("msg_from_instructions", "addLocationActivity");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.instructions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.currentVarieties_instructions:
                goToCurrentVarietiesActivity();
                return true;
            case R.id.setLocationMenu_instructions:
                goToSetLocationActivity();
                return true;
            case R.id.setVarietiesMenu_instructions:
                goToSetVarietiesActivity();
                return true;
            case R.id.mostRecentNotification_instructions:
                goToUpcomingNotificationsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.custom_instructions_toolbar);
        float textAlphaTo = 0.35f;
        ObjectAnimator.ofFloat(toolbar, "alpha",textAlphaTo).setDuration(750).start();
        FrameLayout backgroundFader = findViewById(R.id.instructions_background_fader);
        int colorFrom = Color.parseColor("#00000000");
        int colorTo = Color.parseColor("#99000000");
        if(Build.VERSION.SDK_INT > 20){
            backgroundFader.setElevation(5.0f);
        }
        ObjectAnimator.ofObject(backgroundFader, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                .setDuration(750)
                .start();
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        float textAlphaFrom = 1.0f;
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.custom_instructions_toolbar);
        ObjectAnimator.ofFloat(toolbar, "alpha",textAlphaFrom).setDuration(750).start();
        FrameLayout backgroundFader = findViewById(R.id.instructions_background_fader);
        int colorTo = Color.parseColor("#00000000");
        int colorFrom = Color.parseColor("#90000000");
        ObjectAnimator.ofObject(backgroundFader, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                .setDuration(750)
                .start();
        super.onPanelClosed(featureId, menu);
    }

    /**options menu selectors*/
    public void goToCurrentVarietiesActivity(){
        Intent intent = new Intent(this, currentVarieties.class);
        startActivity(intent);
    }
    public void goToSetLocationActivity(){
        Intent intent = new Intent(this, addLocationActivity.class);
        startActivity(intent);
    }
    public void goToSetVarietiesActivity(){
        Intent intent = new Intent(this, addVarietiesActivity.class);
        intent.putExtra("comingFromDashboard", true);
        startActivity(intent);
    }
    public void goToUpcomingNotificationsActivity(){
        Intent intent = new Intent(this, upcomingNotificationsActivity.class);
        startActivity(intent);
    }

}
