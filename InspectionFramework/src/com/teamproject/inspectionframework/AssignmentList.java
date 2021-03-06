package com.teamproject.inspectionframework;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamproject.inspectionframework.Application_Layer.RESTServices;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.InspectionObject;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Entities.User;
import com.teamproject.inspectionframework.List_Adapters.AssignmentAdapter;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class AssignmentList extends ListActivity {

	// VAR-declaration
	private MySQLiteHelper datasource;
	private RESTServices restInstance;
	private AssignmentAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment_list);

		this.createOutputList();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the menu launcher
		getMenuInflater().inflate(R.menu.assignment_list, menu);
		return true;
	}

	public void createOutputList() {
		datasource = new MySQLiteHelper(getApplicationContext());
		List<Assignment> listWithAllStoredAssignments = datasource.getAllAssignments();

		adapter = new AssignmentAdapter(this, listWithAllStoredAssignments);
		setListAdapter(adapter);

		datasource.close();
	}

	// Listens to clicks on list entries and calls the adapter for retrieving
	// the corresponding assignment object
	protected void onListItemClick(ListView l, android.view.View v, int position, long id) {

		Assignment clickedAssignment = adapter.getClickedAssignment(position);
		Log.i("IF", "Clicked Assignment " + clickedAssignment.getAssignmentName());

		Intent goToTaskListIntent = new Intent(this, TaskList.class);
		goToTaskListIntent.putExtra("AssignmentName", clickedAssignment.getAssignmentName());
		goToTaskListIntent.putExtra("AssignmentId", clickedAssignment.getId());
		startActivity(goToTaskListIntent);
	};

	// TODO: Create new Java class with this sync process
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int menuItemId = item.getItemId();
		switch (menuItemId) {

		// Sync assignments between Heroku server and local database
		case R.id.action_synchronize_assignments:
			Log.i("IF", "Assignment Sync activated");
			datasource = new MySQLiteHelper(getApplicationContext());

			restInstance = new RESTServices();

			String inputAssignment = restInstance.readHerokuServer("assignment");

			try {
				JSONArray jArray = new JSONArray(inputAssignment);

				for (int i = 0; i < jArray.length(); i++) {
					Assignment assignment = new Assignment();
					JSONObject jObject = jArray.getJSONObject(i);

					// Checks if assignment is template
					if (jObject.get("isTemplate").toString() == "true") {
						continue;
					}

					// get and set the values for the table assignments
					assignment.setDescription(jObject.get("description").toString());
					assignment.setAssignmentName(jObject.get("assignmentName").toString());
					assignment.setId(jObject.get("id").toString());
					assignment.setStartDate(jObject.getLong("startDate"));
					assignment.setDueDate(jObject.getLong("endDate"));
					assignment.setInspectionObjectId(jObject.get("isTemplate").toString());
					assignment.setState(jObject.getInt("state"));

					// Download all tasks assigned to an assignment from the
					// server
					// jArrayTask gets the SubJSONObject "tasks"

					JSONArray jArrayTask = new JSONArray(jObject.get("tasks").toString());

					for (int j = 0; j < jArrayTask.length(); j++) {
						Task task = new Task();
						JSONObject jObjectTask = jArrayTask.getJSONObject(j);
						task.setId(jObjectTask.get("id").toString());
						task.setDescription(jObjectTask.get("description").toString());
						task.setAssignmentId(assignment.getId());

						// Checks if Task state is set
						if (jObjectTask.isNull("state")) {
							task.setState(0);
						} else {
							task.setState(jObjectTask.getInt("state"));
						}

						task.setTaskName(jObjectTask.get("taskName").toString());

						// Store all assigned tasks into the database
						datasource.createTask(task);
					}

					JSONObject jObjectInspectionObject = new JSONObject(jObject.get("inspectionObject").toString());
					InspectionObject inspectionObject = new InspectionObject();
					inspectionObject.setId(jObjectInspectionObject.get("id").toString());
					inspectionObject.setObjectName(jObjectInspectionObject.get("objectName").toString());
					inspectionObject.setCustomerName(jObjectInspectionObject.get("customerName").toString());
					inspectionObject.setDescription(jObjectInspectionObject.get("description").toString());
					inspectionObject.setLocation(jObjectInspectionObject.get("location").toString());

					// Store the inspection object into the database
					datasource.createInspectionObject(inspectionObject);

					JSONObject jObjectUser = new JSONObject(jObject.get("user").toString());

					assignment.setUserId(jObjectUser.get("id").toString());
					assignment.setInspectionObjectId(inspectionObject.getId());

					// Store all assignments into the databasea
					datasource.createAssignment(assignment);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// Creates the output list after retrieving updates from the server
			this.createOutputList();
			break;

		default:
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
