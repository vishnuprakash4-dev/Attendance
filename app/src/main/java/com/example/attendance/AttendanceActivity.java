package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    int PERMISSION_ID = 44;
    TextView dateandtime,emp_name,emp_id,emp_position;
    Button check_in;
    TextView starea,stlat,ctime,username;
    ImageView backbtn,userimage;

    FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;
    String latitude, longitude;
    SessionManager sessionManager;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        drawerLayout = findViewById(R.id.drawer_layout);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        dateandtime = findViewById(R.id.dateandtime);
        emp_name = findViewById(R.id.emp_name);
        emp_id = findViewById(R.id.emp_id);
        emp_position = findViewById(R.id.emp_position);
        backbtn = findViewById(R.id.backbtn);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String name = user.get(sessionManager.NAME);
        String empid = user.get(sessionManager.EMP_ID);
        String position = user.get(sessionManager.POSITION);
        String profile_pic = user.get(sessionManager.PROFILE_PIC);
        String finger_print_id = user.get(sessionManager.FINGER_PRINT_ID);

        emp_name.setText(name.toString().trim());
        emp_id.setText(empid.toString().trim());
        emp_position.setText(position.toString().trim());

        username.setText(name.toString().trim());
        CircleImageView imageView1 = findViewById(R.id.userimage);
        Glide.with(this).load(profile_pic).circleCrop().into(imageView1);


        CircleImageView imageView = findViewById(R.id.user_image);
        Glide.with(this).load(profile_pic).circleCrop().into(imageView);

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

        check_in = findViewById(R.id.check_in);
        starea = findViewById(R.id.starea);
        stlat = findViewById(R.id.stlat);
        ctime = findViewById(R.id.ctime);

        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            Log.i("PRAKASH","Hello World");
//            getCurrentLocation();
        }

        check_in.setOnClickListener(new View.OnClickListener() {
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

                            Intent intent = new Intent(AttendanceActivity.this,Home_PageActivity.class);
                            startActivity(intent);

                        }, error -> {

                }
                ){
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String,String> params = new HashMap<String,String>();
                        params.put("finger_print_id", finger_print_id);
//                        params.put("employee_id", employee_id);
                        params.put("in_out_time", dateTime);


                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(AttendanceActivity.this);
                requestQueue.add(request);
//                ctime.setText(dateTime);



            }
        });

//        backbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AttendanceActivity.this,Home_PageActivity.class);
//                startActivity(intent);
//            }
//        });

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
//        recreate();
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
        Geocoder geocoder = new Geocoder(AttendanceActivity.this, Locale.getDefault());
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
            starea.setText(add1.toString().trim());
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

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
//                            Toast.makeText(TripStartActivity.this, "Current Loc : "+ location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
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

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

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