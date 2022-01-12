package com.example.sampleschoolteacher.ui.courses.class_data.class_students.student_data.student_attendance;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Callback.IAttendanceViewCallback;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AttendanceModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentAttendanceViewModel extends ViewModel implements IAttendanceViewCallback {
    private MutableLiveData<List<AttendanceModel>> attendanceMutableLiveData;
    private MutableLiveData<String> attendanceErrorMessage = new MutableLiveData<>();
    private IAttendanceViewCallback attendanceViewCallback;


    public StudentAttendanceViewModel() {
        attendanceViewCallback=this;
    }

    public MutableLiveData<List<AttendanceModel>> getAttendanceMutableLiveData(String uid) {
        if (attendanceMutableLiveData == null) {
            attendanceMutableLiveData = new MutableLiveData<>();
            attendanceErrorMessage = new MutableLiveData<>();
            loadAttendance(uid);
        }
        return attendanceMutableLiveData;
    }

    private void loadAttendance(String uid) {
        List<AttendanceModel> attendanceModelList = new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Students")
                .document(uid)
                .collection("attendance")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        attendanceViewCallback.onAttendanceLoadFailed(error.getMessage());
                    } else {
                        if (value != null) {
                            attendanceModelList.clear();
                            for (QueryDocumentSnapshot snapshot : value) {
                                AttendanceModel attendanceModel = snapshot.toObject(AttendanceModel.class);
                                attendanceModelList.add(attendanceModel);
                            }
                            if (attendanceModelList.size() > 0) {
                                Common.attendanceModels = attendanceModelList;
                                attendanceViewCallback.onAttendanceLoadSuccess(attendanceModelList);
                            }
                        }
                    }
                });
    }

    public MutableLiveData<String> getAttendanceErrorMessage() {
        return attendanceErrorMessage;
    }

    @Override
    public void onAttendanceLoadSuccess(List<AttendanceModel> attendanceModelList) {
        attendanceMutableLiveData.setValue(attendanceModelList);
    }

    @Override
    public void onAttendanceLoadFailed(String attendanceLoadError) {
        attendanceErrorMessage.setValue(attendanceLoadError);
    }
}