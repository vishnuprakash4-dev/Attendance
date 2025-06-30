package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE=0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USER_ID = "USER_ID";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String MOBILE = "MOBILE";
    public static final String DISTRICT = "DISTRICT";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String PROFILE_PIC = "PROFILE_PIC";
    public static final String EMP_ID = "EMP_ID";
    public static final String POSITION = "POSITION";
    public static final String REPORTING_PERSON = "REPORTING_PERSON";
    public static final String LOG_DATE = "LOG_DATE";
    public static final String FINGER_PRINT_ID = "FINGER_PRINT_ID";
    public static final String REMOTE_ATTENDANCE = "REMOTE_ATTENDANCE";

    public static final String ST_LOC = "ST_LOC";
    public static final String ET_LOC = "ET_LOC";

    public static final String FILTER_DATE = "FILTER_DATE";

    public static final String CHECKING = "CHECKING";


    public static final String CHECKIN_TIME = "CHECKIN_TIME";
    public static final String CHECKIN_DATE = "CHECKIN_DATE";

    public static final String SELECT_DATE = "SELECT_DATE";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLoggin()){
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            ((DashboardActivity) context).finish();
        }
    }

    public void saveFilterdate(String fDate) {

        editor.putString(FILTER_DATE, fDate);
        editor.apply();

    }

    public void createSession(String name, String email, String mobile, String user_id, String profile_pic, String emp, String position, String reporting_name, String finger_print_id, String remote_attendance){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(MOBILE, mobile);
//        editor.putString(USER_TYPE, user_type);
        editor.putString(USER_ID, user_id);
//        editor.putString(DISTRICT, districts);
        editor.putString(REPORTING_PERSON, reporting_name);
        editor.putString(POSITION, position);
        editor.putString(PROFILE_PIC, profile_pic);
        editor.putString(EMP_ID, emp);
        editor.putString(FINGER_PRINT_ID, finger_print_id);
        editor.putString(REMOTE_ATTENDANCE, remote_attendance);
//        editor.putString(LOG_DATE, last_date);
        editor.apply();
    }

    public void setStartLocation(String location) {
        editor.putString(ST_LOC, location);
        editor.apply();
    }

    public void setCheckinTime(String dateTime, String date) {
        editor.putString(CHECKIN_TIME, dateTime);
        editor.putString(CHECKIN_DATE, date);
        editor.apply();
    }

    public void setselectdates(String message) {
        editor.putString(SELECT_DATE, message);
        editor.apply();
    }

    public void setEndLocation(String location) {
        editor.putString(ET_LOC, location);
        editor.apply();
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(MOBILE, sharedPreferences.getString(MOBILE, null));
        user.put(USER_TYPE, sharedPreferences.getString(USER_TYPE, null));
        user.put(USER_ID, sharedPreferences.getString(USER_ID, null));
        user.put(DISTRICT, sharedPreferences.getString(DISTRICT, null));
        user.put(REPORTING_PERSON, sharedPreferences.getString(REPORTING_PERSON, null));
        user.put(POSITION, sharedPreferences.getString(POSITION, null));
        user.put(PROFILE_PIC, sharedPreferences.getString(PROFILE_PIC, null));
        user.put(EMP_ID, sharedPreferences.getString(EMP_ID, null));
        user.put(FINGER_PRINT_ID, sharedPreferences.getString(FINGER_PRINT_ID, null));
        user.put(REMOTE_ATTENDANCE, sharedPreferences.getString(REMOTE_ATTENDANCE, null));
//        user.put(LOG_DATE, sharedPreferences.getString(LOG_DATE, null));
        return user;
    }

    public HashMap<String, String > getStartLoc(){
        HashMap<String, String> start = new HashMap<>();
        start.put(ST_LOC, sharedPreferences.getString(ST_LOC,null));
        return start;
    }

    public HashMap<String, String> getEndLoc(){
        HashMap<String, String> end = new HashMap<>();
        end.put(ET_LOC, sharedPreferences.getString(ET_LOC,null));
        return end;
    }

    public HashMap<String, String> getcheckintime(){
        HashMap<String, String> checkin_time = new HashMap<>();
        checkin_time.put(CHECKIN_TIME, sharedPreferences.getString(CHECKIN_TIME,null));
        checkin_time.put(CHECKIN_DATE, sharedPreferences.getString(CHECKIN_DATE,null));
        return checkin_time;
    }

    public HashMap<String, String> getselecteddate(){
        HashMap<String, String> selecteddate = new HashMap<>();
        selecteddate.put(SELECT_DATE, sharedPreferences.getString(SELECT_DATE,null));
        return selecteddate;
    }

    public HashMap<String, String> getFilterDate(){
        HashMap<String, String> filterdate = new HashMap<>();
        filterdate.put(FILTER_DATE, sharedPreferences.getString(FILTER_DATE, null));
        return filterdate;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
        ((Home_PageActivity)context).finish();
    }
}
