package com.example.sampleschoolteacher.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Eventbus.AssignmentDeleteEvent;
import com.example.sampleschoolteacher.Model.AssignmentModel;
import com.example.sampleschoolteacher.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder> {

    Context context;
    List<AssignmentModel> assignmentModelList;
    int classPos;

    public AssignmentAdapter(Context context, List<AssignmentModel> assignmentModelList, int classPos) {
        this.context = context;
        this.assignmentModelList = assignmentModelList;
        this.classPos = classPos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignmentAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_assignment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.img_assignment_delete.setVisibility(View.GONE);
        holder.txt_subject_name.setText(new StringBuilder(assignmentModelList.get(position).getSubject()));
        holder.txt_teacher_name.setText(new StringBuilder(assignmentModelList.get(position).getTeacherName()));
        holder.txt_topic.setText(new StringBuilder(assignmentModelList.get(position).getTopic()));
        holder.txt_body.setText(new StringBuilder(assignmentModelList.get(position).getBody()));
        holder.txt_date.setText(new StringBuilder(assignmentModelList.get(position).getDate()));

        if (Common.currentTeacher.getTeacherName().equals(assignmentModelList.get(position).getTeacherName()))
        {
            holder.img_assignment_delete.setVisibility(View.VISIBLE);
            holder.img_assignment_delete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Assignment");
                builder.setMessage("Do you want to delete ' "+assignmentModelList.get(position).getTopic()+" '?");
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseFirestore.getInstance().collection("Classes")
                            .document(Common.classModelList.getValue().get(classPos).getDocId())
                            .collection("Assignments")
                            .document(assignmentModelList.get(position).getAssignmentId())
                            .delete()
                            .addOnSuccessListener(unused -> dialog.dismiss())
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            });
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }

    }

    @Override
    public int getItemCount() {
        return assignmentModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        Unbinder unbinder;


        @BindView(R.id.txt_subject_name)
        TextView txt_subject_name;
        @BindView(R.id.txt_teacher_name)
        TextView txt_teacher_name;
        @BindView(R.id.txt_topic)
        TextView txt_topic;
        @BindView(R.id.txt_body)
        TextView txt_body;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.img_assignment_delete)
        ImageButton img_assignment_delete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
