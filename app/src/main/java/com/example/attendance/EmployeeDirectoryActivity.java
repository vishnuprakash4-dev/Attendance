package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeDirectoryActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView username;
    ImageView userimage;
    SessionManager sessionManager;
    LinearLayout scroll_view,employee_list,search_view;
    private RequestQueue mQueue;
    EmployeeAdapter myAdapter;
    FloatingActionButton back_to_top;
    ListView listView;
    public static ArrayList<EmployeeDirectory> arraylistemployees = new ArrayList<>();
//    private String url= "https://eportal.newstamil.tv/attendance/employee_directory.php";
    EmployeeDirectory employeeDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_directory);

        drawerLayout = findViewById(R.id.drawer_layout);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);

        back_to_top = findViewById(R.id.back_to_top);
        scroll_view = findViewById(R.id.scroll_view);

        listView = findViewById(R.id.myListView);

        myAdapter = new EmployeeAdapter(this, arraylistemployees);
        listView.setAdapter(myAdapter);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String name = user.get(sessionManager.NAME);
        String profile_pic = user.get(sessionManager.PROFILE_PIC);

//        username.setText(name.toString().trim());
//        CircleImageView imageView1 = findViewById(R.id.userimage);
//        Glide.with(this).load(profile_pic).circleCrop().into(imageView1);

        back_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.api_url)+"directory";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("INFORMATION",response);
//                    JSONArray jsonArray = new JSONArray(response);
                    JSONArray jsnobject = new JSONArray(response);
                    JSONArray jsonArray = jsnobject;
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject events = jsonArray.getJSONObject(i);

                        String name = events.getString("emp_name");
                        String emp_ip = events.getString("emp_id");
                        String image = events.getString("photo");
                        String email_id = events.getString("email");
                        String emp_phnno = events.getString("phone");
                        String emp_doj = events.getString("date_of_joining");
                        String emp_department = events.getString("department_name");
                        String position = events.getString("designation_name");

                        employeeDirectory = new EmployeeDirectory(name,emp_ip,image,email_id,emp_phnno,emp_doj,emp_department,position);
                        arraylistemployees.add(employeeDirectory);
                        myAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

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
        DashboardActivity.closeDrawer(drawerLayout);
    }

    public void ClickAttendance(View view){
        DashboardActivity.redirectActivity(this,TestingActivity.class);
//        recreate();
    }

    public void ClickLeave(View view){
        DashboardActivity.redirectActivity(this,Apply_LeaveActivity.class);
//        recreate();
    }

    public void ClickSalary(View view){
        DashboardActivity.redirectActivity(this,PayrollActivity.class);
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
}