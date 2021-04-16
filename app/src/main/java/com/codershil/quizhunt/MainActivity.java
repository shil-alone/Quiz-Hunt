package com.codershil.quizhunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codershil.quizhunt.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ArrayList<CategoryModel> categories = new ArrayList<>();
        categories.add(new CategoryModel("","mathematics","https://i.pinimg.com/originals/dd/37/03/dd3703c08a47b6b6ad238ebd5ea5e303.png"));
        categories.add(new CategoryModel("","science","https://i.pinimg.com/originals/dd/37/03/dd3703c08a47b6b6ad238ebd5ea5e303.png"));
        categories.add(new CategoryModel("","history","https://i.pinimg.com/originals/dd/37/03/dd3703c08a47b6b6ad238ebd5ea5e303.png"));
        categories.add(new CategoryModel("","Language","https://i.pinimg.com/originals/dd/37/03/dd3703c08a47b6b6ad238ebd5ea5e303.png"));
        categories.add(new CategoryModel("","Puzzle","https://i.pinimg.com/originals/dd/37/03/dd3703c08a47b6b6ad238ebd5ea5e303.png"));
        categories.add(new CategoryModel("","Drama","https://i.pinimg.com/originals/dd/37/03/dd3703c08a47b6b6ad238ebd5ea5e303.png"));

        CategoryAdapter adapter = new CategoryAdapter(this,categories);
        binding.categoryList.setLayoutManager(new GridLayoutManager(this,2));
        binding.categoryList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.wallet){
            Toast.makeText(MainActivity.this, "wallet is clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}