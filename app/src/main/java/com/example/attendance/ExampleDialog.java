//package com.example.attendance;
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatDialogFragment;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.HashMap;
//
//public class ExampleDialog extends AppCompatDialogFragment {
//
//    CalendarActivity calendarActivity;
//    SessionManager sessionManager;
//
//    @NonNull
//    @NotNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
////
////        HashMap<String, String> selecteddate = sessionManager.getselecteddate();
////        String ctime = selecteddate.get(sessionManager.SELECT_DATE);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Attendance Tracking!!!")
//                .setMessage("Popup :")
//                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        return builder.create();
//
//    }
//}
