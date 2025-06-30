package com.example.attendance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Apply_LeaveActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    TextView reporting_person,username;
    SessionManager sessionManager;
    ImageView backbtn,userimage;
    DrawerLayout drawerLayout;

    //Leave Balance
    TextView cl_leave,sl_leave,el_leave,comp_off;

    AppCompatSpinner eapplying_for, day_sector, leave_type;
    TimePickerDialog timePickerDialog;
    TextInputEditText permission_from, permission_to, date_from, date_to, reason;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    int choosen_date = 0;
    private String mUserid;
    LinearLayout leave_type_wrapper,permission_from_wrapper, permission_from_to, date_from_wrapper, date_to_wrapper,
            day_sector_wrapper,leave_application,available_leave;

    final Calendar myCalendar = Calendar.getInstance();

    String[] format_options = {"Applying For", "Permission", "OD", "Half Day", "Full Day", "More than a Day"};
    String[] sector_options = {"Fore Noon (AM)", "After Noon (PM)"};
    String[] leave_type_options = {"Casual Leave", "Sick Leave", "Earned Leave", "Comp Off"};

    Button submit;

    String url = "https://eportal.newstamil.tv/attendance/myleave.php";
    String url1 = "https://eportal.newstamil.tv/attendance/leave_balance.php";

    private List<Leave> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LeaveAdapter mAdapter;

    Leave helpdesk;

    ToggleButton leave_history_,leave_apply,leave_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        drawerLayout = findViewById(R.id.drawer_layout);
        //Apply Leave Activity
        leave_apply = findViewById(R.id.leave_apply);
        leave_application = findViewById(R.id.leave_application);

        //Leave History
        leave_history_ = findViewById(R.id.leave_history_);
        recyclerView = findViewById(R.id.recyclerView);

        //Available Leave
        leave_balance = findViewById(R.id.leave_balance);
        available_leave = findViewById(R.id.available_leave);

        reporting_person = findViewById(R.id.reporting_person);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String name = user.get(sessionManager.REPORTING_PERSON);
        mUserid = user.get(sessionManager.EMP_ID);
        String uname = user.get(sessionManager.NAME);
        String profile_pic = user.get(sessionManager.PROFILE_PIC);
        String finger_id = user.get(SessionManager.FINGER_PRINT_ID);

//        username.setText(uname.toString().trim());
//        CircleImageView imageView1 = findViewById(R.id.userimage);
//        Glide.with(this).load(profile_pic).circleCrop().into(imageView1);

//        reporting_person.setText("Reporting To :- "+name.toString().trim());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        //Getting current date
        Calendar cal = Calendar.getInstance();

        //Number of Days to add
        cal.add(Calendar.DAY_OF_MONTH, 1);
        //Date after adding the days to the current date
        String newDate = sdf.format(cal.getTime());


        submit = findViewById(R.id.submit);


        permission_from_wrapper = findViewById(R.id.permission_from_wrapper);
        permission_from_to = findViewById(R.id.permission_from_to);
        date_from_wrapper = findViewById(R.id.date_from_wrapper);
        date_to_wrapper = findViewById(R.id.date_to_wrapper);
        day_sector_wrapper = findViewById(R.id.day_sector_wrapper);
        leave_type_wrapper = findViewById(R.id.leave_type_wrapper);

        eapplying_for = findViewById(R.id.applying_for);
        day_sector = findViewById(R.id.day_sector);
        leave_type = findViewById(R.id.leave_type);
        permission_from = findViewById(R.id.permission_from);
        permission_to = findViewById(R.id.permission_to);
        date_from = findViewById(R.id.date_from);
        date_to = findViewById(R.id.date_to);
        reason = findViewById(R.id.reason);

        //Leave Balance
        comp_off = findViewById(R.id.comp_off);
        el_leave = findViewById(R.id.el_leave);
        sl_leave = findViewById(R.id.sl_leave);
        cl_leave = findViewById(R.id.cl_leave);

        backbtn = findViewById(R.id.backbtn);

        leave_balance.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                if (leave_balance.isChecked()) {
                    TransitionManager.beginDelayedTransition(drawerLayout);
                    visible = !visible;
//                    available_leave.setVisibility(View.VISIBLE);
                    available_leave.setVisibility(visible ? View.VISIBLE: View.GONE);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url1,
                            response -> {
                                Log.i("Info",response);
                                JSONObject api = null;
                                try {
                                    JSONObject jsnobject = new JSONObject(response);
                                    JSONArray jsonArray = jsnobject.getJSONArray("data");
                                    for (int i=0;i<jsonArray.length();i++) {
                                        JSONObject leave = jsonArray.getJSONObject(i);

                                        String casual_leave = leave.getString("cl");
                                        String earned_leave = leave.getString("el");
                                        String sick_leave = leave.getString("sl");
                                        String com_off = leave.getString("co");
                                        cl_leave.setText(casual_leave);
                                        sl_leave.setText(sick_leave);
                                        el_leave.setText(earned_leave);
                                        comp_off.setText(com_off);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }, error ->
                            Toast.makeText(Apply_LeaveActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("finger_id", finger_id);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Apply_LeaveActivity.this);
                    requestQueue.add(stringRequest);

                }else {
//                    TransitionManager.endTransitions(drawerLayout);
//                    available_leave.setVisibility(View.GONE);
                    TransitionManager.beginDelayedTransition(drawerLayout);
                    visible = !visible;
                    available_leave.setVisibility(visible ? View.VISIBLE: View.GONE);
                }
            }
        });

        leave_apply.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                if (leave_apply.isChecked()) {
                    TransitionManager.beginDelayedTransition(drawerLayout);
                    visible = !visible;
                    leave_application.setVisibility(visible ? View.VISIBLE: View.GONE);
                }else {
                    TransitionManager.beginDelayedTransition(drawerLayout);
                    visible = !visible;
                    leave_application.setVisibility(visible ? View.VISIBLE: View.GONE);
                }
            }
        });

        mAdapter = new LeaveAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        leave_history_.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                if (leave_history_.isChecked()) {
                    TransitionManager.beginDelayedTransition(drawerLayout);
                    visible = !visible;
                    recyclerView.setVisibility(visible ? View.VISIBLE: View.GONE);
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            response -> {

                                movieList.clear();
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    String sucess = jsonObject.getString("success");
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    if (sucess.equals("1")) {

                                        if(jsonArray.length()==0) {
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        else {

                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                JSONObject object = jsonArray.getJSONObject(i);

                                                String id = object.getString("id");
                                                String applying_for = object.getString("applying_for");
                                                String leave_type = object.getString("leave_type");
                                                String from_date = object.getString("from_date");
                                                String to_date = object.getString("to_date");
                                                String status = object.getString("status");
                                                String reason = object.getString("reason");

                                                helpdesk = new Leave(id, applying_for, leave_type, from_date, to_date, status, reason);
                                                movieList.add(helpdesk);
                                                mAdapter.notifyDataSetChanged();
                                                recyclerView.scrollToPosition(movieList.size());
                                            }


                                        }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }, error -> Toast.makeText(Apply_LeaveActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()
                    ){
                        @Override
                        protected Map<String, String> getParams() {

                            Map<String,String> params = new HashMap<String,String>();
                            params.put("user_id", mUserid);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(Apply_LeaveActivity.this);
                    requestQueue.add(request);
                }else {
                    TransitionManager.beginDelayedTransition(drawerLayout);
                    visible = !visible;
                    recyclerView.setVisibility(visible ? View.VISIBLE: View.GONE);
                }
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "YYYY-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if(choosen_date==0) {
                    date_from.setText(sdf.format(myCalendar.getTime()));
                }
                else {
                    date_to.setText(sdf.format(myCalendar.getTime()));
                }

            }
        };

        date_from.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                choosen_date = 0;
                // TODO Auto-generated method stub
                new DatePickerDialog(Apply_LeaveActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        date_to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                choosen_date = 1;
                // TODO Auto-generated method stub
                new DatePickerDialog(Apply_LeaveActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        AppCompatSpinner format_select = (AppCompatSpinner) findViewById(R.id.applying_for);
        format_select.setOnItemSelectedListener(this);

        ArrayAdapter select = new ArrayAdapter(this,android.R.layout.simple_spinner_item,format_options);
        select.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        format_select.setAdapter(select);

        AppCompatSpinner sselect = (AppCompatSpinner) findViewById(R.id.day_sector);
        sselect.setOnItemSelectedListener(this);

        ArrayAdapter sector_select = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sector_options);
        sector_select.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sselect.setAdapter(sector_select);

        AppCompatSpinner leave_typeselect = (AppCompatSpinner) findViewById(R.id.leave_type);
        leave_typeselect.setOnItemSelectedListener(this);

        ArrayAdapter leave_type_select = new ArrayAdapter(this,android.R.layout.simple_spinner_item,leave_type_options);
        leave_type_select.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leave_typeselect.setAdapter(leave_type_select);

        permission_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(Apply_LeaveActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        //if (hourOfDay >= 12) {
                        //amPm = "PM";
                        //} else {
                        // amPm = "AM";
                        // }
                        permission_from.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, true);

                timePickerDialog.show();
            }
        });

        permission_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(Apply_LeaveActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        //if (hourOfDay >= 12) {
                        //amPm = "PM";
                        //} else {
                        // amPm = "AM";
                        // }
                        permission_to.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, true);

                timePickerDialog.show();
            }
        });

        eapplying_for.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!format_options[position].equals("Applying For")) {

                    if(format_options[position].equals("OD")){
                        permission_from_wrapper.setVisibility(View.GONE);
                        permission_from_to.setVisibility(View.GONE);
                        date_from_wrapper.setVisibility(View.VISIBLE);
                        date_to_wrapper.setVisibility(View.VISIBLE);
                        day_sector_wrapper.setVisibility(View.GONE);
                        leave_type_wrapper.setVisibility(View.GONE);
                    }
                    if(format_options[position].equals("Permission")){
                        permission_from_wrapper.setVisibility(View.VISIBLE);
                        permission_from_to.setVisibility(View.VISIBLE);
                        date_from_wrapper.setVisibility(View.GONE);
                        date_to_wrapper.setVisibility(View.GONE);
                        day_sector_wrapper.setVisibility(View.GONE);
                        leave_type_wrapper.setVisibility(View.GONE);
                    }
                    if(format_options[position].equals("Work From Home")){
                        permission_from_wrapper.setVisibility(View.GONE);
                        permission_from_to.setVisibility(View.GONE);
                        date_from_wrapper.setVisibility(View.VISIBLE);
                        date_to_wrapper.setVisibility(View.VISIBLE);
                        day_sector_wrapper.setVisibility(View.GONE);
                        leave_type_wrapper.setVisibility(View.GONE);
                    }
                    if(format_options[position].equals("Half Day")){
                        permission_from_wrapper.setVisibility(View.GONE);
                        permission_from_to.setVisibility(View.GONE);
                        date_from_wrapper.setVisibility(View.GONE);
                        date_to_wrapper.setVisibility(View.GONE);
                        day_sector_wrapper.setVisibility(View.VISIBLE);
                        leave_type_wrapper.setVisibility(View.VISIBLE);
                    }
                    if(format_options[position].equals("Full Day")){
                        permission_from_wrapper.setVisibility(View.GONE);
                        permission_from_to.setVisibility(View.GONE);
                        date_from_wrapper.setVisibility(View.VISIBLE);
                        date_to_wrapper.setVisibility(View.GONE);
                        day_sector_wrapper.setVisibility(View.GONE);
                        leave_type_wrapper.setVisibility(View.VISIBLE);
                    }
                    if(format_options[position].equals("More than a Day")){
                        permission_from_wrapper.setVisibility(View.GONE);
                        permission_from_to.setVisibility(View.GONE);
                        date_from_wrapper.setVisibility(View.VISIBLE);
                        date_to_wrapper.setVisibility(View.VISIBLE);
                        day_sector_wrapper.setVisibility(View.GONE);
                        leave_type_wrapper.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLeave();
            }
        });

    }

    private void submitLeave() {

        String emp_id = mUserid;
        String applying_for = eapplying_for.getSelectedItem().toString();
        String leave_types = leave_type.getSelectedItem().toString();
        String from_date = date_from.getText().toString();
        String permission_from_date = permission_from.getText().toString();
        String to_date = date_to.getText().toString();
        String permission_to_date = permission_to.getText().toString();
        String reasons = reason.getText().toString();
        String timing = day_sector.getSelectedItem().toString();

        ProgressDialog nDialog;
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Please Wait..");
        nDialog.setTitle("Submitting Data");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://eportal.newstamil.tv/leave/add.php",
                response -> {
                    Toast.makeText(Apply_LeaveActivity.this, response, Toast.LENGTH_LONG).show();
                    if(response.equalsIgnoreCase("Data Inserted")){
                        Toast.makeText(Apply_LeaveActivity.this, "Leave Applied Successfully", Toast.LENGTH_SHORT).show();
                        nDialog.dismiss();
                        finish();
                        startActivity(new Intent(getApplicationContext(), Home_PageActivity.class));

                    }
                    else{
                        Toast.makeText(Apply_LeaveActivity.this, response, Toast.LENGTH_SHORT).show();
                        nDialog.dismiss();
                    }

                }, error -> {
            Toast.makeText(Apply_LeaveActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            nDialog.dismiss();
        }

        ){
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<String,String>();

                params.put("emp_id", emp_id);
                params.put("applying_for", applying_for);
                params.put("leave_type",leave_types);
                params.put("from_date",from_date);
                params.put("to_date",to_date);
                params.put("reason",reasons);
                params.put("timing",timing);
                params.put("permission_from_date",permission_from_date);
                params.put("permission_to_date",permission_to_date);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Apply_LeaveActivity.this);
        requestQueue.add(request);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void ClickMenu(View view){
        DashboardActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        DashboardActivity.closeDrawer(drawerLayout);
    }

    public void ClickMyFeed(View view){
        DashboardActivity.redirectActivity(this,DashboardActivity.class);
    }
    public void ClickEmployeeDirectory(View view){
        DashboardActivity.redirectActivity(this,EmployeeDirectoryActivity.class);
    }

    public void ClickAttendance(View view){
        DashboardActivity.redirectActivity(this,TestingActivity.class);
//        recreate();
    }

    public void ClickLeave(View view){
        DashboardActivity.closeDrawer(drawerLayout);
//        DashboardActivity.redirectActivity(this,Apply_LeaveActivity.class);
//        recreate();
    }

    public void ClickSalary(View view){
        DashboardActivity.redirectActivity(this,PayrollActivity.class);
//        recreate();
    }

    public void ClickMyProfile(View view){
        DashboardActivity.redirectActivity(this,ProfileActivity.class);
    }

    public void ClickLogout(View view){
        logout(this);
    }

    public void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sessionManager.logout();
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DashboardActivity.closeDrawer(drawerLayout);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

}