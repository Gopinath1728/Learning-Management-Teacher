package com.example.sampleschoolteacher.Callback;



import com.example.sampleschoolteacher.Model.ExaminationModel;

import java.util.List;

public interface IViewExamsCallbackListener {
    void onExamsLoadSuccess(List<ExaminationModel> examinationModels);
    void onExamsLoadFailed(String examLoadError);
}
