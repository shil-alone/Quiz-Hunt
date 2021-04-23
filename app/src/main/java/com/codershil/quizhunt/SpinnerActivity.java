package com.codershil.quizhunt;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codershil.quizhunt.SpinWheel.LuckyWheelView;
import com.codershil.quizhunt.SpinWheel.model.LuckyItem;
import com.codershil.quizhunt.databinding.ActivitySpinnerBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SpinnerActivity extends AppCompatActivity {

    ActivitySpinnerBinding binding;
    long coin = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        List<LuckyItem> data  = new ArrayList<>();

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = "5";
        luckyItem1.secondaryText = "Coins";
        luckyItem1.color = Color.parseColor("#eceff1");
        luckyItem1.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = "10";
        luckyItem2.secondaryText = "Coins";
        luckyItem2.color = Color.parseColor("#43A047");
        luckyItem2.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = "15";
        luckyItem3.secondaryText = "Coins";
        luckyItem3.color = Color.parseColor("#eceff1");
        luckyItem3.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = "20";
        luckyItem4.secondaryText = "Coins";
        luckyItem4.color = Color.parseColor("#dc0000");
        luckyItem4.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = "25";
        luckyItem5.secondaryText = "Coins";
        luckyItem5.color = Color.parseColor("#eceff1");
        luckyItem5.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = "30";
        luckyItem6.secondaryText = "Coins";
        luckyItem6.color = Color.parseColor("#008bff");
        luckyItem6.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem6);

        binding.wheelView.setData(data);
        binding.wheelView.setRound(5);

        binding.btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int randomNumber = random.nextInt(6);
                binding.wheelView.startLuckyWheelWithTargetIndex(randomNumber);
            }
        });

        binding.wheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                updateCoins(index);
            }
        });
    }

    public void updateCoins(int index){
        switch (index){
            case 0:
                coin = 5;
                break;
            case 1:
                coin = 10;
                break;
            case 2:
                coin = 15;
                break;
            case 3:
                coin = 20;
                break;
            case 4:
                coin = 25;
                break;
            case 5:
                coin = 30;
                break;
        }
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(coin))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SpinnerActivity.this, coin +" Coins Added In Wallet", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}