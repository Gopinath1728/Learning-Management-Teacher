package com.example.sampleschoolteacher.ui.courses.class_data.class_students.student_data.student_attendance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sampleschoolteacher.Adapter.AttendanceDataAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AddAttendanceModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentStudentAttendanceDataBinding;

import java.util.List;


public class StudentAttendanceDataFragment extends Fragment {

    RecyclerView recycler_attendance_data;
    AttendanceDataAdapter dataAdapter;
    TextView txt_std_no_attendance;

    int position;

    private FragmentStudentAttendanceDataBinding binding;
    private StudentAttendanceDataViewModel studentAttendanceDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        studentAttendanceDataViewModel = new ViewModelProvider(this).get(StudentAttendanceDataViewModel.class);
        binding = FragmentStudentAttendanceDataBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        recycler_attendance_data = binding.recyclerAttendanceData;
        txt_std_no_attendance = binding.txtStdNoAttendance;

        if (getArguments() != null)
        {
            position = getArguments().getInt("position");

            studentAttendanceDataViewModel.getMutableLiveData(position).observe(getViewLifecycleOwner(), addAttendanceModels -> {
                if (addAttendanceModels != null && addAttendanceModels.size()>0)
                {
                    txt_std_no_attendance.setVisibility(View.GONE);
                    dataAdapter = new AttendanceDataAdapter(getContext(),addAttendanceModels );
                    recycler_attendance_data.setAdapter(dataAdapter);
                }
            });

            recycler_attendance_data.setHasFixedSize(true);
            recycler_attendance_data.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}