package com.example.mcimdo;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainWindowAplication extends AppCompatActivity {

    Button btn_profile;
    String token;
    private LinearLayout bookButtonsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window_aplication);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        ScrollView scrollView = findViewById(R.id.scrollView);
        bookButtonsContainer = findViewById(R.id.bookButtonsContainer);


        btn_profile = findViewById(R.id.profile_btn);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWindowAplication.this, ProfileActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
        new GetBookTask().execute();
    }
    private void createBookButtons(JSONArray recommendations) {
        int buttonWidth = getResources().getDimensionPixelSize(R.dimen.button_width);
        int buttonHeight = getResources().getDimensionPixelSize(R.dimen.button_height);
        try {
            for (int i = 0; i < recommendations.length(); i++) {
                JSONObject recommendationsObject = recommendations.getJSONObject(i);
                String book = recommendationsObject.getString("title") + "\n" + recommendationsObject.getString("author");
                String id_book = recommendationsObject.getString("id");

                Button button = new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight));
                button.setText(book);
                button.setAllCaps(false);
                button.setPadding(10, 10, 10, 10);
                button.setBackgroundResource(R.drawable.button_background); // замените на фон кнопки по вашему выбору

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 10, 0, 10);
                button.setLayoutParams(params);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String selectedbook = book;
                        String selectionId = id_book;

                        Intent intent = new Intent(MainWindowAplication.this, BookDetailsActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("idbook", selectionId);
                        startActivity(intent);

                    }
                });

                bookButtonsContainer.addView(button);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class GetBookTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            JSONArray genres = null;

            try {
                URL url = new URL("https://petrusu.bsite.net/api/ForAllUsers/recommendations");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + token);

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder response = new StringBuilder();

                    if (inputStream != null) {
                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                    }

                    genres = new JSONArray(response.toString());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return genres;
        }

        @Override
        protected void onPostExecute(JSONArray recommendations) {
            if (recommendations != null) {
                createBookButtons(recommendations);
            } else {
                Toast.makeText(MainWindowAplication.this, "Failed to add genre to favorites", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

