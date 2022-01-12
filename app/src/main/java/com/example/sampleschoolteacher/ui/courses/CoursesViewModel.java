package com.example.sampleschoolteacher.ui.courses;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Callback.IAssignmentLoadCallbackListener;
import com.example.sampleschoolteacher.Callback.ICoursesLoadCallbackListener;
import com.example.sampleschoolteacher.Callback.IViewClassesCallbackListener;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AssignmentModel;
import com.example.sampleschoolteacher.Model.ClassModel;
import com.example.sampleschoolteacher.Model.TeachingClassModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CoursesViewModel extends ViewModel {
    private MutableLiveData<List<TeachingClassModel>> mutableLiveData;

    public CoursesViewModel() {
    }

    public MutableLiveData<List<TeachingClassModel>> getMutableLiveData() {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.tcm);
        return mutableLiveData;
    }
}