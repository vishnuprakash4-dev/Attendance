package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    SessionManager sessionManager;
    TextView username;
    ImageView userimage,wish_now;
    private String profile_pic;
    private RequestQueue mQueue;
    NewsAdapter myAdapter;
    ListView listView;
    private String url= "https://eportal.newstamil.tv/attendance/feed.php";
    public static ArrayList<Newsfeed> arraylistfeeds = new ArrayList<>();
    Newsfeed newsfeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String name = user.get(SessionManager.NAME);
        profile_pic = user.get(SessionManager.PROFILE_PIC);

        listView = findViewById(R.id.myListView);
        myAdapter = new NewsAdapter(this, arraylistfeeds);
        listView.setAdapter(myAdapter);

//        username.setText(name.trim());

        CircleImageView imageView = findViewById(R.id.userimage);
        Glide.with(this).load(profile_pic).circleCrop().into(imageView);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {

            arraylistfeeds.clear();

            try {
               Log.i("INFORMATION",response);
                JSONObject jsnobject = new JSONObject(response);
                JSONArray jsonArray = jsnobject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject events = jsonArray.getJSONObject(i);

                    String name1 = events.getString("username");
                    String date_of_birth = events.getString("date_of_birth");
                    String date_of_joining = events.getString("date_of_joining");
                    String eventname = events.getString("date_of_event");
                    String phone_no = events.getString("phone");
                    String post_image = events.getString("photo");
                    newsfeed = new Newsfeed(name1,post_image,date_of_birth,date_of_joining,eventname,phone_no);
                    arraylistfeeds.add(newsfeed);
                    myAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {

        });
        queue.add(stringRequest);

    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickMyFeed(View view) {
        DashboardActivity.closeDrawer(drawerLayout);
    }

    public void ClickEmployeeDirectory(View view){
        redirectActivity(this,EmployeeDirectoryActivity.class);
    }

    public void ClickMyProfile(View view) {
        redirectActivity(this,ProfileActivity.class);
    }

    public void ClickSalary(View view) {
        redirectActivity(this,PayrollActivity.class);
    }

    public void ClickLeave(View view) {
        redirectActivity(this,Apply_LeaveActivity.class);
    }
                                        
    public void ClickAttendance(View view) {
        redirectActivity(this,TestingActivity.class);
    }

    public void ClickLogout(View view) {
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

    public static void redirectActivity(Activity activity,Class aclass) {

        Intent intent = new Intent(activity,aclass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
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