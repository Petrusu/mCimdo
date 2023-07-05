package com.example.mcimdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

public class BookDetailsActivity extends AppCompatActivity {
    String token;
    String bookId;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        button = findViewById(R.id.starButton);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        bookId = intent.getStringExtra("idbook");

        getBookInformation();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            // Создание URL для запроса
                            String urlString = "https://petrusu.bsite.net/api/ForAllUsers/addbookforfavorite?idBook=" + bookId;
                            URL url = new URL(urlString);

                            // Создание соединения
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            // Установка метода запроса
                            connection.setRequestMethod("POST");

                            // Установка заголовков запроса
                            connection.setRequestProperty("Authorization", "Bearer " + token);

                            // Получение ответа от сервера
                            int responseCode = connection.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                // Запрос выполнен успешно
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BookDetailsActivity.this, "Book added to favorites", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Обработка ошибок
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BookDetailsActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            // Закрытие соединения
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateUI(String title, String[] genres, String description) {
        TextView titleTextView = findViewById(R.id.title);
        titleTextView.setText(title);

        TextView genresTextView = findViewById(R.id.genres);
        genresTextView.setText(TextUtils.join(", ", genres));

        TextView descriptionTextView = findViewById(R.id.description);
        descriptionTextView.setText(description);
    }

    private void getBookInformation() {
        String url = "https://petrusu.bsite.net/api/ForAllUsers/getinformationaboutbook?bookId=" + bookId;

        new GetBookInformationTask().execute(url);
    }

    private class GetBookInformationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String response = null;

            try {
                URL urlObject = new URL(url);
                connection = (HttpURLConnection) urlObject.openConnection();
                connection.setRequestMethod("GET");
                connection.addRequestProperty("Authorization", "Bearer " + token);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    response = responseBuilder.toString();
                } else {
                    // Обработка ошибки
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String title = jsonResponse.getString("title");
                    JSONArray genresArray = jsonResponse.getJSONArray("geners");
                    String[] genres = new String[genresArray.length()];
                    for (int i = 0; i < genresArray.length(); i++) {
                        genres[i] = genresArray.getString(i);
                    }
                    String description = jsonResponse.getString("description");

                    updateUI(title, genres, description);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
