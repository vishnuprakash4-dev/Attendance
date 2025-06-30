package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_PageActivity extends AppCompatActivity {

    private String profile_pic;
    private int num=1;
    TextView emp_name,dateandtime;
    LinearLayout attendance,apply_leave,attendance_history,leave_history,log_out,pay_slip;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        emp_name = findViewById(R.id.emp_name);
        dateandtime = findViewById(R.id.dateandtime);
        attendance = findViewById(R.id.attendance);
        apply_leave = findViewById(R.id.apply_leave);
        attendance_history = findViewById(R.id.attendance_history);
        leave_history = findViewById(R.id.leave_history);
        log_out = findViewById(R.id.log_out);
        pay_slip = findViewById(R.id.pay_slip);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd ");
        String date = simpleDate.format(calendar.getTime());

        sessionManager = new SessionManager(this);

        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        String name = user.get(sessionManager.NAME);
        profile_pic = user.get(sessionManager.PROFILE_PIC);

        emp_name.setText(name.toString().trim());

        CircleImageView imageView = findViewById(R.id.user_image);
        Glide.with(this).load(profile_pic).circleCrop().into(imageView);

        HashMap<String, String> checkin_time = sessionManager.getcheckintime();
        String ctime = checkin_time.get(sessionManager.CHECKIN_DATE);


//        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
//        dateandtime.setText(currentDateTimeString);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.dateandtime);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (date.equals(ctime)){
                    Intent intent = new Intent(Home_PageActivity.this,CheckOutActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Home_PageActivity.this,AttendanceActivity.class);
                    startActivity(intent);
                }
            }
        });

        apply_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Home_PageActivity.this,DashboardActivity.class);
                startActivity(intent1);
            }
        });

        attendance_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_PageActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

        leave_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_PageActivity.this,Leave_HistoryActivity.class);
                startActivity(intent);
            }
        });

        pay_slip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home_PageActivity.this, "Working In Progress!!!", Toast.LENGTH_SHORT).show();
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home_PageActivity.this);
                builder.setTitle("Logout").
                        setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sessionManager.logout();
                                Intent i = new Intent(getApplicationContext(),
                                        MainActivity.class);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sessionManager.logout();
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
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