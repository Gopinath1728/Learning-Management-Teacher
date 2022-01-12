package com.example.sampleschoolteacher.Callback;



import com.example.sampleschoolteacher.Model.StudentModel;

import java.util.List;

public interface IViewStudentCallbackListener {
    void onStudentLoadSuccess(List<StudentModel> studentModelList);
    void onStudentLoadFailed(String error);
}
