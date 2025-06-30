package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Leave_HistoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public String mUserid;
    SessionManager sessionManager;
    ImageView backbtn,userimage;
    TextView username;

    DrawerLayout drawerLayout;

    String url = "https://eportal.newstamil.tv/attendance/myleave.php";

    private List<Leave> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LeaveAdapter mAdapter;

    Leave helpdesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);


        drawerLayout = findViewById(R.id.drawer_layout);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);

        backbtn = findViewById(R.id.backbtn);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new LeaveAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        mUserid = user.get(sessionManager.EMP_ID);
        String uname = user.get(sessionManager.NAME);
        String profile_pic = user.get(sessionManager.PROFILE_PIC);

        username.setText(uname.toString().trim());
        CircleImageView imageView1 = findViewById(R.id.userimage);
        Glide.with(this).load(profile_pic).circleCrop().into(imageView1);

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
                                    recyclerView.scrollToPosition(movieList.size() - 1);
                                }


                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Toast.makeText(Leave_HistoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<String,String>();
                params.put("user_id", mUserid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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
        DashboardActivity.redirectActivity(this,Leave_HistoryActivity.class);
    }

    public void ClickAttendance(View view){
        DashboardActivity.redirectActivity(this,HistoryActivity.class);
//        recreate();
    }

    public void ClickLeave(View view){
        DashboardActivity.closeDrawer(drawerLayout);
//        DashboardActivity.redirectActivity(this,Apply_LeaveActivity.class);
//        recreate();
    }

    public void ClickSalary(View view){
//        DashboardActivity.redirectActivity(this,Apply_LeaveActivity.class);
        Toast.makeText(this, "Working In Progress!", Toast.LENGTH_SHORT).show();
//        recreate();
    }

    public void ClickMyProfile(View view){
//        DashboardActivity.redirectActivity(this,Apply_LeaveActivity.class);
        Toast.makeText(this, "Working In Progress!", Toast.LENGTH_SHORT).show();
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