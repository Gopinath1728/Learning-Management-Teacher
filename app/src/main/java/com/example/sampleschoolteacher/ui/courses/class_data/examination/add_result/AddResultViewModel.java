package com.example.sampleschoolteacher.ui.courses.class_data.examination.add_result;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Callback.IViewStudentCallbackListener;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.StudentModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddResultViewModel extends ViewModel implements IViewStudentCallbackListener
{
    private MutableLiveData<List<StudentModel>> studentModelListMutable;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private IViewStudentCallbackListener viewStudentCallbackListener;

    public AddResultViewModel() {
        viewStudentCallbackListener=this;
    }

    public MutableLiveData<List<StudentModel>> getStudentModelListMutable(String ClassName) {
        if (studentModelListMutable == null) {
            studentModelListMutable = new MutableLiveData<>();
            errorMessage = new MutableLiveData<>();
            loadstudents(ClassName);
        }
        return studentModelListMutable;
    }

    private void loadstudents(String className) {
        List<StudentModel> tempList = new ArrayList<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Students")
                .whereEqualTo("studentClass",className)
                .orderBy("studentName")
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        Log.d("Student Listen Failed", "" + error);
                        viewStudentCallbackListener.onStudentLoadFailed(error.getMessage());
                        return;
                    }
                    if (value != null && !value.isEmpty()) {
                        tempList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            StudentModel studentModel = documentSnapshot.toObject(StudentModel.class);
                            tempList.add(studentModel);
                        }
                        if (tempList.size() > 0) {
                            Common.studentModelList = tempList;
                            viewStudentCallbackListener.onStudentLoadSuccess(tempList);
                        }
                    }
                });
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void onStudentLoadSuccess(List<StudentModel> studentModelList) {
        studentModelListMutable.setValue(studentModelList);
    }

    @Override
    public void onStudentLoadFailed(String error) {
        errorMessage.setValue(error);
    }
}