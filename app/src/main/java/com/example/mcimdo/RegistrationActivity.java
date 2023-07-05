package com.example.mcimdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegistrationActivity extends AppCompatActivity {

    EditText log, email, pass;
    Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        log = findViewById(R.id.logEdit);
        email = findViewById(R.id.emailEdit);
        pass = findViewById(R.id.passEdit);

        reg = findViewById(R.id.reg_btn);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Получение введенных данных пользователя
                            String login = log.getText().toString();
                            String userEmail = email.getText().toString();
                            String password = pass.getText().toString();

                            // Создание URL для запроса
                            String urlString = "https://petrusu.bsite.net/api/ForAllUsers/registration?login=" +
                                    URLEncoder.encode(login, "UTF-8") +
                                    "&email=" + URLEncoder.encode(userEmail, "UTF-8") +
                                    "&password=" + URLEncoder.encode(password, "UTF-8");
                            URL url = new URL(urlString);

                            // Создание соединения
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            // Установка метода запроса
                            connection.setRequestMethod("POST");

                            // Получение ответа от сервера
                            int responseCode = connection.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                // Успешная регистрация
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegistrationActivity.this, "User successfully registered", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            } else if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                                // Пользователь с таким именем пользователя или email'ом уже существует
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegistrationActivity.this, "A user with the same username or email address already exists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Обработка других ошибок
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegistrationActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
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
}