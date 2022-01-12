package com.example.sampleschoolteacher.Callback;

import com.example.sampleschoolteacher.Model.ClassModel;

import java.util.List;

public interface IClassViewCallbackListener {
   void onClassLoadSuccess(ClassModel classModel);
   void onClassLoadFailed(String error);

}
