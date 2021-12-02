package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        ((Button) findViewById(R.id.buttonNotHaveAccount)).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

        ((Button) findViewById(R.id.buttonLogin)).setOnClickListener(v -> {
            if (Pattern.matches(
                    "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$",
                    ((TextView) findViewById(R.id.emailLogin)).getText().toString())
            ) {
                new MaterialAlertDialogBuilder(((View) findViewById(R.id.componentMaterial)).getContext(),
                        R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setMessage("Email введён неверно")
                        .setPositiveButton("Ok", (dialog, which) -> {
                        })
                        .show();
                return;
            }
            for (TextView textView : new TextView[]{findViewById(R.id.emailLogin), findViewById(R.id.passwordLogin)}) {
                if (textView.getText().equals("")) {
                    new MaterialAlertDialogBuilder(((View) findViewById(R.id.componentMaterial)).getContext(),
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                            .setMessage((textView.getId() == R.id.emailLogin ? "Email" : "Password") + " введён неверно")
                            .setPositiveButton("Ok", (dialog, which) -> {
                            })
                            .show();
                    return;
                }
            }


            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("email", ((TextView) findViewById(R.id.emailLogin)).getText().toString());
                jsonBody.put("password", ((TextView) findViewById(R.id.passwordLogin)).getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Volley.newRequestQueue(getApplicationContext()).add(
                    new JsonObjectRequest(
                            Request.Method.POST, "http://cinema.areas.su/auth/login", jsonBody,
                            response -> {
                                try {
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.putExtra("token", response.get("token").toString());
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                            Throwable::printStackTrace)
            );
        });
    }
}