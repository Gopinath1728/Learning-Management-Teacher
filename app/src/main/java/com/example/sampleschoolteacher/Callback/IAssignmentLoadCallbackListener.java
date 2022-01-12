package com.example.sampleschoolteacher.Callback;

import com.example.sampleschoolteacher.Model.AssignmentModel;

import java.util.List;

public interface IAssignmentLoadCallbackListener {
    void onAssignmentLoadSuccess(List<AssignmentModel> assignmentModelList);
    void onAssignmentLoadFailed(String error);
}
