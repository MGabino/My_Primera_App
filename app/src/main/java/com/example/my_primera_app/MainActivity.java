package com.example.my_primera_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        EditText inputTextNombrePersona = (EditText) findViewById(R.id.editTextNombrePersona);
        String nombrePersona = inputTextNombrePersona.getText().toString();

        llamarAPIGenero("https://api.genderize.io?name=" + nombrePersona, Request.Method.GET);
        llamarAPIEdad("https://api.agify.io/?name=" + nombrePersona, Request.Method.GET);
    }

    private void llamarAPIGenero(String url, int httpVerb) {
        TextView campoTextoGenero = (TextView) findViewById(R.id.textGenero);
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(httpVerb, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject reader = new JSONObject(response);
                            String genero = reader.getString("gender");

                            if (genero.equals("male")) {
                                genero = "hombre";
                            } else {
                                genero = "mujer";
                            }

                            campoTextoGenero.setText("Tu genero es: " + genero);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                campoTextoGenero.setText("ocurrio un error");
            }
        });
    }

    private void llamarAPIEdad(String url, int httpVerb) {
        //obtengo la referencia al objeto que es el texto donde se mostrará el género
        TextView campoTextoEdad = (TextView) findViewById(R.id.textEdad);

        //creo una cola de requests
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(httpVerb, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //aca deberíamos asignar el string al elemento de la UI, por ejemplo
                        try {
                            //El json de la respuesta tiene muchos campos, solo extraigo "gender"
                            JSONObject reader = new JSONObject(response);
                            String edad = reader.getString("age");


                            //Asigno el valor de género al campo de visualización de texto
                            campoTextoEdad.setText("Tu Edad es: " + edad);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                campoTextoEdad.setText("ocurrió un error");
            }
        });

        //Hacemos la llamada a la API
        queue.add(stringRequest);
    }
}

