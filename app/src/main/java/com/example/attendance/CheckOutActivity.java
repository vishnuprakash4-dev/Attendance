package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckOutActivity extends AppCompatActivity {

    TextView checked_in_time,emp_name,emp_id,emp_position,username;
    ImageView backbtn,userimage;
    Button check_out;
    SessionManager sessionManager;
    AttendanceActivity attendanceActivity;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        drawerLayout = findViewById(R.id.drawer_layout);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);

        checked_in_time = findViewById(R.id.checked_in_time);
        emp_position = findViewById(R.id.emp_position);
        emp_id = findViewById(R.id.emp_id);
        emp_name = findViewById(R.id.emp_name);
        backbtn = findViewById(R.id.backbtn);
        check_out = findViewById(R.id.check_out);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String name = user.get(sessionManager.NAME);
        String empid = user.get(sessionManager.EMP_ID);
        String position = user.get(sessionManager.POSITION);
        String profile_pic = user.get(sessionManager.PROFILE_PIC);

        emp_name.setText(name.toString().trim());
        emp_id.setText(empid.toString().trim());
        emp_position.setText(position.toString().trim());

        username.setText(name.toString().trim());
        CircleImageView imageView1 = findViewById(R.id.userimage);
        Glide.with(this).load(profile_pic).circleCrop().into(imageView1);

        CircleImageView imageView = findViewById(R.id.user_image);
        Glide.with(this).load(profile_pic).circleCrop().into(imageView);

        HashMap<String, String> checkin_time = sessionManager.getcheckintime();
        String ctime = checkin_time.get(sessionManager.CHECKIN_TIME);

        checked_in_time.setText("CheckedIn Time :- "+ctime);

//        Thread t = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    while (!isInterrupted()) {
//                        Thread.sleep(1000);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                TextView tdate = (TextView) findViewById(R.id.checked_in_time);
//                                long date = System.currentTimeMillis();
//                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
//                                String dateString = sdf.format(date);
//                                tdate.setText(dateString);
//                            }
//                        });
//                    }
//                } catch (InterruptedException e) {
//                }
//            }
//        };
//        t.start();

        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckOutActivity.this);
                builder.setTitle("CheckOut").
                        setMessage("Are you sure you want to Checkout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(getApplicationContext(),
                                        Home_PageActivity.class);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
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
        DashboardActivity.redirectActivity(this,PayrollActivity.class);
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