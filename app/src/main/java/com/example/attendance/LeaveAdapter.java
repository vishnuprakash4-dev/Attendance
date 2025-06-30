package com.example.attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.MyViewHolder> {

    private List<Leave> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ticketname, requestor, createdtime, status;

        public MyViewHolder(View view) {
            super(view);
            ticketname = (TextView) view.findViewById(R.id.personality);
            requestor = (TextView) view.findViewById(R.id.detail);
            createdtime = (TextView) view.findViewById(R.id.date_time);
            status = (TextView) view.findViewById(R.id.approval);
        }
    }


    public LeaveAdapter(List<Leave> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public LeaveAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leave, parent, false);

        return new LeaveAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LeaveAdapter.MyViewHolder holder, int position) {
        Leave movie = moviesList.get(position);
        holder.ticketname.setText(movie.getApplying_for());
        holder.requestor.setText(movie.getStatus() + "\n Reason : "+ movie.getReason());
        holder.createdtime.setText("From : "+ movie.getFrom_date() + "\n To : " + movie.getTo_date());
        holder.status.setText(movie.getLeave_type());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}