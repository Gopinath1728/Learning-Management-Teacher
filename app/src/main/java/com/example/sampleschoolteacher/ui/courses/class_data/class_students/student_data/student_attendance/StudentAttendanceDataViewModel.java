package com.example.sampleschoolteacher.ui.courses.class_data.class_students.student_data.student_attendance;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AddAttendanceModel;

import java.util.List;

public class StudentAttendanceDataViewModel extends ViewModel {
    private MutableLiveData<List<AddAttendanceModel>> mutableLiveData;

    public StudentAttendanceDataViewModel() {
    }

    public MutableLiveData<List<AddAttendanceModel>> getMutableLiveData(int position) {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.attendanceModels.get(position).getAttendance());
        return mutableLiveData;
    }
}
