package com.example.sampleschoolteacher.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Callback.IAnnouncementCallbackListener;
import com.example.sampleschoolteacher.Callback.IViewClassesCallbackListener;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AnnouncementModel;
import com.example.sampleschoolteacher.Model.ClassModel;
import com.example.sampleschoolteacher.Model.TeachingClassModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements IViewClassesCallbackListener, IAnnouncementCallbackListener {

    private MutableLiveData<List<TeachingClassModel>> teachingModelListMutable;
    private MutableLiveData<List<ClassModel>> classModelListMutable;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private IViewClassesCallbackListener classesCallbackListener;

    private MutableLiveData<List<AnnouncementModel>> announcementListMutable;
    private MutableLiveData<String> announcementError = new MutableLiveData<>();
    private IAnnouncementCallbackListener iAnnouncementCallbackListener;

    public HomeViewModel() {
        classesCallbackListener=this;
        iAnnouncementCallbackListener=this;
    }

    public MutableLiveData<List<ClassModel>> getClassModelListMutable(List<String> classes) {
        if (classModelListMutable == null) {
            classModelListMutable = new MutableLiveData<>();
            errorMessage = new MutableLiveData<>();
            loadclasses(classes);
        }
        Common.classModelList = classModelListMutable;
        return classModelListMutable;
    }

    public MutableLiveData<List<TeachingClassModel>> getTeachingModelListMutable() {
        if (teachingModelListMutable == null)
            teachingModelListMutable = new MutableLiveData<>();
        teachingModelListMutable.setValue(Common.currentTeacher.getTeachingClasses());
        return teachingModelListMutable;
    }

    private void loadclasses(List<String> classes) {
        List<ClassModel> tempList = new ArrayList<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Classes")
                .whereIn("className",classes)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d("Classes Listen Failed", "" + error);
                        classesCallbackListener.onClassesLoadFailed(error.getMessage());
                        return;
                    }
                    if (value != null && !value.isEmpty()) {
                        tempList.clear();
                        for (QueryDocumentSnapshot snapshot : value) {
                            ClassModel classModel = snapshot.toObject(ClassModel.class);
                            tempList.add(classModel);
                        }

                        if (tempList.size()>0){
                            Common.classModelList.setValue(tempList);
                            classesCallbackListener.onClassesLoadSuccess(tempList);
                        }
                    }
                });
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<List<AnnouncementModel>> getAnnouncementListMutable() {
        if (announcementListMutable == null) {
            announcementListMutable = new MutableLiveData<>();
            announcementError = new MutableLiveData<>();
            loadAnnouncements();
        }
        return announcementListMutable;
    }

    private void loadAnnouncements() {
        List<AnnouncementModel> announcementModelList = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection("Announcements")
                .addSnapshotListener((value, error) -> {
                    if (error != null)
                    {
                        iAnnouncementCallbackListener.onAnnouncementLoadFailed(error.getMessage());
                    }
                    else {
                        if (value != null)
                        {
                            announcementModelList.clear();
                            for (QueryDocumentSnapshot snapshot : value)
                            {
                                AnnouncementModel announcementModel = snapshot.toObject(AnnouncementModel.class);
                                announcementModelList.add(announcementModel);
                            }
                            if (announcementModelList.size()>0)
                            {
                                Common.announcementList = announcementModelList;
                                iAnnouncementCallbackListener.onAnnouncementLoadSuccess(announcementModelList);
                            }
                        }
                    }
                });
    }

    public MutableLiveData<String> getAnnouncementError() {
        return announcementError;
    }

    @Override
    public void onClassesLoadSuccess(List<ClassModel> classModelList) {
        classModelListMutable.setValue(classModelList);
    }

    @Override
    public void onClassesLoadFailed(String error) {
        errorMessage.setValue(error);
    }

    @Override
    public void onAnnouncementLoadSuccess(List<AnnouncementModel> announcementModelList) {
        announcementListMutable.setValue(announcementModelList);
    }

    @Override
    public void onAnnouncementLoadFailed(String announcementLoadError) {
        announcementError.setValue(announcementLoadError);
    }
}