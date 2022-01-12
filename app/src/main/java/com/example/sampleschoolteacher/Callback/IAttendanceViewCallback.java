package com.example.sampleschoolteacher.Callback;



import com.example.sampleschoolteacher.Model.AttendanceModel;

import java.util.List;

public interface IAttendanceViewCallback {
    void onAttendanceLoadSuccess(List<AttendanceModel> attendanceModelList);
    void onAttendanceLoadFailed(String attendanceLoadError);
}
