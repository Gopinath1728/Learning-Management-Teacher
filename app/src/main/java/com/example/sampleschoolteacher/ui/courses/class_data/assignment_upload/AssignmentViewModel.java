package com.example.sampleschoolteacher.ui.courses.class_data.assignment_upload;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleschoolteacher.Callback.IAssignmentLoadCallbackListener;
import com.example.sampleschoolteacher.Callback.IClassViewCallbackListener;
import com.example.sampleschoolteacher.Callback.IViewClassesCallbackListener;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AssignmentModel;
import com.example.sampleschoolteacher.Model.ClassModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AssignmentViewModel extends ViewModel implements IAssignmentLoadCallbackListener {

    private MutableLiveData<List<AssignmentModel>> assignmentMutableLiveData;
    private MutableLiveData<String> assignmentErrorMessage = new MutableLiveData<>();
    private IAssignmentLoadCallbackListener assignmentLoadCallbackListener;

    public AssignmentViewModel() {
        assignmentLoadCallbackListener=this;
    }


    public MutableLiveData<List<AssignmentModel>> getAssignmentMutableLiveData(int pos) {
        if (assignmentMutableLiveData == null) {
            assignmentMutableLiveData = new MutableLiveData<>();
            assignmentErrorMessage = new MutableLiveData<>();
            loadAssignments(pos);
        }
        return assignmentMutableLiveData;
    }

    public MutableLiveData<String> getAssignmentErrorMessage() {
        return assignmentErrorMessage;
    }

    private void loadAssignments(int pos) {
        List<AssignmentModel> assignmentModelList = new ArrayList<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Classes")
                .document(Common.classModelList.getValue().get(pos).getDocId())
                .collection("Assignments")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d("Classes Listen Failed", "" + error);
                        assignmentLoadCallbackListener.onAssignmentLoadFailed(error.getMessage());
                        return;
                    }
                    if (value != null) {
                        assignmentModelList.clear();
                        for (QueryDocumentSnapshot snapshot : value)
                        {
                            AssignmentModel assignmentModel = snapshot.toObject(AssignmentModel.class);
                            assignmentModelList.add(assignmentModel);
                        }
                        if (assignmentModelList.size()>0)
                        {
                            assignmentLoadCallbackListener.onAssignmentLoadSuccess(assignmentModelList);
                        }
                    }
                });
    }




    @Override
    public void onAssignmentLoadSuccess(List<AssignmentModel> assignmentModelList) {
        assignmentMutableLiveData.setValue(assignmentModelList);
    }

    @Override
    public void onAssignmentLoadFailed(String error) {
assignmentErrorMessage.setValue(error);
    }
}