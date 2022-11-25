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
import hu.petrik.varosok.databinding.ActivityListBinding;

public class ListActivity extends AppCompatActivity {


    ActivityListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        RequestTask task = new RequestTask(MainActivity.BASE_URL);
        task.execute();

        binding.backBtn.setOnClickListener( e -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private class RequestTask extends AsyncTask<Void, Void, Response> {
        private String requestUrl;
        private String requestMethod;

        public RequestTask(String requestUrl) {
            this.requestUrl = requestUrl;
            this.requestMethod = "GET";
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                response = RequestHandler.get(requestUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response == null) {
                Toast.makeText(ListActivity.this, "Nem sikerült kapcsolódni a szerverre", Toast.LENGTH_SHORT).show();
                return;
            }
            if (response.getResponseCode() >= 400) {
                Toast.makeText(ListActivity.this, response.getContent(), Toast.LENGTH_SHORT).show();
                return;
            }
            if ("GET".equals(requestMethod)) {
                String content = response.getContent();
                binding.listTextView.setText(content);
            } else {
                if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
                    RequestTask task = new RequestTask(MainActivity.BASE_URL);
                    task.execute();
                }
            }
        }
    }




}