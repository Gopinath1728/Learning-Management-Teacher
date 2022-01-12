package com.example.sampleschoolteacher.Callback;


import com.example.sampleschoolteacher.Model.TeachingClassModel;

import java.util.List;

public interface ICoursesLoadCallbackListener {
    void onCoursesLoadSuccess(List<TeachingClassModel> teachingClassModelList);
    void onCoursesLoadFailed(String error);
}
