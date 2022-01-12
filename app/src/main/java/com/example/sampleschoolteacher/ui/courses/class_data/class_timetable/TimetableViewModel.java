package com.example.sampleschoolteacher.ui.courses.class_data.class_timetable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.TimeModel;

import java.util.List;

public class TimetableViewModel extends ViewModel {
    private MutableLiveData<List<TimeModel>> mutableLiveData;

    public TimetableViewModel() {
    }

    public MutableLiveData<List<TimeModel>> getMutableLiveData(int classpos,int weekpos) {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.classModelList.getValue().get(classpos).getTimetable().get(weekpos).getTime());
        return mutableLiveData;
    }
}
