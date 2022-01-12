package com.example.sampleschoolteacher.ui.courses.class_data;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Model.ClassModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentClassDataBinding;
import com.example.sampleschoolteacher.databinding.FragmentGalleryBinding;
import com.example.sampleschoolteacher.ui.courses.CoursesViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClassDataFragment extends Fragment {


    CircleImageView img_class_proctor;
    TextView txt_class_proctor_name;
    ImageButton btn_students, btn_timetable, btn_view_students_assignments,
            btn_quiz, btn_exams;


    private FragmentClassDataBinding binding;

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClassDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        img_class_proctor = (CircleImageView) root.findViewById(R.id.img_class_proctor);
        txt_class_proctor_name = (TextView) root.findViewById(R.id.txt_class_proctor_name);
        btn_students = (ImageButton) root.findViewById(R.id.btn_students);
        btn_timetable = (ImageButton) root.findViewById(R.id.btn_timetable);

        btn_view_students_assignments = (ImageButton) root.findViewById(R.id.btn_view_students_assignments);
        btn_quiz = (ImageButton) root.findViewById(R.id.btn_quiz);
        btn_exams = (ImageButton) root.findViewById(R.id.btn_exams);


        assert getArguments() != null;
        int pos = getArguments().getInt("classPos");
        String subject = getArguments().getString("subjectName");


        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(Common.classModelList.getValue().get(pos).getClassName());
        if (!Common.classModelList.getValue().get(pos).getProctorImage().equalsIgnoreCase("Null"))
            Glide.with(getContext()).load(Common.classModelList.getValue().get(pos).getProctorImage()).into(img_class_proctor);
        txt_class_proctor_name.setText(new StringBuilder(Common.classModelList.getValue().get(pos).getProctorName()));

        Bundle bundle = new Bundle();
        bundle.putInt("classPos",pos);
        bundle.putString("subjectName",subject);
        bundle.putString("className", Common.classModelList.getValue().get(pos).getClassName());
        btn_students.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_students, bundle));

        Bundle bundle1 = new Bundle();
        bundle1.putInt("classPos",pos);
        bundle1.putString("subjectName",subject);
        btn_timetable.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_weekday,bundle1));

        btn_view_students_assignments.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_assignment,bundle1));

        btn_quiz.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_quiz,bundle1));

        btn_exams.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_examinations,bundle));

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}