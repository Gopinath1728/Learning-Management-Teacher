package com.example.sampleschoolteacher.ui.timetable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sampleschoolteacher.Adapter.MyWeekdayAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.TeacherTimetableModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentWeekday2Binding;
import com.example.sampleschoolteacher.databinding.FragmentWeekdayBinding;
import com.google.android.material.resources.TextAppearance;

import java.util.List;


public class WeekdayFragment extends Fragment {

    private FragmentWeekday2Binding binding;

    private WeekdayViewModel weekdayViewModel;

    RecyclerView recycler_my_weekday;
    TextView txt_no_weekday2;
    MyWeekdayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        weekdayViewModel = new ViewModelProvider(this).get(WeekdayViewModel.class);
        binding = FragmentWeekday2Binding.inflate(inflater,container,false);
        View root = binding.getRoot();

        recycler_my_weekday = binding.recyclerMyWeekday;
        txt_no_weekday2 = binding.txtNoWeekday2;

        weekdayViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), teacherTimetableModels -> {
            if (teacherTimetableModels != null && teacherTimetableModels.size()>0) {
                txt_no_weekday2.setVisibility(View.GONE);
                adapter = new MyWeekdayAdapter(getContext(), teacherTimetableModels);
                recycler_my_weekday.setAdapter(adapter);
            }
        });
        recycler_my_weekday.setHasFixedSize(true);
        recycler_my_weekday.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}