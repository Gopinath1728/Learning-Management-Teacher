package com.example.sampleschoolteacher.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.ExaminationModel;
import com.example.sampleschoolteacher.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ExaminationAdapter extends RecyclerView.Adapter<ExaminationAdapter.MyViewHolder> {

    Context context;
    List<ExaminationModel> examinationModelList;
    String subject,className;

    public ExaminationAdapter(Context context, List<ExaminationModel> examinationModelList, String subject, String className) {
        this.context = context;
        this.examinationModelList = examinationModelList;
        this.subject = subject;
        this.className = className;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExaminationAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_exam_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_exam_subject.setText(new StringBuilder(examinationModelList.get(position).getExamSbjtName()));
        holder.txt_exam_date.setText(new StringBuilder(examinationModelList.get(position).getExamDate()));
        holder.txt_exam_timeFrom.setText(new StringBuilder(examinationModelList.get(position).getExamTimeFrom()));
        holder.txt_exam_timeTo.setText(new StringBuilder(examinationModelList.get(position).getExamTimeTo()));
        holder.txt_exam_venue.setText(new StringBuilder(examinationModelList.get(position).getExamRoom()));
        holder.txt_exam_type.setText(new StringBuilder(examinationModelList.get(position).getExamType()));


        if(subject.equals(examinationModelList.get(position).getExamSbjtName()))
        {
            for (int i=0;i<Common.currentTeacher.getTeachingClasses().size();i++)
            {
                if (className.equals(Common.currentTeacher.getTeachingClasses().get(i).getClassName()))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("subjectName",subject);
                    bundle.putString("className",className);
                    bundle.putString("examType",examinationModelList.get(position).getExamType());
                    holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_add_results,bundle));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return examinationModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Unbinder unbinder;

        @BindView(R.id.txt_exam_subject)
        TextView txt_exam_subject;
        @BindView(R.id.txt_exam_date)
        TextView txt_exam_date;
        @BindView(R.id.txt_exam_venue)
        TextView txt_exam_venue;
        @BindView(R.id.txt_exam_timeFrom)
        TextView txt_exam_timeFrom;
        @BindView(R.id.txt_exam_timeTo)
        TextView txt_exam_timeTo;
        @BindView(R.id.txt_exam_type)
        TextView txt_exam_type;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (examinationModelList.size() == 1)
            return 0;
        else
        {
            if (examinationModelList.size() % 2 == 0)
                return 0;
            else
                return (position > 1 && position == examinationModelList.size()-1) ? 1:0;
        }

    }
}
