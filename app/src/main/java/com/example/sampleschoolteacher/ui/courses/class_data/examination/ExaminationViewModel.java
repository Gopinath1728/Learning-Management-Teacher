package com.example.sampleschoolteacher.ui.courses.class_data.examination;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Callback.IViewExamsCallbackListener;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.ExaminationModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExaminationViewModel extends ViewModel implements IViewExamsCallbackListener {
    private MutableLiveData<List<ExaminationModel>> examMutableLiveData;
    private MutableLiveData<String> examErrorMutable = new MutableLiveData<>();
    private IViewExamsCallbackListener iViewExamsCallbackListener;

    public ExaminationViewModel() {
        iViewExamsCallbackListener=this;
    }

    public MutableLiveData<List<ExaminationModel>> getExamMutableLiveData(int classPos) {
        if (examMutableLiveData == null) {
            examMutableLiveData = new MutableLiveData<>();
            examErrorMutable = new MutableLiveData<>();
            loadExams(classPos);
        }
        return examMutableLiveData;
    }

    private void loadExams(int classPos) {
        List<ExaminationModel> tempList = new ArrayList<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Classes")
                .document(Common.classModelList.getValue().get(classPos).getDocId())
                .collection("Examinations")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d("Exams Listen Failed", "" + error);
                        iViewExamsCallbackListener.onExamsLoadFailed(error.getMessage());
                        return;
                    }
                    if (value != null && !value.isEmpty()) {
                        tempList.clear();
                        for (QueryDocumentSnapshot snapshot : value) {
                            ExaminationModel examinationModel = snapshot.toObject(ExaminationModel.class);
                            tempList.add(examinationModel);
                        }
                        if (tempList.size()>0)
                        {
                            iViewExamsCallbackListener.onExamsLoadSuccess(tempList);
                        }
                    }
                });
    }

    public MutableLiveData<String> getExamErrorMutable() {
        return examErrorMutable;
    }

    @Override
    public void onExamsLoadSuccess(List<ExaminationModel> examinationModels) {
        examMutableLiveData.setValue(examinationModels);
    }

    @Override
    public void onExamsLoadFailed(String examLoadError) {
        examErrorMutable.setValue(examLoadError);
    }
}