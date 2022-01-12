package com.example.sampleschoolteacher.ui.timetable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.HomeActivity;
import com.example.sampleschoolteacher.Model.AddAttendanceModel;
import com.example.sampleschoolteacher.Model.ClassJoinModel;
import com.example.sampleschoolteacher.Model.StudentModel;
import com.example.sampleschoolteacher.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class OnlineClassFragment extends Fragment {

    private List<ClassJoinModel> classJoinModelList = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private String[] failedList;
    private List<StudentModel> studentModelList = new ArrayList<>();
    Button btn_online_back;
    CardView card_end_class;
    TextView txt_subject_name, txt_tcr_class_name, txt_time_from, txt_time_to, txt_class_duration, txt_students_list;

    private String className, subjectName, clsTimeFrom, clsTimeTo;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    };

    private void onBroadcastReceived(Intent intent) {
        if (intent != null) {
            BroadcastEvent event = new BroadcastEvent(intent);

            switch (event.getType()) {
                case CONFERENCE_JOINED:
                    card_end_class.setVisibility(View.GONE);
                    clsTimeFrom = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date());
                    Timber.i("Conference Joined with url%s", event.getData().get("url"));
                    break;
                case PARTICIPANT_JOINED:
                    Timber.i("Participant joined%s", event.getData().get("name"));
                    String name = (String) event.getData().get("name");
                    if (name != null && !name.equals(Common.currentTeacher.getTeacherName()))
                        userJoined(name);
                    break;
                case PARTICIPANT_LEFT:
                    String leftUserName = (String) event.getData().get("name");
                    if (leftUserName != null && !leftUserName.equals(Common.currentTeacher.getTeacherName()))
                        userLeft(leftUserName);
                    break;
                case CONFERENCE_TERMINATED:
                    card_end_class.setVisibility(View.VISIBLE);
                    clsTimeTo = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date());
                    conferenceEnd();
                    break;

            }
        }
    }

    private void conferenceEnd() {
        if (classJoinModelList.size() > 0) {
            failedList = new String[classJoinModelList.size()];
            for (int i = 0; i < classJoinModelList.size(); i++) {
                if (classJoinModelList.get(i).getUserName() != null &&
                        classJoinModelList.get(i).getJoinTime() != null &&
                        classJoinModelList.get(i).getLeftTime() == null)
                    classJoinModelList.get(i).setLeftTime(new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date()));
            }
            Date classTimeFrom, classTimeTo;
            float totalDuration = 0;

            txt_class_duration.setText(new StringBuilder((int) totalDuration).append(" minutes"));
            txt_students_list.setText(new StringBuilder(classJoinModelList.size()));
            txt_subject_name.setText(new StringBuilder(subjectName));
            txt_tcr_class_name.setText(new StringBuilder(className));
            txt_time_from.setText(new StringBuilder(clsTimeFrom));
            txt_time_to.setText(new StringBuilder(clsTimeTo));

            for (int i = 0; i < classJoinModelList.size(); i++) {
                for (int j = 0; j < classJoinModelList.size(); j++) {
                    if (classJoinModelList.get(j).getUserName().equals(studentModelList.get(j).getStudentName())) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                            Date timeFrom = format.parse(classJoinModelList.get(j).getJoinTime());
                            Date timeTo = format.parse(classJoinModelList.get(j).getLeftTime());
                            int duration = (int) TimeUnit.MILLISECONDS.toMinutes(Math.abs(timeTo.getTime() - timeFrom.getTime()));

                            classTimeFrom = format.parse(clsTimeFrom);
                            classTimeTo = format.parse(clsTimeTo);
                            totalDuration = TimeUnit.MILLISECONDS.toMinutes(Math.abs(classTimeTo.getTime() - classTimeFrom.getTime()));

                            int factor = (int) (100 * (duration / totalDuration));
                            AddAttendanceModel addAttendanceModel;
                            if (factor > 75) {
                                addAttendanceModel = new AddAttendanceModel(
                                        new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date()),
                                        Common.currentTeacher.getTeacherName(), "present");
                            } else {
                                addAttendanceModel = new AddAttendanceModel(
                                        new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date()),
                                        Common.currentTeacher.getTeacherName(), "absent");
                            }
                            int k = j;
                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            firebaseFirestore.collection("Students")
                                    .document(studentModelList.get(j).getUid())
                                    .collection("attendance")
                                    .document(subjectName)
                                    .update("attendance", FieldValue.arrayUnion(addAttendanceModel))
                                    .addOnFailureListener(e -> {
                                        failedList[k] = studentModelList.get(k).getStudentName();
                                    })
                                    .addOnSuccessListener(unused -> classJoinModelList.remove(k));
                        } catch (Exception e) {
                            Log.d("Time Conversion Error: ", e.getMessage());
                        }
                        break;
                    }
                }

            }

            if (failedList.length > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Upload Failed");
                builder.setMessage("Could not Upload the attendance of these students. Do you want to retry?");
                builder.setItems(failedList, null);
                builder.setPositiveButton("Retry", (dialog, which) -> {
                    conferenceEnd();
                    dialog.dismiss();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    getActivity().onBackPressed();
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            } else
                getActivity().onBackPressed();
        }
    }

    private void userLeft(String leftUserName) {
        for (int i = 0; i < classJoinModelList.size(); i++) {
            if (classJoinModelList.get(i).getUserName().equals(leftUserName)) {
                classJoinModelList.get(i).setLeftTime(new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date()));
            }
        }
    }

    private void userJoined(String name) {
        if (!names.isEmpty() && names.contains(name)) {
            for (int i = 0; i < classJoinModelList.size(); i++) {
                if (classJoinModelList.get(i).getUserName().equals(name)) {
                    classJoinModelList.get(i).setLeftTime(null);
                    return;
                }
            }
        } else {
            names.add(name);
            ClassJoinModel classJoinModel = new ClassJoinModel();
            classJoinModel.setUserName(name);
            classJoinModel.setJoinTime(new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date()));
            classJoinModelList.add(classJoinModel);
        }
    }

    View.OnClickListener goBack = v -> {
        getActivity().onBackPressed();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_online_class, container, false);

        assert getArguments() != null;
        className = getArguments().getString("className");
        subjectName = getArguments().getString("subjectName");
        String timeFrom = getArguments().getString("timeFrom");
        String timeTo = getArguments().getString("timeTo");

        String time = timeFrom.substring(0,2)+"-"+timeTo.substring(0,2);

        String l = "sampleSchool-"+className+"-"+time+"/"+subjectName;

        btn_online_back = (Button) root.findViewById(R.id.btn_online_back);
        btn_online_back.setOnClickListener(goBack);

        card_end_class = (CardView) root.findViewById(R.id.card_end_class);
        txt_subject_name = (TextView) root.findViewById(R.id.txt_subject_name);
        txt_tcr_class_name = (TextView) root.findViewById(R.id.txt_tcr_class_name);
        txt_time_from = (TextView) root.findViewById(R.id.txt_time_from);
        txt_time_to = (TextView) root.findViewById(R.id.txt_time_to);
        txt_class_duration = (TextView) root.findViewById(R.id.txt_class_duration);
        txt_students_list = (TextView) root.findViewById(R.id.txt_students_list);

        studentModelList = getStudents(className);
        String link = l.replaceAll("\\s","-");
        URL serverURL;
        try {
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        registerForBroadcastMessages();


        JitsiMeetUserInfo userInfo = new JitsiMeetUserInfo();
        userInfo.setDisplayName(Common.currentTeacher.getTeacherName());
        if (!Common.currentTeacher.getTeacherImage().equalsIgnoreCase("Null")) {
            try {
                URL url = new URL(Common.currentTeacher.getTeacherImage());
                userInfo.setAvatar(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        // Build options object for joining the conference. The SDK will merge the default
        // one we set earlier and this one when joining.
        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom(link)
                .setUserInfo(userInfo)
                .setVideoMuted(true)
                .build();

        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(getContext(), options);


        return root;

    }

    private List<StudentModel> getStudents(String className) {
        List<StudentModel> studentModels = new ArrayList<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Students")
                .whereEqualTo("studentClass", className)
                .orderBy("studentName")
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        Log.d("Student Listen Failed", "" + error);
                        return;
                    }
                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            StudentModel studentModel = documentSnapshot.toObject(StudentModel.class);
                            studentModels.add(studentModel);
                        }
                    }
                });
        return studentModels;
    }

    private void registerForBroadcastMessages() {
        IntentFilter intentFilter = new IntentFilter();

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.getAction());
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
                ... other events
         */
        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.getAction());
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}