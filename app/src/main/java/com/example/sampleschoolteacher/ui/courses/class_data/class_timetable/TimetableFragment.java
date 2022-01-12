package com.example.sampleschoolteacher.ui.courses.class_data.class_timetable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sampleschoolteacher.Adapter.TimeViewAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Common.SpacesItemDecoration;
import com.example.sampleschoolteacher.Model.TimeModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentTimetableBinding;

import java.util.List;


public class TimetableFragment extends Fragment {

    RecyclerView recycler_timetable;
    TimeViewAdapter adapter;
    TextView txt_no_timetable;

    private FragmentTimetableBinding binding;
    private TimetableViewModel timetableViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        timetableViewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
        binding = FragmentTimetableBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        recycler_timetable = binding.recyclerTimetable;
        txt_no_timetable = binding.txtNoTimetable;

        if (getArguments() != null){
            int classpos = getArguments().getInt("classPosition");
            int weekpos = getArguments().getInt("weekPosition");

            timetableViewModel.getMutableLiveData(classpos, weekpos).observe(getViewLifecycleOwner(), timeModels -> {
                if (timeModels != null && timeModels.size() > 0) {
                    txt_no_timetable.setVisibility(View.GONE);
                    adapter = new TimeViewAdapter(getContext(), timeModels);
                    recycler_timetable.setAdapter(adapter);
                }
            });

            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (adapter != null) {
                        switch (adapter.getItemViewType(position)) {
                            case 0:
                                return 1;
                            case 1:
                                return 2;
                            default:
                                return -1;
                        }
                    }
                    return -1;
                }
            });
            recycler_timetable.setLayoutManager(layoutManager);
            recycler_timetable.addItemDecoration(new SpacesItemDecoration(8));
        }
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}