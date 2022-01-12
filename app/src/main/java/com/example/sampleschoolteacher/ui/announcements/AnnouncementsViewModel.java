package com.example.sampleschoolteacher.ui.announcements;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AnnouncementModel;

import java.util.List;

public class AnnouncementsViewModel extends ViewModel {
    private MutableLiveData<List<AnnouncementModel>> mutableLiveData;

    public AnnouncementsViewModel() {
    }

    public MutableLiveData<List<AnnouncementModel>> getMutableLiveData() {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.announcementList);
        return mutableLiveData;
    }
}
