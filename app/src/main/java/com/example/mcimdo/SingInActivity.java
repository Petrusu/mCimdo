package com.example.mcimdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SingInActivity extends AppCompatActivity {

    Button singin;
    EditText log, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in2);

        log = findViewById(R.id.loginEdit);
        pass = findViewById(R.id.passwordEdit);

        singin = findViewById(R.id.btn_singin);
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Создание URL для запроса
                            String login = log.getText().toString();
                            String password = pass.getText().toString();
                            String urlString = "https://petrusu.bsite.net/api/ForAllUsers/login?login=" + login + "&password=" + password;
                            URL url = new URL(urlString);

                            // Создание соединения
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            // Установка метода запроса
                            connection.setRequestMethod("POST");

                            // Получение ответа от сервера
                            int responseCode = connection.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                // Чтение ответа от сервера
                                InputStream inputStream = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                                reader.close();

                                // Обработка ответа
                                String jsonResponse = response.toString();
                                JSONObject responseObject = new JSONObject(jsonResponse);

                                String token = responseObject.getJSONObject("loginResponse").getString("token");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Открытие новой активити с передачей токена
                                        Intent intent = new Intent(SingInActivity.this, MainWindowAplication.class);
                                        intent.putExtra("token", token);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                // Обработка ошибки
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SingInActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            // Закрытие соединения
                            connection.disconnect();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}