package hu.petrik.varosok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import hu.petrik.api.RequestHandler;
import hu.petrik.api.Response;
import hu.petrik.varosok.databinding.ActivityInsertBinding;

public class InsertActivity extends AppCompatActivity {


    private class RequestTask extends AsyncTask<Void, Void, Response> {
        private String requestUrl;
        private String requestBody;

        public RequestTask(String requestUrl, String requestBody) {
            this.requestUrl = requestUrl;
            this.requestBody = requestBody;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;

            try {
                response = RequestHandler.post(requestUrl, requestBody);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response == null) {
                Toast.makeText(InsertActivity.this, "Nem sikerült kapcsolódni a szerverre", Toast.LENGTH_SHORT).show();
                return;
            }
            if (response.getResponseCode() >= 400) {
                Toast.makeText(InsertActivity.this, response.getContent(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
                Toast toast = Toast.makeText(InsertActivity.this, "Sikeres hozzáadás", Toast.LENGTH_SHORT);
                toast.show();
                binding.setVaros(new Varos(0, "", "", 0));
            }
        }
    }

    ActivityInsertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.setVaros(new Varos(0, "", "", 0));
        binding.backBtn.setOnClickListener( e -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        binding.insertBtn.setOnClickListener(e-> insert());
    }

    private void insert() {
        Varos varos = binding.getVaros();
        if (varos.getLakossag() == 0) {
            Toast.makeText(InsertActivity.this, "A lakosság mező kitöltése kötelező!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (varos.getNev().isEmpty()) {
            Toast.makeText(InsertActivity.this, "A név mező kitöltése kötelező!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (varos.getOrszag().isEmpty()) {
            Toast.makeText(InsertActivity.this, "Az ország mező kitöltése kötelező!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Gson converter = new Gson();
        String json = converter.toJson(varos);
        RequestTask task = new RequestTask(MainActivity.BASE_URL, json);
        task.execute();

    }


}

