package com.example.sampleschoolteacher.Callback;



import com.example.sampleschoolteacher.Model.AnnouncementModel;

import java.util.List;

public interface IAnnouncementCallbackListener {
    void onAnnouncementLoadSuccess(List<AnnouncementModel> announcementModelList);
    void onAnnouncementLoadFailed(String announcementLoadError);
}
