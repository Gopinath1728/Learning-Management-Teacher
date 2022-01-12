package com.example.sampleschoolteacher.ui.timetable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.TeacherTimeModel;

import java.util.List;

public class MyTimetableViewModel extends ViewModel {
    private MutableLiveData<List<TeacherTimeModel>> mutableLiveData;

    public MyTimetableViewModel() {
    }

    public MutableLiveData<List<TeacherTimeModel>> getMutableLiveData(int position) {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.currentTeacher.getTimetable().get(position).getTime());
        return mutableLiveData;
    }
}
