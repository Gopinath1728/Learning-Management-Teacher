package com.example.sampleschoolteacher.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Adapter.CoursesAdapter;
import com.example.sampleschoolteacher.Adapter.HomeFragmentAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Common.SpacesItemDecoration;
import com.example.sampleschoolteacher.Model.AnnouncementModel;
import com.example.sampleschoolteacher.Model.ClassModel;
import com.example.sampleschoolteacher.Model.TeachingClassModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    HomeFragmentAdapter adapter;

    RecyclerView recycler_home_timetable;
    TextView txt_home_timetable,txt_announcement;
    ImageButton btn_courses, btn_my_timetable,btn_announcement,btn_my_profile;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btn_courses = binding.btnCourses;
        btn_my_timetable = binding.btnMyTimetable;
        recycler_home_timetable = binding.recyclerHomeTimetable;
        txt_home_timetable = binding.txtHomeTimetable;
        btn_my_profile = binding.btnMyProfile;
        btn_announcement = binding.btnAnnouncement;
        txt_announcement = binding.txtAnnouncement;


        homeViewModel.getErrorMessage().observe(getViewLifecycleOwner(), s -> {
            Snackbar.make(root, "" + s, Snackbar.LENGTH_SHORT).show();
        });

        List<TeachingClassModel> teachingClassModelList1 = Common.currentTeacher.getTeachingClasses();
        List<String> classList = new ArrayList<>();
        for (int i = 0; i < teachingClassModelList1.size(); i++) {
            classList.add(teachingClassModelList1.get(i).getClassName());
        }
        homeViewModel.getClassModelListMutable(classList).observe(getViewLifecycleOwner(), classModels -> {

            homeViewModel.getTeachingModelListMutable().observe(getViewLifecycleOwner(), teachingClassModelList -> {
                if (classModels != null && !classModels.isEmpty()) {
                    List<TeachingClassModel> tcm = new ArrayList<>();
                    for (int i = 0; i < teachingClassModelList.size(); i++)
                    {
                        if (teachingClassModelList.get(i).getClassName().equals(classModels.get(i).getClassName())) {
                            TeachingClassModel teachingClassModel = new TeachingClassModel(
                                    teachingClassModelList.get(i).getClassName(),
                                    teachingClassModelList.get(i).getSubjectName()
                            );
                            tcm.add(teachingClassModel);
                        } else {
                            for (int j = 0; j < teachingClassModelList.size(); j++) {
                                if (classModels.get(i).getClassName().equals(teachingClassModelList.get(j).getClassName())) {
                                    TeachingClassModel teachingClassModel = new TeachingClassModel(
                                            teachingClassModelList.get(j).getClassName(),
                                            teachingClassModelList.get(j).getSubjectName()
                                    );
                                    tcm.add(teachingClassModel);
                                    break;
                                }
                            }
                        }
                    }

                    Common.tcm = tcm;
                }
            });


            List<String> days = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

            days.add("Monday");
            days.add("Tuesday");
            days.add("Wednesday");
            days.add("Thursday");
            days.add("Friday");
            days.add("Saturday");

            if (days.contains(day))
            {
                adapter = new HomeFragmentAdapter(getContext(), Common.currentTeacher.getTimetable().get(days.indexOf(day)).getTime());
                recycler_home_timetable.setAdapter(adapter);
            }else
                txt_home_timetable.setText(new StringBuilder("No Timetable Available !"));

            recycler_home_timetable.setHasFixedSize(true);
            recycler_home_timetable.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            recycler_home_timetable.addItemDecoration(new SpacesItemDecoration(8));

        });




        homeViewModel.getAnnouncementError().observe(getViewLifecycleOwner(), s -> Snackbar.make(root,""+s,Snackbar.LENGTH_SHORT).show());

        homeViewModel.getAnnouncementListMutable().observe(getViewLifecycleOwner(), announcementModels -> {
            if (announcementModels != null && announcementModels.size()>0)
            {
                txt_announcement.setText(new StringBuilder(""+announcementModels.size()));
            }
        });

        btn_courses.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_courses));

        btn_my_timetable.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_my_weekday));

        btn_announcement.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_announcement));

        btn_my_profile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_my_profile));


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}