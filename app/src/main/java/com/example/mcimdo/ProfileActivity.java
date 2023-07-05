package com.example.mcimdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {
    Button edit, exit, select;
    private LinearLayout ButtonsContainer;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        edit = findViewById(R.id.btn_edit);
        exit = findViewById(R.id.btn_exit);
        select = findViewById(R.id.SelectGener_btn);
        ButtonsContainer = findViewById(R.id.ButtonsContainer);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SelectionGenersActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        new GetFavoriteBooksTask().execute();
    }

    private void createBookButtons(JSONArray favoriteBooks) {
        int buttonWidth = getResources().getDimensionPixelSize(R.dimen.button_width);
        int buttonHeight = getResources().getDimensionPixelSize(R.dimen.button_height);
        try {
            for (int i = 0; i < favoriteBooks.length(); i++) {
                JSONObject bookObject = favoriteBooks.getJSONObject(i);
                int idBook = bookObject.getInt("idBook");
                String title = bookObject.getString("title");
                String author = bookObject.getString("author");

                String book = title + "\n" + author;

                Button button = new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight));
                button.setText(book);
                button.setAllCaps(false);
                button.setPadding(10, 10, 10, 10);
                button.setBackgroundResource(R.drawable.button_background);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 10, 0, 10);
                button.setLayoutParams(params);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Обработка нажатия кнопки
                        String selectedBook = book;
                        int selectionId = idBook;

                        Intent intent = new Intent(ProfileActivity.this, BookDetailsActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("idbook", String.valueOf(selectionId));
                        startActivity(intent);
                    }
                });

                ButtonsContainer.addView(button);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Добавьте следующий внутренний класс AsyncTask в вашу ProfileActivity
    private class GetFavoriteBooksTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            JSONArray favoriteBooks = null;

            try {
                URL url = new URL("https://petrusu.bsite.net/api/ForAllUsers/Favorite");
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

                    favoriteBooks = new JSONArray(response.toString());
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

            return favoriteBooks;
        }

        @Override
        protected void onPostExecute(JSONArray favoriteBooks) {
            if (favoriteBooks != null) {
                createBookButtons(favoriteBooks);
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to retrieve favorite books", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
