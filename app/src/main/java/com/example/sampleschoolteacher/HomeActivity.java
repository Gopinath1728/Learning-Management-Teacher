package com.example.sampleschoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sampleschoolteacher.Common.Common;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sampleschoolteacher.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    public HomeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_courses, R.id.nav_slideshow,
                R.id.nav_students, R.id.nav_class_data, R.id.nav_assignment,
                R.id.nav_weekday, R.id.nav_timetable, R.id.nav_quiz, R.id.nav_add_quiz,
                R.id.nav_examinations, R.id.nav_my_timetable, R.id.nav_my_weekday, R.id.nav_online_class,
                R.id.nav_announcement, R.id.nav_announcement_detail)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        TextView txt_user = (TextView)headerView.findViewById(R.id.txt_user);
        ImageView img_nav_user = (ImageView)headerView.findViewById(R.id.img_nav_user);
        Common.setSpanString("Hey, ",Common.currentTeacher.getTeacherName(),txt_user);
        if (Common.currentTeacher.getTeacherImage().equalsIgnoreCase("Null"))
            img_nav_user.setImageResource(R.drawable.ic_teacher_icon);
        else
            Glide.with(HomeActivity.this).load(Common.currentTeacher.getTeacherImage()).into(img_nav_user);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logout(MenuItem item) {
        Common.currentTeacher=null;
        Common.classModelList=null;
        Common.tcm=null;
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}