package com.example.sampleschoolteacher.ui.courses.class_data.class_students.student_data;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AttendanceModel;
import com.example.sampleschoolteacher.Model.StudentModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentStudentDataBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentDataFragment extends Fragment {



    CircleImageView img_student_data;
    TextView txt_student_data_Uid, txt_student_data_name, txt_student_data_email,
            txt_student_data_class, txt_student_data_roll, txt_student_data_address,
            txt_student_data_parent_name, txt_student_data_parent_phone;
    ImageButton btn_call_parent, btn_view_students_attendance, btn_view_students_results;

    String studentUid,subjectName;
    int studentPos;

    public static StudentDataFragment newInstance() {
        return new StudentDataFragment();
    }

    private FragmentStudentDataBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        img_student_data = binding.imgStudentData;
        txt_student_data_Uid = binding.txtStudentDataUid;
        txt_student_data_name = binding.txtStudentDataName;
        txt_student_data_email = binding.txtStudentDataEmail;
        txt_student_data_class = binding.txtStudentDataClass;
        txt_student_data_roll = binding.txtStudentDataRoll;
        txt_student_data_address = binding.txtStudentDataAddress;
        txt_student_data_parent_name = binding.txtStudentDataParentName;
        txt_student_data_parent_phone = binding.txtStudentDataParentPhone;
        btn_call_parent = binding.btnCallParent;
        btn_view_students_attendance = binding.btnViewStudentsAttendance;
        btn_view_students_results = binding.btnViewStudentsResults;

        if (getArguments() != null) {
            studentUid = getArguments().getString("studentUid");
            studentPos = getArguments().getInt("studentPos");
            subjectName = getArguments().getString("subjectName");

            StudentModel studentModel = Common.studentModelList.get(studentPos);

            if (!studentModel.getStudentImage().equalsIgnoreCase("Null"))
                Glide.with(getContext()).load(studentModel.getStudentImage()).into(img_student_data);

            txt_student_data_Uid.setText(new StringBuilder(studentModel.getStudentUid()));
            txt_student_data_name.setText(new StringBuilder(studentModel.getStudentName()));
            txt_student_data_email.setText(new StringBuilder(studentModel.getStudentEmail()));
            txt_student_data_class.setText(new StringBuilder(studentModel.getStudentClass()));
            txt_student_data_roll.setText(new StringBuilder(studentModel.getStudentRollNo()));
            txt_student_data_address.setText(new StringBuilder(studentModel.getStudentAddress()));
            txt_student_data_parent_name.setText(new StringBuilder(studentModel.getStudentParentName()));
            txt_student_data_parent_phone.setText(new StringBuilder(studentModel.getStudentParentPhone()));

            Bundle bundle = new Bundle();
            bundle.putString("studentUid",studentUid);
            bundle.putString("subjectName",subjectName);

            btn_view_students_attendance.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_student_attendance,bundle));

            btn_view_students_results.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_view_results,bundle));
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}