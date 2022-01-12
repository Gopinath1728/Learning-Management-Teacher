package com.example.sampleschoolteacher.ui.courses.class_data.class_students.student_data.student_attendance;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sampleschoolteacher.Adapter.AttendanceDataAdapter;
import com.example.sampleschoolteacher.Adapter.AttendanceListAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AddAttendanceModel;
import com.example.sampleschoolteacher.Model.AttendanceModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentStudentAttendanceBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class StudentAttendanceFragment extends Fragment {

    private StudentAttendanceViewModel mViewModel;

    TextView txt_no_std_attendance;
    RecyclerView recycler_attendance;

    private FragmentStudentAttendanceBinding binding;

    public static StudentAttendanceFragment newInstance() {
        return new StudentAttendanceFragment();
    }

    String studentUid,subjectName;

    AttendanceDataAdapter dataAdapter;
    AttendanceListAdapter listAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(StudentAttendanceViewModel.class);
        binding = FragmentStudentAttendanceBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        recycler_attendance = binding.recyclerAttendance;
        txt_no_std_attendance = binding.txtNoStdAttendance;

        if (getArguments()!= null)
        {
            studentUid = getArguments().getString("studentUid");
            subjectName = getArguments().getString("subjectName");

            mViewModel.getAttendanceErrorMessage().observe(getViewLifecycleOwner(), s -> Snackbar.make(root,""+s,Snackbar.LENGTH_LONG).show());

            mViewModel.getAttendanceMutableLiveData(studentUid).observe(getViewLifecycleOwner(), attendanceModels -> {
                if (attendanceModels != null && attendanceModels.size()>0)
                {
                    txt_no_std_attendance.setVisibility(View.GONE);
                    if (Common.currentTeacher.getProctor())
                    {
                        listAdapter = new AttendanceListAdapter(getContext(),attendanceModels);
                        recycler_attendance.setAdapter(listAdapter);
                    }
                    else
                    {
                        AttendanceModel attendanceModel = new AttendanceModel();
                        for (int i=0;i<attendanceModels.size();i++)
                        {
                            if (attendanceModels.get(i).getSubjectName().equals(subjectName))
                                attendanceModel = attendanceModels.get(i);
                        }
                        dataAdapter = new AttendanceDataAdapter(getContext(),attendanceModel.getAttendance());
                        recycler_attendance.setAdapter(dataAdapter);
                    }
                }
            });

            recycler_attendance.setHasFixedSize(true);
            recycler_attendance.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}