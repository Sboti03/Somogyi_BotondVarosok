package hu.petrik.varosok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;

import hu.petrik.varosok.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static String BASE_URL = "https://retoolapi.dev/sjeIFG/varosok";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.insertBtn.setOnClickListener((e)-> {
            Intent intent = new Intent(this, InsertActivity.class);
            startActivity(intent);
            finish();
        });

        binding.listBtn.setOnClickListener((e)-> {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
            finish();
        });

    }
}