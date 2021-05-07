package com.codershil.quizhunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.codershil.quizhunt.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding ;
    Question question;
    CountDownTimer timer ;
    FirebaseFirestore database;

    int correctAnswers = 0 ;
    String catId ;
    String noOfQuestions ;
    ArrayList<Question> questions = new ArrayList<>();
    int randomNumber ;
    int index = 0 ;
    boolean canAnswer = true ;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading questions...");
        progressDialog.setCancelable(false);

        catId = getIntent().getStringExtra("catId");
        noOfQuestions = getIntent().getStringExtra("noOfQuestions");

        Random random = new Random();
        randomNumber = random.nextInt(Integer.parseInt(noOfQuestions));
        loadQuestions();
        resetTimer();

        binding.btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClicked();
            }
        });
        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    public void loadQuestions(){
        progressDialog.show();
        database.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index",randomNumber).orderBy("index")
                .limit(5)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size()<5){

                            database.collection("categories")
                                    .document(catId)
                                    .collection("questions")
                                    .whereLessThanOrEqualTo("index", randomNumber).orderBy("index")
                                    .limit(5)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                                                Question question = snapshot.toObject(Question.class);
                                                questions.add(question);
                                            }
                                            Collections.shuffle(questions);
                                            setNextQuestion();
                                            progressDialog.dismiss();
                                        }
                                    });

                        }
                        else {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                            Collections.shuffle(questions);
                            progressDialog.dismiss();
                            setNextQuestion();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(QuizActivity.this, "Failed To Load Questions Try Restarting App !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void setNextQuestion(){
        if (timer != null){
            timer.cancel();
        }
        timer.start();
        if (index<questions.size()){
            binding.txtQuestionCounter.setText(String.format("%d/%d",(index+1),questions.size()));
            question = questions.get(index);
            binding.txtQuestion.setText(question.getQuestion());
            binding.txtOption1.setText(question.getOption1());
            binding.txtOption2.setText(question.getOption2());
            binding.txtOption3.setText(question.getOption3());
            binding.txtOption4.setText(question.getOption4());
        }
    }

    public void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if (selectedAnswer.equals(question.getAnswer())){
            correctAnswers++;
            textView.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_right,null));
        }
        else{
            textView.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_wrong,null));
            showAnswer();
        }
        showAnswer();
    }

    public void resetBackground(){
        binding.txtOption1.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_unselected,null));
        binding.txtOption2.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_unselected,null));
        binding.txtOption3.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_unselected,null));
        binding.txtOption4.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_unselected,null));
    }

    void resetTimer(){
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtTimer.setText(String.valueOf(millisUntilFinished/1000));
            }
            @Override
            public void onFinish() {
                canAnswer = false ;
                timer.cancel();
            }
        };
    }

    public void showAnswer(){
        if (question.getAnswer().equals(binding.txtOption1.getText().toString())){
            binding.txtOption1.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_right,null));
        }
        else if (question.getAnswer().equals(binding.txtOption2.getText().toString())){
            binding.txtOption2.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_right,null));
        }
        else if (question.getAnswer().equals(binding.txtOption3.getText().toString())){
            binding.txtOption3.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_right,null));
        }
        else {
            binding.txtOption4.setBackground(ResourcesCompat.getDrawable(this.getResources(),R.drawable.option_right,null));
        }

    }

    public void onOptionClicked(View view){
        switch (view.getId()){
            case R.id.txtOption1:
            case R.id.txtOption2:
            case R.id.txtOption3:
            case R.id.txtOption4:
                if (canAnswer) {
                    if (timer != null) {
                        timer.cancel();
                    }
                    TextView selected = (TextView) view;
                    checkAnswer(selected);
                    canAnswer = false;
                    break;
                }
                else{
                    Toast.makeText(this, "time over! please click next", Toast.LENGTH_SHORT).show(); 
                }
        }
    }

    public void onNextButtonClicked(){
        canAnswer = true ;
        if (index<questions.size()-1) {
            timer.start();
            index++;
            setNextQuestion();
            resetBackground();
        }
        else {
            binding.btnNextQuestion.setEnabled(false);
            timer.cancel();
            Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("correct",correctAnswers);
            intent.putExtra("total",questions.size());
            intent.putExtra("catId",catId);
            intent.putExtra("noOfQuestions",noOfQuestions);
            finish();
            startActivity(intent);
        }
    }

}