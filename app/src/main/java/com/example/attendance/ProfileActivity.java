package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView username,name_user,emp_id,phn_number,email_id,position_emp,reporting_person;
    ImageView userimage;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        name_user = findViewById(R.id.name_user);
        emp_id = findViewById(R.id.emp_id);
        phn_number = findViewById(R.id.phn_number);
        email_id = findViewById(R.id.email_id);
        position_emp = findViewById(R.id.position_emp);
        reporting_person = findViewById(R.id.reporting_person);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String name = user.get(sessionManager.NAME);
        String mobile = user.get(sessionManager.MOBILE);
        String email = user.get(sessionManager.EMAIL);
        String profile_pic = user.get(sessionManager.PROFILE_PIC);
        String empid = user.get(sessionManager.EMP_ID);
        String position = user.get(sessionManager.POSITION);
        String reportingperson = user.get(sessionManager.REPORTING_PERSON);

//        username.setText(name.toString().trim());
//        CircleImageView imageView1 = findViewById(R.id.userimage);
//        Glide.with(this).load(profile_pic).circleCrop().into(imageView1);

//        name_user.setText(name.toString().trim());
//        emp_id.setText(empid.toString().trim());
//        phn_number.setText("+91 "+mobile.toString().trim());
//        email_id.setText(email.toString().trim());
//        position_emp.setText(position.toString().trim());
//        reporting_person.setText(reportingperson.toString().trim());
//        CircleImageView imageView = findViewById(R.id.profile_image);
//        Glide.with(this).load(profile_pic).circleCrop().into(imageView);

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
    }

    public void ClickLeave(View view){
        DashboardActivity.redirectActivity(this,Apply_LeaveActivity.class);
    }

    public void ClickSalary(View view){
        DashboardActivity.redirectActivity(this,PayrollActivity.class);
    }

    public void ClickMyProfile(View view){
        DashboardActivity.closeDrawer(drawerLayout);
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