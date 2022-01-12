package com.example.sampleschoolteacher.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.TeacherTimeModel;
import com.example.sampleschoolteacher.Model.TimeModel;
import com.example.sampleschoolteacher.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.MyViewHolder> {

    Context context;
    List<TeacherTimeModel> timeModelList;

    public HomeFragmentAdapter(Context context, List<TeacherTimeModel> timeModelList) {
        this.context = context;
        this.timeModelList = timeModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.home_timetable_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_subject_name.setText(new StringBuilder(timeModelList.get(position).getSubjName()));
        holder.txt_time_from.setText(new StringBuilder(timeModelList.get(position).getTimeFrom()));
        holder.txt_time_to.setText(new StringBuilder(timeModelList.get(position).getTimeTo()));
        holder.txt_tcr_class_name.setText(new StringBuilder(timeModelList.get(position).getClassName()));


        String currentTime = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date());
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy hh:mm a",Locale.getDefault());
        try {
            Date timeFrom = parser.parse((new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date())) + " " + timeModelList.get(position).getTimeFrom());
            Date timeTo = parser.parse((new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date())) + " " + timeModelList.get(position).getTimeTo());
            if (parser.parse(currentTime).after(timeFrom) && parser.parse(currentTime).before(timeTo))
            {
                holder.home_item_cardView.setCardBackgroundColor(Color.parseColor("#00b4d8"));
                Bundle bundle = new Bundle();
                bundle.putString("className",timeModelList.get(position).getClassName());
                bundle.putString("subjectName",timeModelList.get(position).getSubjName());
                bundle.putString("timeFrom",timeModelList.get(position).getTimeFrom());
                bundle.putString("timeTo",timeModelList.get(position).getTimeTo());
                holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_online_class,bundle));
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }





    }

    @Override
    public int getItemCount() {
        return timeModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Unbinder unbinder;

        @BindView(R.id.txt_subject_name)
        TextView txt_subject_name;
        @BindView(R.id.txt_time_from)
        TextView txt_time_from;
        @BindView(R.id.txt_time_to)
        TextView txt_time_to;
        @BindView(R.id.txt_subject_status)
        TextView txt_subject_status;
        @BindView(R.id.home_item_cardView)
        CardView home_item_cardView;
        @BindView(R.id.txt_tcr_class_name)
        TextView txt_tcr_class_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
