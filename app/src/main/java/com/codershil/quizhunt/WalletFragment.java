package com.codershil.quizhunt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codershil.quizhunt.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(inflater,container,false);

        database = FirebaseFirestore.getInstance();
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                binding.txtCollectedCoins.setText(String.valueOf(user.getCoins()));
            }
        });
        
        binding.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(user.getCoins() >= 50000){
                    String uid =FirebaseAuth.getInstance().getUid();
                    String paypalEmail = binding.edtPaypalEmail.getText().toString();
                    if (paypalEmail.isEmpty()){
                        binding.edtPaypalEmail.setError("please enter email first");
                    }
                    WithdrawRequest request = new WithdrawRequest(uid,paypalEmail,user.getName());
                    
                    database.collection("withdraws")
                            .document(uid)
                            .set(request)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Request sent successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    ;
                }
                else{
                    Toast.makeText(getContext(), "You Need More coins for withdrawal", Toast.LENGTH_SHORT).show();
                }
                
            }
        });



        return binding.getRoot();
    }
}