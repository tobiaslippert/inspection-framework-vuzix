package com.teamproject.inspectionframework;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Sets subtitle for Action Bar
        ActionBar actionBar = getActionBar();
        actionBar.setSubtitle("User Login");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_synchronize) {
            
        	//TODO: Add synchronization here
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void continueButton(View view) {
    	
    	Log.i("IF","continueButtonPressed");
    	
    	// Intent to 
    	Intent goToAssignmentListIntent = new Intent(ctx,AssignmentList.class);
    	startActivity(goToAssignmentListIntent);
    }
}