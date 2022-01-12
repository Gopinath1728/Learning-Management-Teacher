package com.example.sampleschoolteacher.ui.timetable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.TeacherTimetableModel;

import java.util.List;

public class WeekdayViewModel extends ViewModel {
    private MutableLiveData<List<TeacherTimetableModel>> mutableLiveData;

    public WeekdayViewModel() {
    }

    public MutableLiveData<List<TeacherTimetableModel>> getMutableLiveData() {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.currentTeacher.getTimetable());
        return mutableLiveData;
    }
}
