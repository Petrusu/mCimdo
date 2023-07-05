package com.example.mcimdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditActivity extends AppCompatActivity {
    Button editlog, editemail, editpass;
    EditText log, email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        String token = intent.getStringExtra("token");

        log = findViewById(R.id.logEdit);
        email =findViewById(R.id.emailEdit);
        pass = findViewById(R.id.passEdit);

        editlog = findViewById(R.id.edit_login);
        editemail = findViewById(R.id.edit_email);
        editpass = findViewById(R.id.edit_password);

        editlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLogin = log.getText().toString();
                new ChangeLoginTask().execute(newLogin, token);
            }
        });

        editemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = email.getText().toString();
                new ChangeEmailTask().execute(newEmail, token);
            }
        });

        editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = pass.getText().toString();
                new ChangePasswordTask().execute(newPassword, token);
            }
        });

    }
    private class ChangeLoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String newLogin = params[0];
            String token = params[1];
            String endpoint = "https://petrusu.bsite.net/api/ForAllUsers/changelogin?login=" + newLogin;

            try {
                URL url = new URL(endpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setDoOutput(true);

                // Отправка данных на сервер
                try (OutputStream outputStream = connection.getOutputStream()) {
                    // Нет данных для отправки в теле запроса PUT
                }

                // Получение ответа от сервера
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Логин успешно изменен
                    return "Login changed";
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    // Пользователь не найден
                    return "User not found";
                } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    // Логин уже существует
                    return "Login already exists";
                } else {
                    // Неизвестная ошибка
                    return "Unknown error";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Обработка результата запроса
            Log.d("ChangeLoginTask", "Result: " + result);
            // Вывод сообщения о смене логина (например, с помощью Toast)
            Toast.makeText(EditActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
    private class ChangeEmailTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String newEmail = params[0];
            String token = params[1];
            String endpoint = "https://petrusu.bsite.net/api/ForAllUsers/changeemail?email=" + newEmail;

            try {
                URL url = new URL(endpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setDoOutput(true);

                // Отправка данных на сервер
                try (OutputStream outputStream = connection.getOutputStream()) {
                    // Нет данных для отправки в теле запроса PUT
                }

                // Получение ответа от сервера
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Email успешно изменен
                    return "Email changed";
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    // Пользователь не найден
                    return "User not found";
                } else {
                    // Неизвестная ошибка
                    return "Unknown error";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Обработка результата запроса
            Log.d("ChangeEmailTask", "Result: " + result);
            // Вывод сообщения о смене email (например, с помощью Toast)
            Toast.makeText(EditActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
    private class ChangePasswordTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String newPassword = params[0];
            String token = params[1];
            String endpoint = "https://petrusu.bsite.net/api/ForAllUsers/changepassword?password=" + newPassword;

            try {
                URL url = new URL(endpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setDoOutput(true);

                // Отправка данных на сервер
                try (OutputStream outputStream = connection.getOutputStream()) {
                    // Нет данных для отправки в теле запроса PUT
                }

                // Получение ответа от сервера
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Пароль успешно изменен
                    return "Password changed";
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    // Пользователь не найден
                    return "User not found";
                } else {
                    // Неизвестная ошибка
                    return "Unknown error";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Обработка результата запроса
            Log.d("ChangePasswordTask", "Result: " + result);
            // Вывод сообщения о смене пароля (например, с помощью Toast)
            Toast.makeText(EditActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

}