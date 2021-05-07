package com.codershil.quizhunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.codershil.quizhunt.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding ;
    Boolean authFlag = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutContent,new HomeFragment());
        transaction.commit();

        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i){
                    case 0:
                        transaction.replace(R.id.layoutContent,new HomeFragment());
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.layoutContent,new LeaderBoardFragment());
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.layoutContent,new WalletFragment());
                        transaction.commit();
                        break;
                    case 3:
                        transaction.replace(R.id.layoutContent,new ProfileFragment());
                        transaction.commit();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.wallet){
            
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.logout_dialog,null);
            Button btnCancel = view.findViewById(R.id.btnCancel);
            Button btnLogout = view.findViewById(R.id.btnLogout);

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setView(view)
                    .create();
            dialog.show();

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  
                    ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("signing out...");
                    dialog.setCancelable(false);
                    dialog.show();
                    FirebaseAuth auth ;
                    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            if (firebaseAuth.getCurrentUser() == null){
                                if (!authFlag) {
                                    authFlag = true ;
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "logged out successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }
                    };
                    auth = FirebaseAuth.getInstance();
                    auth.addAuthStateListener(authStateListener);
                    auth.signOut();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }
}