package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        ((Button) findViewById(R.id.buttonHaveAccount)).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        ((Button) findViewById(R.id.buttonRegister)).setOnClickListener(v -> {
            if (Pattern.matches(
                    "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$",
                    ((TextView) findViewById(R.id.emailRegister)).getText().toString())
            ) {
                new MaterialAlertDialogBuilder(((View) findViewById(R.id.componentMaterial)).getContext(),
                        R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setMessage("Email введён неверно")
                        .setPositiveButton("Ok", (dialog, which) -> {
                        })
                        .show();
                return;
            }

//            if (((TextView) findViewById(R.id.repeatPassword)).getText().equals(((TextView) findViewById(R.id.passwordRegister)).getText())) {
//
//            } else {
//                new MaterialAlertDialogBuilder(((View) findViewById(R.id.componentMaterial)).getContext(),
//                        R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
//                        .setMessage("Пароли не совпадают")
//                        .setPositiveButton("Ok", (dialog, which) -> {
//                        })
//                        .show();
//                return;
//            }

            for (TextView textView : new TextView[]{
                    findViewById(R.id.emailRegister), findViewById(R.id.passwordRegister),
                    findViewById(R.id.nameRegister), findViewById(R.id.familyRegister),
                    findViewById(R.id.repeatPassword)
            }) {
                if (textView.getText().equals("")) {
                    String name = "";
                    switch (textView.getId()) {
                        case R.id.nameRegister:
                            name = "Имя";
                            break;
                        case R.id.familyRegister:
                            name = "Фамилия";
                            break;
                        case R.id.emailRegister:
                            name = "Почта";
                            break;
                        case R.id.passwordRegister:
                            name = "Пароль";
                            break;
                        case R.id.repeatPassword:
                            name = "Повторный пароль";
                            break;
                        default:
                            break;
                    }
                    new MaterialAlertDialogBuilder(((View) findViewById(R.id.componentMaterial)).getContext(),
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                            .setMessage(name + " - введён неверно")
                            .setPositiveButton("Ok", (dialog, which) -> {
                            })
                            .show();
                    return;
                }
            }

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("email", ((TextView) findViewById(R.id.emailRegister)).getText().toString());
                jsonBody.put("password", ((TextView) findViewById(R.id.passwordRegister)).getText().toString());
                jsonBody.put("firstName", ((TextView) findViewById(R.id.nameRegister)).getText().toString());
                jsonBody.put("lastName", ((TextView) findViewById(R.id.familyRegister)).getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String stringBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://cinema.areas.su/auth/register",
                    response -> {
                        new MaterialAlertDialogBuilder(((View) findViewById(R.id.componentMaterial)).getContext(),
                                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                                .setMessage(response)
                                .setPositiveButton("Ok", (dialog, which) -> {
                                    if (response.equals("Успешная регистрация")) {
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                        // Respond to negative button press
                        Log.d("onCreate: ", response);
                    }, Throwable::printStackTrace) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return stringBody.getBytes(StandardCharsets.UTF_8);
                    } catch (Exception err) {
                        err.printStackTrace();
                        return null;
                    }
                }
            };

            Volley.newRequestQueue(this).add(
                    stringRequest
            );
        });
    }
}