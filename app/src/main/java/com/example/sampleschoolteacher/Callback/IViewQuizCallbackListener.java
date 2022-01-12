package com.example.sampleschoolteacher.Callback;

import com.example.sampleschoolteacher.Model.QuizListModel;

import java.util.List;

public interface IViewQuizCallbackListener {
    void onQuizLoadSuccess(List<QuizListModel> quizListModelList);
    void onQuizLoadFailed(String quizLoadError);
}
