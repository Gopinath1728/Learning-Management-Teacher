package com.example.sampleschoolteacher.ui.courses.class_data.class_students;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Adapter.StudentsAdapter;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.Eventbus.SendMessageEvent;
import com.example.sampleschoolteacher.Eventbus.StudentAttendanceEvent;
import com.example.sampleschoolteacher.Model.AddAttendanceModel;
import com.example.sampleschoolteacher.Model.FCMSendData;
import com.example.sampleschoolteacher.Model.MessageModel;
import com.example.sampleschoolteacher.Model.StudentModel;
import com.example.sampleschoolteacher.Model.TimeModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.Services.IFCMServices;
import com.example.sampleschoolteacher.Services.RetrofitFCMClient;
import com.example.sampleschoolteacher.databinding.FragmentClassStudentsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ClassStudentsFragment extends Fragment {

    private ClassStudentsViewModel classStudentsViewModel;
    private FragmentClassStudentsBinding binding;

    TextView txt_no_users;
    RecyclerView recycler_students;
    StudentsAdapter adapter;

    Button btn_upload_attendance;

    String className,subjectName;
    int classPos;
    List<TimeModel> timeModelList = new ArrayList<>();
    List<String>studentsAttendance = new ArrayList<>();
    List<StudentModel>studentModelList = new ArrayList<>();
    Boolean currentClass = false;

    private IFCMServices ifcmServices;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public static ClassStudentsFragment newInstance() {
        return new ClassStudentsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        classStudentsViewModel = new ViewModelProvider(this).get(ClassStudentsViewModel.class);
        binding = FragmentClassStudentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recycler_students = (RecyclerView) root.findViewById(R.id.recycler_students);
        txt_no_users = (TextView) root.findViewById(R.id.txt_no_users);
        btn_upload_attendance = binding.btnUploadAttendance;

        if (getArguments() != null) {
            className = getArguments().getString("className");
            classPos = getArguments().getInt("classPos");
            subjectName = getArguments().getString("subjectName");
        } else
            Snackbar.make(root, "No data received !", Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getActivity().onBackPressed();
                        }
                    }).show();

        ifcmServices = RetrofitFCMClient.getInstance().create(IFCMServices.class);

        List<String> days = new ArrayList<>();
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date().getTime());
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");

        String currentTime = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date());
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        List<String> subjects = new ArrayList<>();
        for (int j = 0; j < Common.currentTeacher.getTeachingClasses().size(); j++) {
            subjects.add(Common.currentTeacher.getTeachingClasses().get(j).getSubjectName());
        }


        if (days.contains(today) && Common.classModelList.getValue() != null) {
            timeModelList = Common.classModelList.getValue().get(classPos).getTimetable().get(days.indexOf(today)).getTime();
            for (int i = 0; i < timeModelList.size(); i++) {
                if (subjects.contains(timeModelList.get(i).getSubjName())) {
                    try {
                        Date timeFrom = parser.parse((new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date())) + " " + timeModelList.get(i).getTimeFrom());
                        Date timeTo = parser.parse((new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date())) + " " + timeModelList.get(i).getTimeTo());
                        if (parser.parse(currentTime).after(timeFrom) && parser.parse(currentTime).before(timeTo)) {
                            currentClass = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (currentClass) {
            btn_upload_attendance.setVisibility(View.VISIBLE);
            btn_upload_attendance.setOnClickListener(uploadAttendance);
        }



        classStudentsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), s -> {
            Snackbar.make(root, "" + s, Snackbar.LENGTH_SHORT).show();
        });

        classStudentsViewModel.getStudentModelListMutable(className).observe(getViewLifecycleOwner(), studentModels -> {
            if (studentModels.size() > 0) {
                studentModelList=studentModels;
                txt_no_users.setVisibility(View.GONE);
                adapter = new StudentsAdapter(getContext(), studentModels, currentClass,subjectName);
                recycler_students.setAdapter(adapter);
            }
        });
        recycler_students.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_students.setLayoutManager(linearLayoutManager);
        recycler_students.addItemDecoration(new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation()));


        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    View.OnClickListener uploadAttendance = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Upload Attendance");
        builder.setMessage("Present "+(studentModelList.size()-studentsAttendance.size()));
        builder.setMessage("Absent "+studentsAttendance.size());
        builder.setPositiveButton("Upload", (dialog, which) -> {
            if (studentModelList.size()>0)
            {
                for (int i=0;i<studentModelList.size();i++)
                {
                    int j=i;
                    AddAttendanceModel addAttendanceModel;
                    if (studentModelList.get(i).getUid().equals(studentsAttendance.get(i)))
                    {
                        addAttendanceModel = new AddAttendanceModel(
                                new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date()),
                                Common.currentTeacher.getTeacherName(), "Absent");
                    }
                    else {
                        addAttendanceModel = new AddAttendanceModel(
                                new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date()),
                                Common.currentTeacher.getTeacherName(), "Present");

                    }
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("Students")
                            .document(studentModelList.get(i).getUid())
                            .collection("attendance")
                            .document(subjectName)
                            .update("attendance", FieldValue.arrayUnion(addAttendanceModel))
                            .addOnSuccessListener(unused -> {
                                studentsAttendance.remove(studentModelList.get(j).getUid());
                                dialog.dismiss();
                            });
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
    };

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onListChangedEvent(StudentAttendanceEvent event)
    {
        if (event.getStatus().equals("Absent"))
        {
            studentsAttendance.add(event.getStudentUid());
        }
        if (event.getStatus().equals("Present"))
        {
            studentsAttendance.remove(event.getStudentUid());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendMessageEvent(SendMessageEvent event) {
        Map<String, String> notiData = new HashMap<>();
        notiData.put("title", event.getTitle());
        notiData.put("content", event.getMessage());

        FCMSendData sendData = new FCMSendData(event.getToken(), notiData);
        compositeDisposable.add(ifcmServices.sendNotification(sendData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fcmResponse -> {
                    if (fcmResponse.getSuccess() == 1) {
                        adapter.notifyDataSetChanged();
                    }
                }));
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.message_all)
        {
            if (studentModelList != null && studentModelList.size()>0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.send_message_dialog, null);
                TextView txt_message_from = (TextView) itemView.findViewById(R.id.txt_message_from);
                EditText edt_message_body = (EditText) itemView.findViewById(R.id.edt_message_body);
                EditText edt_message_title = (EditText) itemView.findViewById(R.id.edt_message_title);
                Button btn_message_cancel = (Button)itemView.findViewById(R.id.btn_message_cancel);
                Button btn_message_send = (Button)itemView.findViewById(R.id.btn_message_send);
                builder.setView(itemView);
                AlertDialog dialog = builder.create();
                dialog.show();

                btn_message_cancel.setOnClickListener(v -> dialog.dismiss());

                btn_message_send.setOnClickListener(v -> {
                    txt_message_from.setText(Common.currentTeacher.getTeacherName());
                    MessageModel messageModel = new MessageModel();
                    messageModel.setTitle(edt_message_title.getText().toString().trim());
                    messageModel.setFrom(Common.currentTeacher.getTeacherName());
                    messageModel.setBody(edt_message_body.getText().toString().trim());
                    messageModel.setDate(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
                    messageModel.setTime(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date()));

                    for (int i=0;i<studentModelList.size();i++){
                        int j =i;
                        messageModel.setMsgId(FirebaseFirestore.getInstance()
                                .collection("Students")
                                .document(studentModelList.get(i).getUid())
                                .collection("Messages")
                                .document().getId());
                        FirebaseFirestore.getInstance()
                                .collection("Students")
                                .document(studentModelList.get(i).getUid())
                                .collection("Messages")
                                .document(messageModel.getMsgId())
                                .set(messageModel)
                                .addOnSuccessListener(unused -> EventBus.getDefault().post(new SendMessageEvent(edt_message_title.getText().toString().trim(),
                                        edt_message_body.getText().toString().trim(),
                                        studentModelList.get(j).getStudentToken())))
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show());
                        dialog.dismiss();
                    }
                });

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}