package com.blogspot.salvadorhm.webserviceheroku;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.widget.EditText;

public class ActivityInsert extends AppCompatActivity {

    EditText et_nombre_ai;
    EditText et_apellido_paterno_ai;
    EditText et_apellido_materno_ai;
    EditText et_telefono_ai;
    EditText et_calle_ai;
    EditText et_colonia_ai;
    EditText et_estado_ai;
    EditText et_municipio_ai;
    EditText et_longitud_ai;
    EditText et_latitud_ai;
    EditText et_imagen_ai;

    private String webservice_url = "https://ferreteriaacme.herokuapp" +
            ".com/api_clientes?user_hash=1234&action=put&";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        //inicialización de EditText de la vista
        et_nombre_ai = findViewById(R.id.et_nombre_ai);
        et_apellido_paterno_ai = findViewById(R.id.et_apellido_paterno_ai);
        et_apellido_materno_ai = findViewById(R.id.et_apellido_materno_ai);
        et_telefono_ai = findViewById(R.id.et_telefono_ai);
        et_calle_ai = findViewById(R.id.et_calle_ai);
        et_colonia_ai = findViewById(R.id.et_colonia_ai);
        et_estado_ai = findViewById(R.id.et_estado_ai);
        et_municipio_ai = findViewById(R.id.et_municipio_ai);
        et_longitud_ai = findViewById(R.id.et_longitud_ai);
        et_latitud_ai = findViewById(R.id.et_latitud_ai);
        et_imagen_ai = findViewById(R.id.et_imagen_ai);
    }

    public void btn_insertOnClick(View view){
        StringBuilder sb = new StringBuilder();
        sb.append(webservice_url);
        sb.append("nombre="+et_nombre_ai.getText());
        sb.append("&");
        sb.append("apellido_paterno="+et_apellido_paterno_ai.getText());
        sb.append("&");
        sb.append("apellido_materno="+et_apellido_materno_ai.getText());
        sb.append("&");
        sb.append("telefono="+et_telefono_ai.getText());
        sb.append("&");
        sb.append("calle="+et_calle_ai.getText());
        sb.append("&");
        sb.append("colonia="+et_colonia_ai.getText());
        sb.append("&");
        sb.append("estado="+et_estado_ai.getText());
        sb.append("&");
        sb.append("municipio="+et_municipio_ai.getText());
        sb.append("&");
        sb.append("longitud="+et_longitud_ai.getText());
        sb.append("&");
        sb.append("latitud="+et_latitud_ai.getText());
        sb.append("&");
        sb.append("imagen="+et_imagen_ai.getText());
        webServicePut(sb.toString());
        Log.e("URL",sb.toString());
    }
    private void webServicePut(String requestURL){
        try{
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String webServiceResult="";
            while ((line = bufferedReader.readLine()) != null){
                webServiceResult += line;
            }
            bufferedReader.close();
            parseInformation(webServiceResult);
        }catch(Exception e){
            Log.e("Error 100",e.getMessage());
        }
    }

    private void parseInformation(String jsonResult){
        JSONArray jsonArray = null;
        String status;
        String description;
        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException e){
            Log.e("Error 101",e.getMessage());
        }
        for(int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Se obtiene cada uno de los datos cliente del webservice
                status = jsonObject.getString("status");
                description = jsonObject.getString("description");
                Log.e("STATUS",status);
                Log.e("DESCRIPTION",description);
            }catch (JSONException e){
                Log.e("Error 102",e.getMessage());
            }
        }
    }
}
