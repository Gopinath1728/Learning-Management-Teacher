package com.example.sampleschoolteacher.ui.courses.class_data.class_students.student_data.student_result;

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
import android.widget.TextView;

import com.example.sampleschoolteacher.Adapter.ResultViewAdapter;
import com.example.sampleschoolteacher.Model.ResultModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentStudentResultBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class StudentResultFragment extends Fragment {

    private StudentResultViewModel studentResultViewModel;

    private FragmentStudentResultBinding binding;

    RecyclerView recycler_results;

    TextView txt_no_result;

    ResultViewAdapter adapter;

    public static StudentResultFragment newInstance() {
        return new StudentResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        studentResultViewModel = new ViewModelProvider(this).get(StudentResultViewModel.class);
        binding = FragmentStudentResultBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        recycler_results = binding.recyclerResults;
        txt_no_result = binding.txtNoResult;

        if (getArguments()!= null)
        {
            String studentUid = getArguments().getString("studentUid");

            studentResultViewModel.getResultErrorMutable().observe(getViewLifecycleOwner(), s -> Snackbar.make(root,""+s,Snackbar.LENGTH_LONG).show());

            studentResultViewModel.getResultMutableLiveData(studentUid).observe(getViewLifecycleOwner(), resultModels -> {
                if (resultModels != null && resultModels.size()>0)
                {
                    txt_no_result.setVisibility(View.GONE);
                    adapter = new ResultViewAdapter(getContext(),resultModels);
                    recycler_results.setAdapter(adapter);
                }
            });
            recycler_results.setHasFixedSize(true);
            recycler_results.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}