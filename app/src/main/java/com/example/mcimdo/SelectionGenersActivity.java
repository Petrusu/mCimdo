package com.example.mcimdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SelectionGenersActivity extends AppCompatActivity {

    String token;
    private LinearLayout genreButtonsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_geners);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        ScrollView scrollView = findViewById(R.id.scrollView);
        genreButtonsContainer = findViewById(R.id.genreButtonsContainer);

        // Выполняем HTTP-запрос к API
        new GetGenresTask().execute();
    }

    private void createGenreButtons(JSONArray genres) {
        try {
            for (int i = 0; i < genres.length(); i++) {
                JSONObject genreObject = genres.getJSONObject(i);
                String genre = genreObject.getString("gener1");
                String id_gener = genreObject.getString("idGener");

                Button button = new Button(this);
                button.setText(genre);
                button.setAllCaps(false);
                button.setTextColor(Color.parseColor("#69516B"));
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
                        String selectedGenre = genre;
                        String selectedId = id_gener;

                        // Выполняем HTTP POST-запрос для добавления предпочитаемого жанра
                        new AddFavoriteGenreTask().execute(selectedId);
                    }
                });

                genreButtonsContainer.addView(button);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class GetGenresTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            JSONArray genres = null;

            try {
                URL url = new URL("https://petrusu.bsite.net/api/ForAllUsers/getgeners");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
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
        protected void onPostExecute(JSONArray genres) {
            if (genres != null) {
                createGenreButtons(genres);
            } else {
                Toast.makeText(SelectionGenersActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class AddFavoriteGenreTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String selectedId = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String response = null;

            try {
                // Формируем URL с выбранным жанром
                String urlString = "https://petrusu.bsite.net/api/ForAllUsers/addfavoritegeners?idGener=" + selectedId;
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + token);

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder responseBuilder = new StringBuilder();

                    if (inputStream != null) {
                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                    }

                    response = responseBuilder.toString();
                }
            } catch (IOException e) {
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

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                Toast.makeText(SelectionGenersActivity.this, response, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SelectionGenersActivity.this, "Failed to add genre to favorites", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
