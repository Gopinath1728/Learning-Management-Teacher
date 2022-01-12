package com.example.sampleschoolteacher.ui.courses.class_data.examination.add_result;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sampleschoolteacher.Adapter.AddResultsAdapter;
import com.example.sampleschoolteacher.Eventbus.AddResultEvent;
import com.example.sampleschoolteacher.Model.ResultModel;
import com.example.sampleschoolteacher.Model.StudentModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentAddResultBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AddResultFragment extends Fragment {

    private AddResultViewModel addResultViewModel;

    private FragmentAddResultBinding binding;

    RecyclerView recycler_student_result;
    Button btn_upload_result;
    TextView txt_no_student;

    AddResultsAdapter adapter;

    String examType, subjectName;

    List<String> uid = new ArrayList<>();
    List<String> maxMarks = new ArrayList<>();
    List<String> obtainedMarks = new ArrayList<>();

    public static AddResultFragment newInstance() {
        return new AddResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addResultViewModel = new ViewModelProvider(this).get(AddResultViewModel.class);
        binding = FragmentAddResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recycler_student_result = binding.recyclerStudentResult;
        btn_upload_result = binding.btnUploadResult;
        txt_no_student = binding.txtNoStudent;

        if (getArguments() != null) {
            String className = getArguments().getString("className");
            examType = getArguments().getString("examType");
            subjectName = getArguments().getString("subjectName");

            addResultViewModel.getErrorMessage().observe(getViewLifecycleOwner(), s -> Snackbar.make(root, "" + s, Snackbar.LENGTH_LONG).show());

            addResultViewModel.getStudentModelListMutable(className).observe(getViewLifecycleOwner(), studentModels -> {
                if (studentModels != null && studentModels.size() > 0) {
                    txt_no_student.setVisibility(View.GONE);
                    adapter = new AddResultsAdapter(getContext(),studentModels);
                    recycler_student_result.setAdapter(adapter);
                }
            });
            recycler_student_result.setHasFixedSize(true);
            recycler_student_result.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


            btn_upload_result.setOnClickListener(addResult);

        }

        return root;
    }

    View.OnClickListener addResult = v -> {
        ResultModel resultModel = new ResultModel();
        resultModel.setExamType(examType);
        resultModel.setSubjectName(subjectName);
        for (int i = 0; i < uid.size(); i++) {
            resultModel.setMarksObtained(obtainedMarks.get(i));
            resultModel.setTotalMarks(maxMarks.get(i));
            resultModel.setResultDocId(FirebaseFirestore.getInstance().collection("Students")
                    .document(uid.get(i))
                    .collection("Results")
                    .document().getId());
            FirebaseFirestore.getInstance().collection("Students")
                    .document(uid.get(i))
                    .collection("Results")
                    .document(resultModel.getResultDocId())
                    .set(resultModel)
                    .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    btn_upload_result.setVisibility(View.GONE);
                }
            });
        }
    };

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUploadMarksEvent(AddResultEvent event) {
        if (uid.size() > 0) {
            if (uid.contains(event.getStudentUid())) {
                maxMarks.add(uid.indexOf(event.getStudentUid()), event.getMaxMarks());
                obtainedMarks.add(uid.indexOf(event.getStudentUid()), event.getMarksObtained());
            } else {
                uid.add(event.getStudentUid());
                maxMarks.add(event.getMaxMarks());
                obtainedMarks.add(event.getMarksObtained());
            }
        } else {
            btn_upload_result.setVisibility(View.VISIBLE);
            uid.add(event.getStudentUid());
            maxMarks.add(event.getMaxMarks());
            obtainedMarks.add(event.getMarksObtained());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}