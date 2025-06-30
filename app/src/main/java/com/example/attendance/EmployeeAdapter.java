package com.example.attendance;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends ArrayAdapter<EmployeeDirectory>{

    Context context;
    List<EmployeeDirectory> employeelist;
    LinearLayout emp_list;

    public EmployeeAdapter(@NonNull Context context, @NonNull List<EmployeeDirectory> employeelist) {
        super(context, R.layout.emp_list, employeelist);

        this.context = context;
        this.employeelist = employeelist;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emp_list, null, true);

        CircleImageView image = view.findViewById(R.id.emp_img);
        TextView name = view.findViewById(R.id.name_emp);
        TextView emp_id = view.findViewById(R.id.id_emp);
        TextView department = view.findViewById(R.id.position_empl);
        CircleImageView e_image = view.findViewById(R.id.view_image);
        TextView name_employee = view.findViewById(R.id.name_employee);
        TextView id_employee = view.findViewById(R.id.id_employee);
        TextView emp_pos = view.findViewById(R.id.emp_pos);
        TextView emp_c_num = view.findViewById(R.id.emp_c_num);
        TextView email_emp = view.findViewById(R.id.email_emp);
        TextView emp_doj = view.findViewById(R.id.emp_doj);

        LinearLayout card_view = view.findViewById(R.id.card_view);
        CardView car_view = view.findViewById(R.id.car_view);
        LinearLayout hide_info = view.findViewById(R.id.hide_info);


        Glide.with(context).load(employeelist.get(position).getImage()).circleCrop().into(image);
        name.setText(employeelist.get(position).getName());
        emp_id.setText(employeelist.get(position).getEmp_ip());
        department.setText(employeelist.get(position).getEmp_department());

        card_view.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {

                if (hide_info.getVisibility() == View.VISIBLE){

                    TransitionManager.beginDelayedTransition(hide_info,new AutoTransition());
                    card_view.setVisibility(View.VISIBLE);
                    visible = !visible;
                    hide_info.setVisibility(visible ? View.VISIBLE: View.GONE);

                }else {

                    Glide.with(context).load(employeelist.get(position).getImage()).circleCrop().into(e_image);
                    name_employee.setText(employeelist.get(position).getName());
                    id_employee.setText(employeelist.get(position).getPosition());
                    emp_pos.setText(employeelist.get(position).getEmp_phnno());
                    emp_c_num.setText("+91 " + employeelist.get(position).getEmail_id());
                    email_emp.setText(employeelist.get(position).getEmp_ip());
                    emp_doj.setText(employeelist.get(position).getEmp_doj());

                    TransitionManager.beginDelayedTransition(hide_info, new AutoTransition());
                    card_view.setVisibility(View.VISIBLE);
                    visible = !visible;
                    hide_info.setVisibility(visible ? View.VISIBLE: View.GONE);

                }
            }
        });

        return view;
    }
}
