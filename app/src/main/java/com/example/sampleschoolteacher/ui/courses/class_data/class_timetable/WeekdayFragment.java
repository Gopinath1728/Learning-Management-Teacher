package com.example.sampleschoolteacher.ui.courses.class_data.class_timetable;

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

import com.example.sampleschoolteacher.Adapter.WeekdayViewAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.TimetableModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentWeekdayBinding;

import java.util.List;


public class WeekdayFragment extends Fragment {

   RecyclerView recycler_weekday;
   TextView txt_no_weekday;
   WeekdayViewAdapter adapter;

   private FragmentWeekdayBinding binding;
   private WeekdayViewModel weekdayViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        weekdayViewModel = new ViewModelProvider(this).get(WeekdayViewModel.class);
        binding = FragmentWeekdayBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        recycler_weekday = binding.recyclerWeekday;
        txt_no_weekday = binding.txtNoWeekday;
        if (getArguments() != null){
            int position = getArguments().getInt("classPos");

            weekdayViewModel.getMutableLiveData(position).observe(getViewLifecycleOwner(), timetableModels -> {
                if (timetableModels != null && timetableModels.size()>0)
                {
                    txt_no_weekday.setVisibility(View.GONE);
                    adapter = new WeekdayViewAdapter(getContext(), timetableModels, position);
                    recycler_weekday.setAdapter(adapter);
                }
            });
            recycler_weekday.setHasFixedSize(true);
            recycler_weekday.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}