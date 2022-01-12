package com.example.sampleschoolteacher.Callback;


import com.example.sampleschoolteacher.Model.ClassModel;

import java.util.List;

public interface IViewClassesCallbackListener {
    void onClassesLoadSuccess(List<ClassModel> classModelList);
    void onClassesLoadFailed(String error);
}
