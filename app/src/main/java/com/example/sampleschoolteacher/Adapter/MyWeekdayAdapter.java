package com.example.sampleschoolteacher.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Model.TeacherTimetableModel;
import com.example.sampleschoolteacher.Model.TimetableModel;
import com.example.sampleschoolteacher.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyWeekdayAdapter extends RecyclerView.Adapter<MyWeekdayAdapter.MyViewHolder> {

    Context context;
    List<TeacherTimetableModel> timetableModelList;

    public MyWeekdayAdapter(Context context, List<TeacherTimetableModel> timetableModelList) {
        this.context = context;
        this.timetableModelList = timetableModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyWeekdayAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_class_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_class_name.setText(new StringBuilder(timetableModelList.get(position).getDayName()));
        Bundle bundle =new Bundle();
        bundle.putInt("weekPosition",position);
        holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_my_timetable,bundle));
    }

    @Override
    public int getItemCount() {
        return timetableModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Unbinder unbinder;

        @BindView(R.id.txt_class_name)
        TextView txt_class_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
        }
    }
}
