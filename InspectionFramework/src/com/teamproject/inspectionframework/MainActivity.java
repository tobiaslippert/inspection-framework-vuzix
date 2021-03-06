package com.teamproject.inspectionframework;

import com.teamproject.inspectionframework.List_Adapters.TabAdapterLoginScreen;
//import com.teamproject.inspectionframework.vuzixHelpers.VuzixVoiceControl;
import com.vuzix.speech.VoiceControl;
import com.vuzix.speech.Constants;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
	private TabAdapterLoginScreen mAdapter;
	private ActionBar actionBar;
//	private VoiceControl vc;
	private String[] tabs = { "Login", "User list" };

/*	@Override
	protected void onResume() {
		super.onResume();
		vc.on();
	}

	@Override
	protected void onPause() {
		super.onPause();
		vc.off();
	}
	
	if(vc != null) vc.destroy();
*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set title for Action Bar
		actionBar = getActionBar();
		actionBar.setTitle("User Login");

		// Set Voice Control
	//	vc = new VuzixVoiceControl(getBaseContext());
	//	vc.addGrammar(Constants.GRAMMAR_BASIC);

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.loginScreenPager);
		mAdapter = new TabAdapterLoginScreen(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		// Sets the tab when view is changed by swiping left/right
		// TODO: Deactivate swiping possibility
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}
		});
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
		int menuItemId = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	public void onClickContinueButton(View view) {

		Log.i("IF", "continueButtonPressed");

		// Intent to
		Intent goToAssignmentListIntent = new Intent(this, AssignmentList.class);
		startActivity(goToAssignmentListIntent);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// Not needed

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Not needed

	}
}
