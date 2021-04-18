package com.codershil.quizhunt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codershil.quizhunt.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding ;
    Question question;
    CountDownTimer timer ;
    FirebaseFirestore database;

    ArrayList<Question> questions = new ArrayList<>();
    int index = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        String catId = getIntent().getStringExtra("catId");

        Random random = new Random();
        int rand = random.nextInt(10);

        database.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index",rand).orderBy("index")
                .limit(5)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size()<5){

                            database.collection("categories")
                                    .document(catId)
                                    .collection("questions")
                                    .whereLessThanOrEqualTo("index",rand).orderBy("index")
                                    .limit(5)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                                                Question question = snapshot.toObject(Question.class);
                                                questions.add(question);
                                            }
                                            setNextQuestion();
                                        }
                                    });


                        }
                        else {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                            setNextQuestion();
                        }
                    }
                });

        resetTimer();
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
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else{
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            showAnswer();
        }
        showAnswer();
    }

    public void resetBackground(){
        binding.txtOption1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.txtOption2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.txtOption3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.txtOption4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    void resetTimer(){
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtTimer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

            }
        };
    }

    public void showAnswer(){
        if (question.getAnswer().equals(binding.txtOption1.getText().toString())){
            binding.txtOption1.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else if (question.getAnswer().equals(binding.txtOption2.getText().toString())){
            binding.txtOption2.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else if (question.getAnswer().equals(binding.txtOption3.getText().toString())){
            binding.txtOption3.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else {
            binding.txtOption4.setBackground(getResources().getDrawable(R.drawable.option_right));
        }

    }

    public void onButtonClick(View view){
        switch (view.getId()){

            case R.id.txtOption1:
            case R.id.txtOption2:
            case R.id.txtOption3:
            case R.id.txtOption4:
                if (timer != null){
                    timer.cancel();
                }
                TextView selected = (TextView) view ;
                checkAnswer(selected);
                break;

            case R.id.btnNextQuestion:
                if (index<questions.size()) {
                    index++;
                    setNextQuestion();
                    resetBackground();
                }
                else {
                    Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}