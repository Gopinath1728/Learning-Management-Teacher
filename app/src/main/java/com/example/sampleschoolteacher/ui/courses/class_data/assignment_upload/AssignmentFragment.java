package com.example.sampleschoolteacher.ui.courses.class_data.assignment_upload;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Adapter.AssignmentAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Eventbus.AssignmentDeleteEvent;
import com.example.sampleschoolteacher.Model.AssignmentModel;
import com.example.sampleschoolteacher.Model.ClassModel;
import com.example.sampleschoolteacher.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AssignmentFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.recycler_assignment)
    RecyclerView recycler_assignment;
    @BindView(R.id.txt_noList)
    TextView txt_noList;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    AssignmentAdapter adapter;
    private List<AssignmentModel> assignmentModel;

    String subject;
    private AssignmentViewModel assignmentViewModel;

    int classPos;

    ClassModel classModel = new ClassModel();

    public static AssignmentFragment newInstance() {
        return new AssignmentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        assignmentViewModel = new ViewModelProvider(this).get(AssignmentViewModel.class);
        View root = inflater.inflate(R.layout.assignment_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);


        assert getArguments() != null;
        classPos = getArguments().getInt("classPos");

        subject = getArguments().getString("subjectName");


        assignmentViewModel.getAssignmentErrorMessage().observe(getViewLifecycleOwner(), s -> {
            Snackbar.make(root,""+s,Snackbar.LENGTH_SHORT).show();
        });

        assignmentViewModel.getAssignmentMutableLiveData(classPos).observe(getViewLifecycleOwner(), assignmentModelList -> {
            if (assignmentModelList !=null && assignmentModelList.size()>0)
            {
                txt_noList.setVisibility(View.GONE);
                adapter = new AssignmentAdapter(getContext(), assignmentModelList,classPos);
                recycler_assignment.setAdapter(adapter);
            }
        });

        recycler_assignment.setHasFixedSize(true);
        recycler_assignment.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        fab.setOnClickListener(AddAssignment);

        return root;
    }

    private View.OnClickListener AddAssignment = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Assignment");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.add_assignment_dialog, null);
        TextView txt_assi_subject_name = (TextView) itemView.findViewById(R.id.txt_assi_subject_name);
        TextView txt_assi_teacher_name = (TextView) itemView.findViewById(R.id.txt_assi_teacher_name);
        EditText edt_assi_topic_name = (EditText) itemView.findViewById(R.id.edt_assi_topic_name);
        EditText edt_assi_body = (EditText) itemView.findViewById(R.id.edt_assi_body);
        Button btn_assi_add = (Button) itemView.findViewById(R.id.btn_assi_add);
        Button btn_assi_cancel = (Button) itemView.findViewById(R.id.btn_assi_cancel);
        builder.setView(itemView);

        txt_assi_subject_name.setText(new StringBuilder(subject));
        txt_assi_teacher_name.setText(new StringBuilder(Common.currentTeacher.getTeacherName()));

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        btn_assi_cancel.setOnClickListener(v1 -> dialog.dismiss());

        btn_assi_add.setOnClickListener(v12 -> {
            AssignmentModel assignmentModel = new AssignmentModel();

            if (!edt_assi_topic_name.getText().toString().isEmpty())
                assignmentModel.setTopic(edt_assi_topic_name.getText().toString().trim());
            else
                edt_assi_topic_name.setError("Topic can't be empty !");

            if (!edt_assi_body.getText().toString().isEmpty())
                assignmentModel.setBody(edt_assi_body.getText().toString().trim());
            else
                edt_assi_body.setError("Body can't be empty !");

            if (!edt_assi_topic_name.getText().toString().isEmpty() &&
                    !edt_assi_body.getText().toString().isEmpty())
            {

                assignmentModel.setSubject(subject);
                assignmentModel.setTeacherName(Common.currentTeacher.getTeacherName());
                assignmentModel.setDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                assignmentModel.setAssignmentId(firestore.collection("Classes")
                        .document(Common.classModelList.getValue().get(classPos).getDocId())
                        .collection("Assignments")
                        .document().getId());
                firestore.collection("Classes")
                        .document(Common.classModelList.getValue().get(classPos).getDocId())
                        .collection("Assignments")
                        .document(assignmentModel.getAssignmentId())
                        .set(assignmentModel)
                        .addOnSuccessListener(unused -> {
                            Snackbar.make(v,"Assignment Added Successfully",Snackbar.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Snackbar.make(v,""+e,Snackbar.LENGTH_SHORT).show();
                        });
                dialog.dismiss();
            }

        });

    };


}