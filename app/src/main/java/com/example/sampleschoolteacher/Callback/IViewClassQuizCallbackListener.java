package com.example.sampleschoolteacher.Callback;

import com.example.sampleschoolteacher.Model.QuizModel;

import java.util.List;

public interface IViewClassQuizCallbackListener {
    void onQuizLoadSuccess(List<QuizModel> quizModelList);
    void onQuizLoadFailed(String error);
}
