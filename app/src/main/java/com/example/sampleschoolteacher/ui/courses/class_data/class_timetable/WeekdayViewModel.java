package com.example.sampleschoolteacher.ui.courses.class_data.class_timetable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.TimetableModel;

import java.util.List;

public class WeekdayViewModel extends ViewModel {
    private MutableLiveData<List<TimetableModel>> mutableLiveData;

    public WeekdayViewModel() {
    }

    public MutableLiveData<List<TimetableModel>> getMutableLiveData(int position) {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.classModelList.getValue().get(position).getTimetable());
        return mutableLiveData;
    }
}
