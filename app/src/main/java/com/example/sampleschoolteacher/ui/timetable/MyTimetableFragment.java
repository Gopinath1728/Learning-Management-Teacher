package com.example.sampleschoolteacher.ui.timetable;

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

import com.example.sampleschoolteacher.Adapter.TeacherTimeViewAdapter;
import com.example.sampleschoolteacher.Adapter.TimeViewAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Common.SpacesItemDecoration;
import com.example.sampleschoolteacher.Model.TeacherTimeModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentMyTimetableBinding;

import java.util.List;


public class MyTimetableFragment extends Fragment {

    private FragmentMyTimetableBinding binding;

    private MyTimetableViewModel myTimetableViewModel;

    RecyclerView recycler_my_timetable;
    TextView txt_no_my_timetable;
    TeacherTimeViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myTimetableViewModel = new ViewModelProvider(this).get(MyTimetableViewModel.class);
        binding = FragmentMyTimetableBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        if (getArguments() != null){

            int position = getArguments().getInt("weekPosition");

            recycler_my_timetable = binding.recyclerMyTimetable;
            txt_no_my_timetable = binding.txtNoMyTimetable;

            myTimetableViewModel.getMutableLiveData(position).observe(getViewLifecycleOwner(), teacherTimeModels -> {
                if (teacherTimeModels != null && teacherTimeModels.size()>0)
                {
                    txt_no_my_timetable.setVisibility(View.GONE);
                    adapter = new TeacherTimeViewAdapter(getContext(), teacherTimeModels);
                    recycler_my_timetable.setAdapter(adapter);
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
            recycler_my_timetable.setLayoutManager(layoutManager);
            recycler_my_timetable.addItemDecoration(new SpacesItemDecoration(8));
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}