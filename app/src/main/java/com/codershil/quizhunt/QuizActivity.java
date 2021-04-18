package com.codershil.quizhunt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.codershil.quizhunt.databinding.ActivityQuizBinding;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




    }
}