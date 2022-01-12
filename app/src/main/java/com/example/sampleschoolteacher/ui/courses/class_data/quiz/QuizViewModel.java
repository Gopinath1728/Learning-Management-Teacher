package com.example.sampleschoolteacher.ui.courses.class_data.quiz;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Callback.IClassViewCallbackListener;
import com.example.sampleschoolteacher.Callback.IViewQuizCallbackListener;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.ClassModel;
import com.example.sampleschoolteacher.Model.QuizListModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuizViewModel extends ViewModel implements IViewQuizCallbackListener {
    private MutableLiveData<List<QuizListModel>> quizMutableLiveData;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private IViewQuizCallbackListener iViewQuizCallbackListener;

    public QuizViewModel() {
        iViewQuizCallbackListener=this;
    }

    public MutableLiveData<List<QuizListModel>> getMutableLiveData(int pos) {
        if (quizMutableLiveData == null) {
            quizMutableLiveData = new MutableLiveData<>();
            errorMessage = new MutableLiveData<>();
            loadclasses(pos);
        }
        return quizMutableLiveData;
    }

    private void loadclasses(int pos) {
        List<QuizListModel> quizListModelList = new ArrayList<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Classes")
                .document(Common.classModelList.getValue().get(pos).getDocId())
                .collection("quiz")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d("Classes Listen Failed", "" + error);
                        iViewQuizCallbackListener.onQuizLoadFailed(error.getMessage());
                        return;
                    }
                    if (value != null) {
                        quizListModelList.clear();
                        for (QueryDocumentSnapshot snapshot : value)
                        {
                            QuizListModel quizListModel = snapshot.toObject(QuizListModel.class);
                            quizListModelList.add(quizListModel);
                        }
                        if (quizListModelList.size()>0)
                        {
                            iViewQuizCallbackListener.onQuizLoadSuccess(quizListModelList);
                        }
                    }
                });
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }


    @Override
    public void onQuizLoadSuccess(List<QuizListModel> quizListModelList) {
        quizMutableLiveData.setValue(quizListModelList);
    }

    @Override
    public void onQuizLoadFailed(String quizLoadError) {
        errorMessage.setValue(quizLoadError);
    }
}