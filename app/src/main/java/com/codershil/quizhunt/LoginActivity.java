package com.codershil.quizhunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.codershil.quizhunt.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding ;
    FirebaseAuth auth ;
    ProgressDialog dialog ;
    ProgressDialog dialogEmailSent ;
    EditText edtCurrentEmail ;
    AlertDialog dialogForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("logging in...");
        dialogEmailSent = new ProgressDialog(this);
        dialogEmailSent.setCancelable(false);
        dialogEmailSent.setMessage("verifying your email...");

        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.forget_password,null);
        Button btnCancelForget = view.findViewById(R.id.btnCancelForget);
        Button btnSubmitForget = view.findViewById(R.id.btnSubmitForget);
        edtCurrentEmail = view.findViewById(R.id.edtCurrentEmail);

        dialogForgetPassword = new AlertDialog.Builder(LoginActivity.this)
                .setView(view)
                .create();

        if (auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email, pass;
                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                if (email.isEmpty()){
                    binding.emailBox.setError("please enter email");
                    return;
                }
                if (pass.isEmpty()){
                    binding.passwordBox.setError("please enter password");
                    return;
                }
                    dialog.show();
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        binding.txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgetPassword.show();
            }
        });

        btnCancelForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgetPassword.cancel();
            }
        });

        btnSubmitForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCurrentEmail.getText().toString().isEmpty()){
                    edtCurrentEmail.setError("please enter email first");
                    return;
                }
                dialogEmailSent.show();
                auth.sendPasswordResetEmail(edtCurrentEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialogEmailSent.dismiss();
                                    dialogForgetPassword.dismiss();
                                    Toast.makeText(LoginActivity.this, "Reset password instruction email is send to your email address", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    dialogEmailSent.dismiss();
                                    dialogForgetPassword.dismiss();
                                    Toast.makeText(LoginActivity.this, "Email not exists,try again ", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogEmailSent.dismiss();
                                dialogForgetPassword.dismiss();
                                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}