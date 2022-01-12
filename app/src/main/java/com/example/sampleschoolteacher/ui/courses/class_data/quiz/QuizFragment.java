package com.example.sampleschoolteacher.ui.courses.class_data.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleschoolteacher.Adapter.QuizViewAdapter;
import com.example.sampleschoolteacher.Model.QuizListModel;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentQuizBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class QuizFragment extends Fragment {

    private QuizViewModel quizViewModel;
    private FragmentQuizBinding binding;

    RecyclerView recycler_quiz;
    TextView txt_no_quiz;
    int classPos,l;
    String subjectName;
    QuizViewAdapter adapter;
    FloatingActionButton fab_quiz;


    public static QuizFragment newInstance() {
        return new QuizFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        recycler_quiz = (RecyclerView) root.findViewById(R.id.recycler_quiz);
        txt_no_quiz = (TextView) root.findViewById(R.id.txt_no_quiz);
        fab_quiz = (FloatingActionButton) root.findViewById(R.id.fab_quiz);



        if (getArguments()!=null){
            classPos = getArguments().getInt("classPos");
            subjectName = getArguments().getString("subjectName");

            fab_quiz.setOnClickListener(addQuiz);

            quizViewModel.getErrorMessage().observe(getViewLifecycleOwner(), s -> {
                Snackbar.make(root, "" + s, Snackbar.LENGTH_SHORT).show();
            });


            quizViewModel.getMutableLiveData(classPos).observe(getViewLifecycleOwner(), quizListModelList -> {
                if (quizListModelList != null && quizListModelList.size() > 0) {
                    txt_no_quiz.setVisibility(View.GONE);
                    adapter = new QuizViewAdapter(getContext(), quizListModelList);
                    recycler_quiz.setAdapter(adapter);
                }
            });
            recycler_quiz.setHasFixedSize(true);
            recycler_quiz.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }

        return root;
    }

    View.OnClickListener addQuiz = v -> {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.add_quiz_dialog, null);
        EditText edt_quiz_question_no = (EditText) itemView.findViewById(R.id.edt_quiz_question_no);
        Button btn_f_quiz_cancel = (Button) itemView.findViewById(R.id.btn_f_quiz_cancel);
        Button btn_f_quiz_next = (Button) itemView.findViewById(R.id.btn_f_quiz_next);

        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();

        btn_f_quiz_next.setOnClickListener(v14 -> {
            if (edt_quiz_question_no.getText().toString().isEmpty())
                edt_quiz_question_no.setError("Enter Number of Questions");
            else {
                l= Integer.parseInt(edt_quiz_question_no.getText().toString().trim());
                Bundle bundle = new Bundle();
                bundle.putInt("quizNo",l);
                bundle.putString("quizSub",subjectName);
                bundle.putInt("classPos",classPos);
                Navigation.findNavController(v).navigate(R.id.nav_add_quiz,bundle);
                dialog.dismiss();
            }
        });
        btn_f_quiz_cancel.setOnClickListener(v15 -> dialog.dismiss());

    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}