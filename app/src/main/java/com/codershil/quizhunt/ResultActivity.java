package com.codershil.quizhunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codershil.quizhunt.databinding.ActivityResultBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    int POINTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String catId = getIntent().getStringExtra("catId");
        String noOfQuestions = getIntent().getStringExtra("noOfQuestions");

        int correctAnswers = getIntent().getIntExtra("correct",0);
        int totalQuestions = getIntent().getIntExtra("total",0);

        int points = correctAnswers * POINTS;
        binding.txtScore.setText(String.format("%d/%d",correctAnswers,totalQuestions));
        binding.txtEarnedCoins.setText(String.valueOf(points));

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(points));


        binding.btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,QuizActivity.class);
                intent.putExtra("catId",catId);
                intent.putExtra("noOfQuestions",noOfQuestions);
                finish();
                startActivity(intent);
            }
        });

        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.putExtra(Intent.EXTRA_TEXT,"Hey , i won "+ points + " coins on Quiz Hunt . Checkout this awesome app on PlayStore");
                sharingIntent.setType("text/plain");
                startActivity(Intent.createChooser(sharingIntent,"Share Your Points Using"));

            }
        });

    }
}