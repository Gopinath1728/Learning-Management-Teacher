package com.example.sampleschoolteacher.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sampleschoolteacher.Common.Common;
import com.example.sampleschoolteacher.R;
import com.example.sampleschoolteacher.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


    private FragmentProfileBinding binding;

    CircleImageView img_teacher;
    TextView txt_tcr_name, txt_tcr_uid, txt_tcr_phone, txt_tcr_email, txt_tcr_address, txt_tcr_proc;
    TableRow table_row_proctor;
    Button btn_tcr_reset_pwd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        img_teacher = binding.imgTeacher;
        txt_tcr_name = binding.txtTcrName;
        txt_tcr_uid = binding.txtTcrUid;
        txt_tcr_phone = binding.txtTcrPhone;
        txt_tcr_email = binding.txtTcrEmail;
        txt_tcr_address = binding.txtTcrAddress;
        txt_tcr_proc = binding.txtTcrProc;
        table_row_proctor = binding.tableRowProctor;
        btn_tcr_reset_pwd = binding.btnTcrResetPwd;

        if (Common.currentTeacher != null) {
            if (!Common.currentTeacher.getTeacherImage().equalsIgnoreCase("Null"))
                Glide.with(getContext()).load(Common.currentTeacher.getTeacherImage()).into(img_teacher);
            txt_tcr_name.setText(new StringBuilder(Common.currentTeacher.getTeacherName()));
            txt_tcr_uid.setText(new StringBuilder(Common.currentTeacher.gettUid()));
            txt_tcr_phone.setText(new StringBuilder(Common.currentTeacher.getTeacherPhone()));
            txt_tcr_email.setText(new StringBuilder(Common.currentTeacher.getTeacherEmail()));
            txt_tcr_address.setText(new StringBuilder(Common.currentTeacher.getTeacherAddress()));
            if (Common.currentTeacher.getProctor())
                txt_tcr_name.setText(new StringBuilder(Common.currentTeacher.getTeacherName()));
            else
                table_row_proctor.setVisibility(View.GONE);
        }

        btn_tcr_reset_pwd.setOnClickListener(passwordReset);

        return root;
    }

    View.OnClickListener passwordReset = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.password_reset_dialog, null);
        LinearLayout linear_current_password = (LinearLayout) itemView.findViewById(R.id.linear_current_password);
        LinearLayout linear_new_pwd = (LinearLayout) itemView.findViewById(R.id.linear_new_pwd);
        EditText edt_current_pwd = (EditText)itemView.findViewById(R.id.edt_current_pwd);
        EditText edt_new_pwd = (EditText)itemView.findViewById(R.id.edt_new_pwd);
        EditText edt_confirm_pwd = (EditText)itemView.findViewById(R.id.edt_confirm_pwd);
        Button btn_crnt_next = (Button) itemView.findViewById(R.id.btn_crnt_next);
        Button btn_pwd_reset = (Button) itemView.findViewById(R.id.btn_pwd_reset);
        Button btn_crnt_cancel = (Button) itemView.findViewById(R.id.btn_crnt_cancel);
        Button btn_pwd_cancel = (Button) itemView.findViewById(R.id.btn_pwd_cancel);
        builder.setView(itemView);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btn_crnt_cancel.setOnClickListener(v14 -> dialog.dismiss());
        btn_pwd_cancel.setOnClickListener(v15 -> dialog.dismiss());
        btn_crnt_next.setOnClickListener(v1 -> {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(Common.currentTeacher.getTeacherEmail(),
                    edt_current_pwd.getText().toString().trim())
                    .addOnSuccessListener(authResult -> {
                        linear_current_password.setVisibility(View.GONE);
                        linear_new_pwd.setVisibility(View.VISIBLE);
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show());

        });
        btn_pwd_reset.setOnClickListener(v12 -> {
            if (edt_new_pwd.getText().toString().trim().equals(edt_confirm_pwd.getText().toString().trim()))
            {

                FirebaseAuth.getInstance().getCurrentUser().updatePassword(edt_confirm_pwd.getText().toString().trim())
                        .addOnSuccessListener(unused -> {
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            firestore.collection("Teachers")
                                    .document(Common.currentTeacher.getUid())
                                    .update("teacherPassword",edt_confirm_pwd.getText().toString().trim())
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Snackbar.make(binding.getRoot(),""+e,Snackbar.LENGTH_LONG)
                                                    .setAction("OK", v13 -> {
                                                        dialog.dismiss();
                                                    }).show();
                                        }
                                    })
                                    .addOnSuccessListener(unused1 -> {
                                        Snackbar.make(binding.getRoot(),"Password Updated Successfully",Snackbar.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    });
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}