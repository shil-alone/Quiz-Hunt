package com.codershil.quizhunt.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codershil.quizhunt.databinding.FragmentWalletBinding;
import com.codershil.quizhunt.models.User;
import com.codershil.quizhunt.models.WithdrawRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletFragment extends Fragment {


    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentWalletBinding binding ;
    FirebaseFirestore database;
    User user ;
    String uid;
    long coins ;
    ProgressDialog dialog ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(inflater,container,false);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("sending withdraw request...");
        dialog.setCancelable(false);

        database = FirebaseFirestore.getInstance();
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                coins = user.getCoins();
                binding.txtCollectedCoins.setText(String.valueOf(user.getCoins()));
            }
        });
        
        binding.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if(user.getCoins() >= 50000){
                    uid =FirebaseAuth.getInstance().getUid();
                    String paypalEmail = binding.edtPaypalEmail.getText().toString();
                    if (paypalEmail.isEmpty()){
                        binding.edtPaypalEmail.setError("please enter email first");
                        dialog.dismiss();
                        return;
                    }
                    WithdrawRequest request = new WithdrawRequest(uid,paypalEmail,user.getName());
                    
                    database.collection("withdraws")
                            .document(uid)
                            .set(request)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            database.collection("users")
                                    .document(uid)
                                    .update("coins", FieldValue.increment(-50000))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    binding.txtCollectedCoins.setText(String.valueOf(coins-50000));
                                    Toast.makeText(getContext(), "wallet coins updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Request sent successfully .you will receive your reward within 48 hours", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "unable to send request , try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else{
                    dialog.dismiss();
                    Toast.makeText(getContext(), "You Need More coins for withdrawal", Toast.LENGTH_SHORT).show();
                }
                
            }
        });


        return binding.getRoot();
    }
}