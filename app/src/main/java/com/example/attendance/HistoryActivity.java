package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryActivity extends AppCompatActivity {

    SessionManager sessionManager;
    ImageView backbtn,userimage;
    DrawerLayout drawerLayout;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        drawerLayout = findViewById(R.id.drawer_layout);
        CalendarView calendarView = findViewById(R.id.calendar_view);
        TextView selected_date = findViewById(R.id.selected_date);
        backbtn = findViewById(R.id.backbtn);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String finger_print_id = user.get(sessionManager.FINGER_PRINT_ID);
        String employee_id = user.get(sessionManager.EMP_ID);
        String name = user.get(sessionManager.NAME);
        String profile_pic = user.get(sessionManager.PROFILE_PIC);

        username.setText(name.toString().trim());
        CircleImageView imageView1 = findViewById(R.id.userimage);
        Glide.with(this).load(profile_pic).circleCrop().into(imageView1);

//        Log.i("INFORMATION",employee_id);
//        Log.i("INFORMATION",finger_print_id);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                month=month+1;
                String attendance_date;
                if (dayOfMonth<10 && month<10){
                    attendance_date = year+"-"+"0"+month+"-"+"0"+dayOfMonth;
                }else if (dayOfMonth<10 && month>=10){
                    attendance_date = year+"-"+month+"-"+"0"+dayOfMonth;
                }else if (dayOfMonth>=10 && month<10){
                    attendance_date = year+"-"+"0"+month+"-"+dayOfMonth;
                }else {
                    attendance_date = year + "-" + month + "-" + dayOfMonth;
                }

                Log.i("INFORMATION",attendance_date);

                String url = "https://eportal.newstamil.tv/attendance/attendancereport.php";
                StringRequest request = new StringRequest(Request.Method.POST,url,
                        response -> {
                            Log.i("INFORMATION",response);

                            selected_date.setText(Html.fromHtml(response));
//                            AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
//                            nDialog.dismiss();
//                            builder.setTitle("Attendance")
//                                    .setMessage(Html.fromHtml(response))
//                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                        }
//                                    });
//                            AlertDialog alert11 = builder.create();
//                            alert11.show();

                        }, error -> {

                }
                ){
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String,String> params = new HashMap<String,String>();
                        params.put("finger_print_id", finger_print_id);
                        params.put("employee_id", employee_id);
                        params.put("attendance_date", attendance_date);

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(HistoryActivity.this);
                requestQueue.add(request);
            }
        });
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
//        DashboardActivity.redirectActivity(this,EmployeeDirectoryActivity.class);
        Toast.makeText(this, "Working In Progress!", Toast.LENGTH_SHORT).show();
    }

    public void ClickAttendance(View view){
        DashboardActivity.closeDrawer(drawerLayout);
//        DashboardActivity.redirectActivity(this,Leave_HistoryActivity.class);
//        recreate();
    }

    public void ClickLeave(View view){
        DashboardActivity.redirectActivity(this,Apply_LeaveActivity.class);
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