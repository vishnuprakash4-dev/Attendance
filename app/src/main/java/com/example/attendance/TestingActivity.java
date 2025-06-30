package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestingActivity extends AppCompatActivity{

    private static final int REQUEST_LOCATION = 1;
    int PERMISSION_ID = 44;

    DrawerLayout drawerLayout;
    ImageView userimage;
    TextView username,starea,stlat,current_date;
    Button checked_in,checked_out;
    LinearLayout check_in,att_history;

    SessionManager sessionManager;
    FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;
    String latitude, longitude;
    ToggleButton toggleButton,attendance_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        drawerLayout = findViewById(R.id.drawer_layout);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        current_date = findViewById(R.id.current_date);

        //Attendance History
        CalendarView calendarView = findViewById(R.id.calendar_view);
        TextView selected_date = findViewById(R.id.selected_date);
        att_history = findViewById(R.id.att_history);

        toggleButton = findViewById(R.id.mark_attendance);
        attendance_history = findViewById(R.id.attendance_history);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String uname = user.get(SessionManager.NAME);
        String employee_id = user.get(SessionManager.EMP_ID);
        String profile_pic = user.get(SessionManager.PROFILE_PIC);
        String finger_print_id = user.get(SessionManager.FINGER_PRINT_ID);
        HashMap<String, String> checkin_time = sessionManager.getcheckintime();
        String ctime = checkin_time.get(SessionManager.CHECKIN_DATE);

        //set name in toolbar
//        username.setText(uname.trim());
//        CircleImageView imageView = findViewById(R.id.userimage);
//        Glide.with(this).load(profile_pic).circleCrop().into(imageView);

        //checkin & checkout area
        checked_in = findViewById(R.id.checked_in);
        checked_out = findViewById(R.id.checked_out);
        check_in = findViewById(R.id.check_in);
        starea = findViewById(R.id.starea);
        stlat = findViewById(R.id.stlat);

        //Get Current Date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd ");
        String date = simpleDate.format(calendar.getTime());

        //Running Time and Date
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                                String dateString = sdf.format(date);
                                current_date.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        //Getting Location for checkin and checkout attendance
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        }else {
            Log.i("PRAKASH","Hello World");
//            getCurrentLocation();
        }

        //Checkin and CheckOut Activity
        toggleButton.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                if (toggleButton.isChecked()) {
                    if (date.equals(ctime)) {

                        //CheckOut Activity
                        TransitionManager.beginDelayedTransition(drawerLayout);
                        visible = !visible;
                        check_in.setVisibility(visible ? View.VISIBLE: View.GONE);
                        checked_out.setVisibility(View.VISIBLE);
                        checked_in.setVisibility(View.GONE);
                        checked_out.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(TestingActivity.this);
                                builder.setTitle("CheckOut").
                                        setMessage("Are you sure you want to Checkout?");
                                builder.setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Calendar calendar = Calendar.getInstance();
                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                                                String dateTime = simpleDateFormat.format(calendar.getTime());
                                                sessionManager.setCheckinTime(dateTime,date);
                                                Log.i("INFORMATION",dateTime);

                                                String url = "https://eportal.newstamil.tv/attendance/checkin_checkout.php";
                                                StringRequest request = new StringRequest(Request.Method.POST,url,
                                                        response -> {
                                                            Log.i("INFORMATION",response);
                                                            Toast.makeText(TestingActivity.this, "Checked Out Submitted Succusfully!", Toast.LENGTH_SHORT).show();
                                                            TransitionManager.beginDelayedTransition(drawerLayout);
                                                            visible = !visible;
                                                            check_in.setVisibility(visible ? View.VISIBLE: View.GONE);
                                                        }, error -> {

                                                }
                                                ){
                                                    @Override
                                                    protected Map<String, String> getParams() {

                                                        Map<String,String> params = new HashMap<String,String>();
                                                        params.put("finger_print_id", finger_print_id);
                                                        params.put("in_out_time", dateTime);
                                                        return params;
                                                    }
                                                };
                                                RequestQueue requestQueue = Volley.newRequestQueue(TestingActivity.this);
                                                requestQueue.add(request);
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

                    }else {
                        //CheckIn Activity
                        TransitionManager.beginDelayedTransition(drawerLayout);
                        visible = !visible;
                        check_in.setVisibility(visible ? View.VISIBLE: View.GONE);
                        checked_in.setVisibility(View.VISIBLE);
                        checked_out.setVisibility(View.GONE);
                        checked_in.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd ");
                                String dateTime = simpleDateFormat.format(calendar.getTime());
                                String date = simpleDate.format(calendar.getTime());
                                sessionManager.setCheckinTime(dateTime,date);
                                Log.i("INFORMATION",dateTime);

                                String url = "https://eportal.newstamil.tv/attendance/checkin_checkout.php";
                                StringRequest request = new StringRequest(Request.Method.POST,url,
                                        response -> {
                                            Log.i("INFORMATION",response);
                                            Toast.makeText(TestingActivity.this, "Checked In Submitted Succusfully!", Toast.LENGTH_SHORT).show();
                                            TransitionManager.beginDelayedTransition(drawerLayout);
                                            visible = !visible;
                                            check_in.setVisibility(visible ? View.VISIBLE: View.GONE);
                                        }, error -> {

                                }
                                ){
                                    @Override
                                    protected Map<String, String> getParams() {

                                        Map<String,String> params = new HashMap<String,String>();
                                        params.put("finger_print_id", finger_print_id);
                                        params.put("in_out_time", dateTime);

                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(TestingActivity.this);
                                requestQueue.add(request);
                            }
                        });
                    }

                }else   {
                    TransitionManager.beginDelayedTransition(drawerLayout);
                    visible = !visible;
                    check_in.setVisibility(visible ? View.VISIBLE: View.GONE);
                }
            }
        });

        //Showing InTime and OutTime
        attendance_history.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                if (attendance_history.isChecked()) {
                    TransitionManager.beginDelayedTransition(drawerLayout);
                    visible = !visible;
                    check_in.setVisibility(View.GONE);
                    att_history.setVisibility(visible ? View.VISIBLE: View.GONE);
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

                            RequestQueue requestQueue = Volley.newRequestQueue(TestingActivity.this);
                            requestQueue.add(request);
                        }
                    });
                } else {
                    TransitionManager.beginDelayedTransition(drawerLayout);
                    visible = !visible;
                    att_history.setVisibility(visible ? View.VISIBLE: View.GONE);
                }
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
        DashboardActivity.redirectActivity(this,EmployeeDirectoryActivity.class);
    }

    public void ClickAttendance(View view){
        DashboardActivity.closeDrawer(drawerLayout);
//        recreate();
    }

    public void ClickLeave(View view){
        DashboardActivity.redirectActivity(this,Apply_LeaveActivity.class);
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

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public String getStartAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(TestingActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            String add1 = "\n" + obj.getSubLocality()+" , " + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
//            String add1 = "\n" + obj.getSubLocality();
            add = add + "\n" + obj.getSubThoroughfare();


            Log.i("VISHNU", "Address" + add1);
            starea.setText(add1.trim());
//            Toast.makeText(this, "Address" + add1, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Double lat = location.getLatitude();
                            Double lng = location.getLongitude();
                            latitude = String.valueOf(lat);
                            longitude = String.valueOf(lng);
                            stlat.setText(latitude+"-"+longitude);
                            getStartAddress(lat,lng);
                            Log.i("VISHNU","Current Loc : "+ location.getLatitude()+","+location.getLongitude());
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.i("MEDIA","Current Loc : "+ mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
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