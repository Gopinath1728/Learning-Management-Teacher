package com.example.sampleschoolteacher.ui.courses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Adapter.CoursesAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Common.SpacesItemDecoration;
import com.example.sampleschoolteacher.Model.ClassModel;
import com.example.sampleschoolteacher.Model.TeachingClassModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentGalleryBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {

    RecyclerView recycler_courses;
    CoursesAdapter adapter;

    private CoursesViewModel coursesViewModel;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        coursesViewModel =
                new ViewModelProvider(this).get(CoursesViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recycler_courses = (RecyclerView) root.findViewById(R.id.recycler_courses);

        coursesViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), teachingClassModelList -> {
            if (teachingClassModelList != null && teachingClassModelList.size()>0)
            {
                adapter = new CoursesAdapter(getContext(), teachingClassModelList);
                recycler_courses.setAdapter(adapter);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter != null)
                {
                    switch (adapter.getItemViewType(position))
                    {
                        case 0: return 1;
                        case 1: return 2;
                        default:return -1;
                    }
                }
                return -1;
            }
        });
        recycler_courses.setLayoutManager(layoutManager);
        recycler_courses.addItemDecoration(new SpacesItemDecoration(8));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}