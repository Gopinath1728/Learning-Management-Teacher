package com.example.sampleschoolteacher.ui.courses.class_data.examination;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Adapter.ExaminationAdapter;
import com.example.sampleschoolteacher.Common.SpacesItemDecoration;
import com.example.sampleschoolteacher.databinding.FragmentExaminationBinding;

public class ExaminationFragment extends Fragment {

    private ExaminationViewModel examinationViewModel;
    private FragmentExaminationBinding binding;

    int position;

    RecyclerView recycler_examinations;
    TextView txt_no_exams;
    ExaminationAdapter adapter;

    public static ExaminationFragment newInstance() {
        return new ExaminationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentExaminationBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        examinationViewModel = new ViewModelProvider(this).get(ExaminationViewModel.class);

        recycler_examinations = binding.recyclerExaminations;
        txt_no_exams = binding.txtNoExams;

        if (getArguments() != null){
            position = getArguments().getInt("classPos");
            String className = getArguments().getString("className");
            String subject  = getArguments().getString("subjectName");

            examinationViewModel.getExamMutableLiveData(position).observe(getViewLifecycleOwner(), examinationModels -> {
                if (examinationModels != null && examinationModels.size() > 0) {
                    txt_no_exams.setVisibility(View.GONE);
                    adapter = new ExaminationAdapter(getContext(), examinationModels,subject,className);
                    recycler_examinations.setAdapter(adapter);
                } else
                    txt_no_exams.setVisibility(View.VISIBLE);
            });
            recycler_examinations.setHasFixedSize(true);
            recycler_examinations.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}