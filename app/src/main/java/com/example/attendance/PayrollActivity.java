package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PayrollActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView userimage;
    TextView username,select_month;
    SessionManager sessionManager;
    Button get_data,download_payslip,download;
    DatePickerDialog.OnDateSetListener setListener;
    LinearLayout payment_details;
    ScrollView download_pdf;
    String date = "empty";
    private Bitmap bitmap;
    private String employeeid;
    private RequestQueue mQueue;
    private String paying_month;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    EnglishNumberToWords englishNumberToWords;

    Display mDisplay;
    public static final int READ_PHONE = 110;
    String path;
    String file_name = "Screenshot";
    int totalHeight;
    int totalWidth;
    File myPath;
    String imageUri;

    private static DecimalFormat df = new DecimalFormat("0.00");

    //URL for get data
    private String url = "https://eportal.newstamil.tv/attendance/payslip.php";

    //payment details
    TextView netpay_amount,earning_amount,basic_amount,hra_amount,da_payment,conveyance_amount,allowance_amount,detection_amount
            ,employee_detection,employer_detection,esi_detection,tax_detection,lop_detection;
    //pdf download
    TextView u_name,bank_name,u_emp_id,bank_acc_no,bank_ifsc,uan_no,total_earnings,take_home,emp_pf,p_tax,u_designation
            ,emp_lop,t_detuction,esi_amount,basic_earning,da_earning,hra_earning,conveyance_earning,special_earning,_branch
            ,u_department,payslip_no,pay_month,u_doj,pay_text,paying_month_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll);

        drawerLayout = findViewById(R.id.drawer_layout);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        get_data = findViewById(R.id.get_data);
        download_payslip = findViewById(R.id.download_payslip);
        payment_details = findViewById(R.id.payment_details);
        download_pdf = findViewById(R.id.download_pdf);

        //Inizilise the value of payments
        download = findViewById(R.id.download);
        netpay_amount = findViewById(R.id.netpay_amount);
        earning_amount = findViewById(R.id.earning_amount);
        basic_amount = findViewById(R.id.basic_amount);
        hra_amount = findViewById(R.id.hra_amount);
        da_payment = findViewById(R.id.da_payment);
        conveyance_amount = findViewById(R.id.conveyance_amount);
        allowance_amount = findViewById(R.id.allowance_amount);
        detection_amount = findViewById(R.id.detection_amount);
        employee_detection = findViewById(R.id.employee_detection);
        employer_detection = findViewById(R.id.employer_detection);
        esi_detection = findViewById(R.id.esi_detection);
        tax_detection = findViewById(R.id.tax_detection);
        lop_detection = findViewById(R.id.lop_detection);

        //download pdf file
        u_name = findViewById(R.id.u_name);
        bank_name = findViewById(R.id.bank_name);
        u_emp_id = findViewById(R.id.u_emp_id);
        bank_acc_no = findViewById(R.id.bank_acc_no);
        bank_ifsc = findViewById(R.id.bank_ifsc);
        uan_no = findViewById(R.id.uan_no);
        total_earnings = findViewById(R.id.total_earnings);
        take_home = findViewById(R.id.take_home);
        emp_pf = findViewById(R.id.emp_pf);
        p_tax = findViewById(R.id.p_tax);
        emp_lop = findViewById(R.id.emp_lop);
        t_detuction = findViewById(R.id.t_detuction);
        esi_amount = findViewById(R.id.esi_amount);
        basic_earning = findViewById(R.id.basic_earning);
        da_earning = findViewById(R.id.da_earning);
        hra_earning = findViewById(R.id.hra_earning);
        conveyance_earning = findViewById(R.id.conveyance_earning);
        special_earning = findViewById(R.id.special_earning);
        u_designation = findViewById(R.id.u_designation);
        _branch = findViewById(R.id._branch);
        u_department = findViewById(R.id.u_department);
        payslip_no = findViewById(R.id.payslip_no);
        pay_month = findViewById(R.id.pay_month);
        u_doj = findViewById(R.id.u_doj);
        pay_text = findViewById(R.id.pay_text);
        paying_month_head = findViewById(R.id.paying_month_head);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String uname = user.get(SessionManager.NAME);
        String profile_pic = user.get(SessionManager.PROFILE_PIC);
        String finger_print_id = user.get(SessionManager.FINGER_PRINT_ID);
        employeeid = user.get(SessionManager.EMP_ID);

//        username.setText(uname.trim());
//        CircleImageView imageView1 = findViewById(R.id.userimage);
//        Glide.with(this).load(profile_pic).circleCrop().into(imageView1);

        select_month = findViewById(R.id.select_month);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},READ_PHONE);
            }
        }

        select_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PayrollActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                if (month<10) {
                    date = year+"-"+"0"+month;
                    String selectedmonth = date;
                    if (month == 1){
                        select_month.setText("Jan -"+year);
                        paying_month = "Jan -"+year;
                    }else if (month == 2){
                        select_month.setText("Feb -"+year);
                        paying_month = "Feb -"+year;
                    }else if (month == 3){
                        select_month.setText("March -"+year);
                        paying_month = "March -"+year;
                    }else if (month == 4){
                        select_month.setText("Apr -"+year);
                        paying_month = "Apr -"+year;
                    }else if (month == 5){
                        select_month.setText("May -"+year);
                        paying_month = "May -"+year;
                    }else if (month == 6){
                        select_month.setText("June -"+year);
                        paying_month = "June -"+year;
                    }else if (month == 7){
                        select_month.setText("July -"+year);
                        paying_month = "July -"+year;
                    }else if (month == 8){
                        select_month.setText("Aug -"+year);
                        paying_month = "Aug -"+year;
                    }else if (month == 9){
                        select_month.setText("Sep -"+year);
                        paying_month = "Sep -"+year;
                    }
                }else {
                    date = year+"-"+month;
                    String selectedmonth = date;
                    if (month == 10){
                        select_month.setText("Oct-"+year);
                        paying_month = "Oct-"+year;
                    }else if (month == 11){
                        select_month.setText("Nov -"+year);
                        paying_month = "Nov -"+year;
                    }else if (month == 12){
                        select_month.setText("Dec -"+year);
                        paying_month = "Dec -"+year;
                    }
                }
            }
        };

        mQueue = Volley.newRequestQueue(this);
        get_data.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {

                if (date.equals("empty")) {
                    Toast.makeText(PayrollActivity.this, "Please Select Month...", Toast.LENGTH_SHORT).show();
                }else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            response -> {

                                Log.i("Information", response);

                                JSONObject api = null;
                                try {

                                    api = (new JSONObject(response)).getJSONObject("response");

                                    if (api.equals("No Records Found")) {
                                        Log.i("Information", "No Data Found");

                                    } else {

                                        TransitionManager.beginDelayedTransition(drawerLayout);
                                        visible = !visible;
                                        download_pdf.setVisibility(View.GONE);
                                        download.setVisibility(View.GONE);
                                        payment_details.setVisibility(visible ? View.VISIBLE : View.GONE);

                                        String net_amount = api.getString("net");
                                        Log.i("Information", net_amount);
                                        int net = Integer.parseInt(net_amount);
                                        earning_amount.setText(df.format(net));
                                        String payment = api.getString("gross");
                                        int pay = Integer.parseInt(payment);
                                        netpay_amount.setText(df.format(pay));
                                        float basic, da, hra, allowance;
                                        int conveyance = 1600;
                                        basic = net * 50 / 100;
                                        basic_amount.setText(df.format(basic));
                                        da = net * 25 / 100;
                                        da_payment.setText(df.format(da));
                                        hra = net * 10 / 100;
                                        hra_amount.setText(df.format(hra));
                                        conveyance_amount.setText(df.format(conveyance));
                                        allowance = net - (basic + da + hra + conveyance);
                                        allowance_amount.setText(df.format(allowance));
                                        int pf_emp = 1800;
                                        employee_detection.setText(df.format(pf_emp));
                                        int tax_emp = 209;
                                        tax_detection.setText(df.format(tax_emp));
                                        String lop = api.getString("lop");
                                        int lop_pay = Integer.parseInt(lop);
                                        lop_detection.setText(df.format(lop_pay));
                                        int esi = 743;
                                        esi_detection.setText(df.format(esi));
                                        int total_detuction = pf_emp + tax_emp + esi + lop_pay;
                                        detection_amount.setText(df.format(total_detuction));
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i("Information", "No Data Found...");
                                    Toast.makeText(PayrollActivity.this, "No Data Found...", Toast.LENGTH_SHORT).show();
                                    TransitionManager.beginDelayedTransition(drawerLayout);
                                    visible = !visible;
                                    download_pdf.setVisibility(View.GONE);
                                    download.setVisibility(View.GONE);
                                    payment_details.setVisibility(View.GONE);
                                }
                            }, error ->
                            Toast.makeText(PayrollActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("finger_print_id", finger_print_id);
                            params.put("paymonth", date);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(PayrollActivity.this);
                    requestQueue.add(stringRequest);
                }
            }
        });


        download_payslip.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                        response ->{

                            Log.i("Information",response);

                            JSONObject api = null;
                            try {

                                api = (new JSONObject(response)).getJSONObject("response");

                                if (api.equals("No Records Found")) {
                                    Log.i("Information","No Data Found");

                                } else {

                                    TransitionManager.beginDelayedTransition(drawerLayout);
                                    visible = !visible;
                                    payment_details.setVisibility(View.GONE);
                                    download.setVisibility(visible ? View.VISIBLE: View.GONE);
                                    download_pdf.setVisibility(visible ? View.VISIBLE: View.GONE);

                                    String name = api.getString("username");
                                    u_name.setText(name);
                                    String bankname = api.getString("bank_name");
                                    bank_name.setText(bankname);
                                    String empid = api.getString("emp_id");
                                    u_emp_id.setText(empid);
                                    String account_no = api.getString("bank_acc_no");
                                    bank_acc_no.setText(account_no);
                                    String ifsc_no = api.getString("ifsc");
                                    bank_ifsc.setText(ifsc_no);
                                    String uan_num = api.getString("uan");
                                    uan_no.setText(uan_num);
                                    String net_payment = api.getString("net");
                                    int net_pay_ment = Integer.parseInt(net_payment);
                                    total_earnings.setText(df.format(net_pay_ment));
                                    float basic, da, hra, allowance;
                                    int conveyance = 1600;
                                    basic = net_pay_ment * 50 / 100;
                                    basic_earning.setText(df.format(basic));
                                    da = net_pay_ment * 25 / 100;
                                    da_earning.setText(df.format(da));
                                    hra = net_pay_ment * 10 / 100;
                                    hra_earning.setText(df.format(hra));
                                    conveyance_earning.setText(df.format(conveyance));
                                    allowance = net_pay_ment - (basic + da + hra + conveyance);
                                    special_earning.setText(df.format(allowance));
                                    Log.i("Information", net_payment);
                                    Log.i("Information", String.valueOf(net_pay_ment));
                                    String net_pay = api.getString("gross");
                                    int gross_pay = Integer.parseInt(net_pay);
                                    String return_val_in_english = englishNumberToWords.convert(gross_pay);
                                    pay_text.setText(return_val_in_english+" Rupees Only");
                                    take_home.setText(df.format(gross_pay));
                                    int pf_emp = 1800;
                                    emp_pf.setText(df.format(pf_emp));
                                    int tax_emp = 209;
                                    p_tax.setText(df.format(tax_emp));
                                    String lop = api.getString("lop");
                                    int lop_pay = Integer.parseInt(lop);
                                    emp_lop.setText(df.format(lop_pay));
                                    int esi = 743;
                                    esi_amount.setText(df.format(esi));
                                    int total_detuction = pf_emp + tax_emp + esi + lop_pay;
                                    Log.i("Information", pf_emp + tax_emp + esi + lop);
                                    t_detuction.setText(df.format(total_detuction));
                                    //Department of user
                                    String userdepartment = api.getString("department");
                                    u_department.setText(userdepartment);
                                    String payslip_num = api.getString("payslip_number");
                                    payslip_no.setText(payslip_num);
                                    String o_branch = api.getString("branch");
                                    if (o_branch.equals("1")) {
                                        _branch.setText("Chetpet");
                                    } else {
                                        _branch.setText("Egmore");
                                    }
                                    pay_month.setText(paying_month);
                                    //Designation Set for PDF Download
                                    String userdesignation = api.getString("designation_name");
                                    u_designation.setText(userdesignation);
                                    String dojoin = api.getString("date_of_joining");
                                    u_doj.setText(dojoin);
                                    paying_month_head.setText("Payment Slip for the month of " + paying_month);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("Information","No Data Found...");
                                Toast.makeText(PayrollActivity.this, "No Data Found...", Toast.LENGTH_SHORT).show();
                                TransitionManager.beginDelayedTransition(drawerLayout);
                                visible = !visible;
                                payment_details.setVisibility(View.GONE);
                                download.setVisibility(View.GONE);
                                download_pdf.setVisibility(View.GONE);
                            }
                        },error ->
                        Toast.makeText(PayrollActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("finger_print_id", finger_print_id);
                        params.put("paymonth", date);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(PayrollActivity.this);
                requestQueue.add(stringRequest);
//                createpdf();
            }
        });

        download.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                        PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_STORAGE_CODE);

                } else {
                    Log.d("CHECKING",""+download_pdf.getMeasuredWidth()+" "+download_pdf.getMeasuredHeight());
                    bitmap = LoadBitmap(download_pdf,download_pdf.getMeasuredWidth(),download_pdf.getMeasuredHeight());
                    createpdf();
                }
            }else {

                Log.d("CHECKING",""+download_pdf.getMeasuredWidth()+" "+download_pdf.getMeasuredHeight());
                bitmap = LoadBitmap(download_pdf,download_pdf.getMeasuredWidth(),download_pdf.getMeasuredHeight());
                createpdf();
            }
        });
    }

//    private boolean appinstalledornot(String s) {
//
//        PackageManager packageManager = getPackageManager();
//        boolean app_installed;
//        try {
//            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
//            app_installed = true;
//        } catch (PackageManager.NameNotFoundException e){
//            app_installed = false;
//        }
//        return app_installed;
//    }

//    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
//        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(returnedBitmap);
//        Drawable bgDrawable = view.getBackground();
//
//        if (bgDrawable != null) {
//            bgDrawable.draw(canvas);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//        view.draw(canvas);
//        return returnedBitmap;
//    }
//
//    private void takeScreenShot(){
//        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ScreenShot/");
//        if (!folder.exists()){
//            boolean success = folder.mkdir();
//        }
//
//        path = folder.getAbsolutePath();
//        path = path+"/"+file_name+System.currentTimeMillis()+".pdf";
//
//        View u = findViewById(R.id.download_pdf);
//
//        ScrollView z = findViewById(R.id.download_pdf);
//        totalHeight = z.getChildAt(0).getHeight();
//        totalWidth = z.getChildAt(0).getWidth();
//
//        String extr = Environment.getExternalStorageDirectory()+"/PaySlip/";
//        File file = new File(extr);
//
//        if (!file.exists())
//            file.mkdir();
//        String fileName = file_name+".jpg";
//        myPath = new File(extr,fileName);
//        imageUri = myPath.getPath();
//        bitmap= getBitmapFromView(u,totalHeight,totalWidth);
//        Log.i("INFO","Hello ....");
//
//        try {
//            FileOutputStream fos = new FileOutputStream(myPath);
//            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
//            fos.flush();
//            fos.close();
//            createpdf();
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//
//    }
//
//    private void createpdf() {
//        PdfDocument document = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(),1).create();
//        PdfDocument.Page page = document.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//
//        Paint paint = new Paint();
////        paint.setColor(Color.parseColor("ffffff"));
//        canvas.drawPaint(paint);
//
//        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.getWidth(), this.bitmap.getHeight(), true);
//
//        paint.setColor(Color.BLUE);
//        canvas.drawBitmap(bitmap,0,0,null);
//        document.finishPage(page);
//        File filePath = new File(path);
//        try {
//            Log.i("INFO", String.valueOf(filePath));
//            document.writeTo(new FileOutputStream(getfilepath()));
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Somthing Wrong: "+e.toString(), Toast.LENGTH_SHORT).show();
//        }
//        document.close();
//
//        if (myPath.exists())
//            myPath.delete();
//
//        openpdf(path);
//
//    }



//    @SuppressLint("MissingSuperCall")
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
//
//        switch (requestCode) {
//            case PERMISSION_STORAGE_CODE: {
//                if (grantResults.length >0 && grantResults[0] ==
//                PackageManager.PERMISSION_GRANTED){
//                    Log.d("CHECKING",""+download_pdf.getMeasuredWidth()+" "+download_pdf.getMeasuredHeight());
//                    bitmap = LoadBitmap(download_pdf,download_pdf.getMeasuredWidth(),download_pdf.getMeasuredHeight());
//                    createpdf();
//                } else {
//                    Toast.makeText(this, "Permission Denied...!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createpdf() {
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Log.i("CHECKINGON", String.valueOf(displayMetrics));
        float width = 1080f;
        float height = 1080f;
        int convertWidth=(int)width, convertHeight=(int)height;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth,convertHeight,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth,convertHeight,true);

        canvas.drawBitmap(bitmap,0, 0,null);
        document.finishPage(page);

//        String targetpdf = "/sdcard/page.pdf";
//        File file;
//        file = new File(targetpdf);
        try {
            document.writeTo(new FileOutputStream(getfilepath()));
            document.close();
            Toast.makeText(this, "Successfully Download", Toast.LENGTH_SHORT).show();
            //Log.i("INFO", String.valueOf(file));
            openpdf();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("INFO",e.toString());
            Toast.makeText(this, "Something Wrong Please try again!"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getfilepath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File pdfDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(pdfDirectory,employeeid+"-"+paying_month+"Payslip"+".pdf");
        return file.getPath();
    }

    private void openpdf() {

        Log.i("INFO","Welcome...");
        File file = new File(getfilepath());
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intent = Intent.createChooser(target,"Open File" );
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No Apps to read PDF File", Toast.LENGTH_SHORT).show();
        }

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
//        recreate();
    }

    public void ClickLeave(View view){
        DashboardActivity.redirectActivity(this,Apply_LeaveActivity.class);
//        recreate();
    }

    public void ClickSalary(View view){
        DashboardActivity.closeDrawer(drawerLayout);
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