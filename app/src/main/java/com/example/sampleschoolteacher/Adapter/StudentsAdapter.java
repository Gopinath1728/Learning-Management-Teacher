package com.example.sampleschoolteacher.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Eventbus.SendMessageEvent;
import com.example.sampleschoolteacher.Eventbus.StudentAttendanceEvent;
import com.example.sampleschoolteacher.Model.MessageModel;
import com.example.sampleschoolteacher.Model.StudentModel;
import com.example.sampleschoolteacher.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {

    Context context;
    List<StudentModel> studentModelList;
    Boolean currentClass;
    String subjectName;

    public StudentsAdapter(Context context, List<StudentModel> studentModelList, Boolean currentClass, String subjectName) {
        this.context = context;
        this.studentModelList = studentModelList;
        this.currentClass = currentClass;
        this.subjectName = subjectName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentsAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.student_item_view, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!studentModelList.get(position).getStudentImage().equalsIgnoreCase("Null"))
            Glide.with(context).load(studentModelList.get(position).getStudentImage()).into(holder.img_student);
        holder.txt_student_name.setText(new StringBuilder(studentModelList.get(position).getStudentName()));
        if (studentModelList.get(position).getClassMonitor())
            holder.img_monitor_label.setVisibility(View.VISIBLE);

        if (currentClass)
        {
            holder.linear_attendance.setVisibility(View.VISIBLE);
            holder.switch_attendance.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isChecked)
                    EventBus.getDefault().post(new StudentAttendanceEvent(studentModelList.get(position).getUid(), "Absent"));
                else
                    EventBus.getDefault().post(new StudentAttendanceEvent(studentModelList.get(position).getUid(), "Present"));
            });
        }

        Bundle bundle = new Bundle();
        bundle.putString("studentUid",studentModelList.get(position).getUid());
        bundle.putInt("studentPos",position);
        bundle.putString("subjectName",subjectName);
        holder.img_profile_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_student_data,bundle));

        holder.img_send_msg_button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View itemView = LayoutInflater.from(context).inflate(R.layout.send_message_dialog,null);
            TextView txt_message_from = (TextView) itemView.findViewById(R.id.txt_message_from);
            EditText edt_message_body = (EditText) itemView.findViewById(R.id.edt_message_body);
            EditText edt_message_title = (EditText) itemView.findViewById(R.id.edt_message_title);
            Button btn_message_cancel = (Button)itemView.findViewById(R.id.btn_message_cancel);
            Button btn_message_send = (Button)itemView.findViewById(R.id.btn_message_send);
            builder.setView(itemView);
            AlertDialog dialog = builder.create();
            dialog.show();

            btn_message_cancel.setOnClickListener(v1 -> dialog.dismiss());

            btn_message_send.setOnClickListener(v12 -> {
                txt_message_from.setText(Common.currentTeacher.getTeacherName());
                MessageModel messageModel = new MessageModel();
                messageModel.setTitle(edt_message_title.getText().toString().trim());
                messageModel.setFrom(Common.currentTeacher.getTeacherName());
                messageModel.setBody(edt_message_body.getText().toString().trim());
                messageModel.setDate(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
                messageModel.setTime(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date()));
                messageModel.setMsgId(FirebaseFirestore.getInstance()
                        .collection("Students")
                        .document(studentModelList.get(position).getUid())
                        .collection("Messages")
                        .document().getId());
                FirebaseFirestore.getInstance()
                        .collection("Students")
                        .document(studentModelList.get(position).getUid())
                        .collection("Messages")
                        .document(messageModel.getMsgId())
                        .set(messageModel)
                        .addOnSuccessListener(unused -> EventBus.getDefault().post(new SendMessageEvent(edt_message_title.getText().toString().trim(),edt_message_body.getText().toString().trim(), studentModelList.get(position).getStudentToken())))
                        .addOnFailureListener(e -> Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
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
        @BindView(R.id.img_monitor_label)
        ImageView img_monitor_label;
        @BindView(R.id.switch_attendance)
        SwitchCompat switch_attendance;
        @BindView(R.id.linear_attendance)
        LinearLayout linear_attendance;
        @BindView(R.id.img_profile_button)
        ImageButton img_profile_button;
        @BindView(R.id.img_send_msg_button)
        ImageButton img_send_msg_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
