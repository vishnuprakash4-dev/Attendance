package com.example.attendance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsAdapter extends ArrayAdapter<Newsfeed> {

    Context context;
    List<Newsfeed> arrayListCourtdiary;

    public NewsAdapter(@NonNull Context context, List<Newsfeed> arrayListCourtDiary) {
        super(context, R.layout.feed_list, arrayListCourtDiary);

        this.context = context;
        this.arrayListCourtdiary = arrayListCourtDiary;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list, null, true);

        TextView datetime = view.findViewById(R.id.datetime);
        TextView wish_msg = view.findViewById(R.id.wish_msg);
        TextView wish_name = view.findViewById(R.id.wish_name);
        TextView wish_person = view.findViewById(R.id.wish_person);
        CircleImageView post_image = view.findViewById(R.id.post_image);
        ImageView baloon1 = view.findViewById(R.id.baloon1);
        ImageView baloon2 = view.findViewById(R.id.baloon2);
        ImageView joinee = view.findViewById(R.id.joinee);
        ImageView joinee1 = view.findViewById(R.id.joinee1);
        LinearLayout wishing_now = view.findViewById(R.id.wishing_now);

        String event = arrayListCourtdiary.get(position).geteventname();
        if (event.equals("birthday")){
//            String imageView1 = arrayListCourtdiary.get(position).getPost_image();
            baloon1.setVisibility(View.VISIBLE);
            baloon2.setVisibility(View.VISIBLE);
            wishing_now.setVisibility(View.VISIBLE);
            joinee.setVisibility(View.GONE);
            joinee1.setVisibility(View.GONE);
            wish_name.setText(" Happy BirthDay ");
            String number = arrayListCourtdiary.get(position).getPhone();
            datetime.setText(arrayListCourtdiary.get(position).getDate_of_birth());
            wish_msg.setText("Happy Birthday "+arrayListCourtdiary.get(position).getName()+"\n"+"We Wish You Have A Great Year Ahead!");
            wish_person.setText(arrayListCourtdiary.get(position).getName());
            String birthday_person = arrayListCourtdiary.get(position).getName();
//            Picasso.with(context).load(arrayListCourtdiary.get(position).getPost_image()).into(post_image);
            Glide.with(context).load(arrayListCourtdiary.get(position).getPost_image()).circleCrop().into(post_image);

            wishing_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PackageManager pm = context.getPackageManager();
                    try {
                        PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=+91"+number+"&text=Wish you Many More happy returns of The Day "+birthday_person));
                    context.startActivity(intent);

                }
            });
        }
        else if (event.equals("joining")){
            baloon1.setVisibility(View.GONE);
            baloon2.setVisibility(View.GONE);
            joinee.setVisibility(View.VISIBLE);
            joinee1.setVisibility(View.VISIBLE);
            wish_name.setText("  New Joinee");
            datetime.setText(arrayListCourtdiary.get(position).getdate_of_joining());
            wish_msg.setText(arrayListCourtdiary.get(position).getName()+" Has Joined Us In The Company On "+arrayListCourtdiary.get(position).getdate_of_joining()+", Please Join Us In Welcoming Our Newest Team Member.");
            wish_person.setText(arrayListCourtdiary.get(position).getName()+" Just Joined US!");
//            Picasso.with(context).load(arrayListCourtdiary.get(position).getPost_image()).into(post_image);
            Glide.with(context).load(arrayListCourtdiary.get(position).getPost_image()).circleCrop().into(post_image);
        }

//        tvName.setText();

        //tvid.setText(arrayListCourtdiary.get(position).getEnd_time());
//        tvName.setText("Vehicle No : " +arrayListCourtdiary.get(position).getVehicle_no()+ "\nPurpose : " + arrayListCourtdiary.get(position).getPurpose() +"\nPerson : " + arrayListCourtdiary.get(position).getPerson() +"\nStart Location : " +arrayListCourtdiary.get(position).getStart_location()+ "\nStart KM : " + arrayListCourtdiary.get(position).getStart_km()+ " KM " +"\nStart Time : " +arrayListCourtdiary.get(position).getStart_time()+"\nEnd KM : " + arrayListCourtdiary.get(position).getEnd_km()+" KM "+"\nEnd Time : " +arrayListCourtdiary.get(position).getEnd_time()+"\nEnd Location : " +arrayListCourtdiary.get(position).getEnd_location());
//        //tvStatus.setText("Vehicle No : " + arrayListCourtdiary.get(position).getVehicle_no());

        return view;
    }

}
