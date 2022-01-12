package com.example.sampleschoolteacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Model.TeacherTimeModel;
import com.example.sampleschoolteacher.Model.TimeModel;
import com.example.sampleschoolteacher.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TeacherTimeViewAdapter extends RecyclerView.Adapter<TeacherTimeViewAdapter.MyViewHolder> {

    Context context;
    List<TeacherTimeModel> timeModelList;

    public TeacherTimeViewAdapter(Context context, List<TeacherTimeModel> timeModelList) {
        this.context = context;
        this.timeModelList = timeModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.teacher_timetable_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_subject_name.setText(new StringBuilder(timeModelList.get(position).getSubjName()));
        holder.txt_time_from.setText(new StringBuilder(timeModelList.get(position).getTimeFrom()));
        holder.txt_time_to.setText(new StringBuilder(timeModelList.get(position).getTimeTo()));
        holder.txt_ttbl_class_name.setText(new StringBuilder(timeModelList.get(position).getClassName()));
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
        @BindView(R.id.txt_ttbl_class_name)
        TextView txt_ttbl_class_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (timeModelList.size() == 1)
            return 0;
        else
        {
            if (timeModelList.size() % 2 == 0)
                return 0;
            else
                return (position > 1 && position == timeModelList.size()-1) ? 1:0;
        }

    }
}
