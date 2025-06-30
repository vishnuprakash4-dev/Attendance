  package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText inputEmail,inputpassword;
    Button buttonSignIn;
    String s1;
    String s2;
    String token;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        Boolean res = sessionManager.isLoggin();

        if(res==true) {
            Intent intent1 = new Intent(MainActivity.this,DashboardActivity.class);
            startActivity(intent1);
        }
        Date currentTime = Calendar.getInstance().getTime();
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputpassword = (EditText) findViewById(R.id.inputpassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Info", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("Info", token);
                    }
                });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);

                if (TextUtils.isEmpty(inputEmail.getText()) && TextUtils.isEmpty(inputpassword.getText())) {
                    inputEmail.setError(" Please Enter mandatory fields!!!");
                    inputpassword.setError(" Please Enter mandatory fields!!!");
                }
                else {

                    ProgressDialog nDialog;
                    nDialog = new ProgressDialog(MainActivity.this);
                    nDialog.setMessage("Please Wait");
                    nDialog.setTitle("Logging In...");
                    nDialog.setIndeterminate(false);
                    nDialog.setCancelable(true);
                    nDialog.show();
                    //proceed with operation
                    s1 = inputEmail.getText().toString();
                    s2 = inputpassword.getText().toString();
                    String url = getString(R.string.api_url)+"login";
                    Log.i("Info",url);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            response -> {
                        Log.i("Information",response);
                                JSONObject api = null;
                                try {

                                    api = (new JSONObject(response)).getJSONObject("response");

                                    String api_status = api.getString("error");

                                    String message = api.getString("message");

                                    if (api_status.equals("false")) {

                                        String name = api.getString("name");

                                        String user_id = api.getString("user_id");

                                        String email = api.getString("email");

                                        String mobile = api.getString("mobile");

//                                        String user_type = api.getString("user_type");

//                                        String districts = api.getString("districts");

                                        String profile_pic = api.getString("profile_picture");

                                        String emp = api.getString("emp_id");

                                        String finger_print_id = api.getString("finger_print_id");

                                        String position = api.getString("position");

                                        String reporting_name = api.getString("reporting");

                                        String remote_attendance = api.getString("remote_attendance");

                                        sessionManager.createSession(name, email, mobile, user_id, profile_pic, emp, position, reporting_name, finger_print_id, remote_attendance);

                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                                        Intent intent1 = new Intent(MainActivity.this, DashboardActivity.class);
                                        nDialog.dismiss();
                                        startActivity(intent1);
                                    }else {
                                        nDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Invalid Username/password!", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                            error ->
                                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_name", s1);
                            params.put("user_password", s2);
//                            params.put("token", "NA");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
    }

    @Override
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