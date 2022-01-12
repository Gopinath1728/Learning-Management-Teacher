package com.example.sampleschoolteacher.ui.announcements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sampleschoolteacher.Adapter.AnnouncementAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.AnnouncementModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentAnnouncementsBinding;

import java.util.List;

public class AnnouncementsFragment extends Fragment {

    private FragmentAnnouncementsBinding binding;
    private AnnouncementsViewModel announcementsViewModel;

    RecyclerView recycler_announcements;
    TextView txt_no_announcements;
    AnnouncementAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        announcementsViewModel = new ViewModelProvider(this).get(AnnouncementsViewModel.class);
        binding = FragmentAnnouncementsBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        recycler_announcements = binding.recyclerAnnouncements;
        txt_no_announcements = binding.txtNoAnnouncements;

        announcementsViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), announcementModels -> {
            if (announcementModels != null && announcementModels.size()>0)
            {
                txt_no_announcements.setVisibility(View.GONE);
                adapter = new AnnouncementAdapter(getContext(),announcementModels );
                recycler_announcements.setAdapter(adapter);
            }
        });
        recycler_announcements.setHasFixedSize(true);
        recycler_announcements.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}