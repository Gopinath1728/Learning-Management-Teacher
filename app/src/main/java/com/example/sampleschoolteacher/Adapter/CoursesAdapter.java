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

import com.example.sampleschoolteacher.Callback.IRecyclerClickListener;
import com.example.sampleschoolteacher.Model.TeachingClassModel;
import com.example.sampleschoolteacher.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.MyViewHolder> {

    Context context;
    List<TeachingClassModel> teachingClassModelList;

    public CoursesAdapter(Context context, List<TeachingClassModel> teachingClassModelList) {
        this.context = context;
        this.teachingClassModelList = teachingClassModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.subject_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_class.setText(new StringBuilder(teachingClassModelList.get(position).getClassName()));
        holder.txt_subject_name.setText(new StringBuilder(teachingClassModelList.get(position).getSubjectName()));
        Bundle bundle = new Bundle();
        bundle.putInt("classPos",position);
        bundle.putString("subjectName",teachingClassModelList.get(position).getSubjectName());
        holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_class_data,bundle));
    }

    @Override
    public int getItemCount() {
        return teachingClassModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Unbinder unbinder;

        @BindView(R.id.txt_subject_name)
        TextView txt_subject_name;
        @BindView(R.id.txt_class)
        TextView txt_class;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (teachingClassModelList.size() == 1)
            return 0;
        else
        {
            if (teachingClassModelList.size() % 2 == 0)
                return 0;
            else
                return (position > 1 && position == teachingClassModelList.size()-1) ? 1:0;
        }

    }
}
