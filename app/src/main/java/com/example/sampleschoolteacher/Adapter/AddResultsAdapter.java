package com.example.sampleschoolteacher.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sampleschoolteacher.Eventbus.AddResultEvent;
import com.example.sampleschoolteacher.Model.StudentModel;
import com.example.sampleschoolteacher.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddResultsAdapter extends RecyclerView.Adapter<AddResultsAdapter.MyViewHolder> {

    Context context;
    List<StudentModel> studentModelList;

    public AddResultsAdapter(Context context, List<StudentModel> studentModelList) {
        this.context = context;
        this.studentModelList = studentModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddResultsAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.student_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!studentModelList.get(position).getStudentImage().equalsIgnoreCase("Null"))
            Glide.with(context).load(studentModelList.get(position).getStudentImage()).into(holder.img_student);
        holder.txt_student_name.setText(new StringBuilder(studentModelList.get(position).getStudentName()));

        holder.txt_obtained_marks.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View itemView = LayoutInflater.from(context).inflate(R.layout.obtained_marks_dialog,null);
            EditText edt_obtained_marks = (EditText)itemView.findViewById(R.id.edt_obtained_marks);
            Button btn_marks_ok = (Button)itemView.findViewById(R.id.btn_marks_ok);
            builder.setView(itemView);
            AlertDialog dialog = builder.create();
            dialog.show();

            btn_marks_ok.setOnClickListener(v1 -> {
                if (!edt_obtained_marks.getText().toString().isEmpty()){
                    EventBus.getDefault().post(new AddResultEvent(studentModelList.get(position).getUid(),
                            holder.edt_total_marks.getText().toString(),
                            edt_obtained_marks.getText().toString()));
                    dialog.dismiss();
                }
                else
                    edt_obtained_marks.setError("Field can't be empty !");
            });
        });
    }

    @Override
    public int getItemCount() {
        return studentModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Unbinder unbinder;

        @BindView(R.id.img_student)
        CircleImageView img_student;
        @BindView(R.id.txt_student_name)
        TextView txt_student_name;
        @BindView(R.id.edt_total_marks)
        EditText edt_total_marks;
        @BindView(R.id.txt_obtained_marks)
        TextView txt_obtained_marks;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
