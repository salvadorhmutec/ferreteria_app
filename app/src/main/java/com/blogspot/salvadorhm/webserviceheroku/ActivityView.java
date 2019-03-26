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

public class ActivityView extends AppCompatActivity {

    EditText et_id_cliente;
    EditText et_nombre;
    EditText et_apellido_paterno;
    EditText et_apellido_materno;
    EditText et_telefono;
    EditText et_calle;
    EditText et_colonia;
    EditText et_municipio;
    EditText et_longitud;
    EditText et_latidud;
    ImageView iv_imagen;



    private String webservice_url = "http://ferreteriaacme.herokuapp" +
            ".com/api_clientes?user_hash=1234&action=get&id_cliente=";

    private String images_url = "http://ferreteriaacme.herokuapp" +
            ".com/static/files/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.activity_view);
        //inicialización de EditText de la vista
        et_id_cliente = findViewById(R.id.et_id_cliente);
        et_nombre = findViewById(R.id.et_nombre);
        et_apellido_paterno = findViewById(R.id.et_apellido_paterno);
        et_apellido_materno = findViewById(R.id.et_apellido_materno);
        et_telefono = findViewById(R.id.et_telefono);
        et_calle = findViewById(R.id.et_calle);
        et_colonia = findViewById(R.id.et_colonia);
        et_municipio = findViewById(R.id.et_municipio);
        et_longitud = findViewById(R.id.et_longitud);
        et_latidud = findViewById(R.id.et_latitud);
        iv_imagen = findViewById(R.id.iv_imagen);
        //Objeto tipo Intent para recuperar el parametro enviado
        Intent intent = getIntent();
        //Se almacena el id_cliente enviado
        String id_cliente = intent.getStringExtra(MainActivity.ID_CLIENTE);
        //Se cocnatena la url con el id_cliente para obtener los datos el cliente
        webservice_url+=id_cliente;
        webServiceRest(webservice_url);
    }

    private void webServiceRest(String requestURL){
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
        String id_cliente;
        String nombre;
        String apellido_paterno;
        String apellido_materno;
        String telefono;
        String calle;
        String colonia;
        String municipio;
        String longitud;
        String latitud;
        String imagen;
        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException e){
            Log.e("Error 101",e.getMessage());
        }
        for(int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Se obtiene cada uno de los datos cliente del webservice
                id_cliente = jsonObject.getString("id_cliente");
                nombre = jsonObject.getString("nombre");
                apellido_paterno = jsonObject.getString("apellido_paterno");
                apellido_materno = jsonObject.getString("apellido_materno");
                telefono = jsonObject.getString("telefono");
                calle = jsonObject.getString("calle");
                colonia = jsonObject.getString("colonia");
                municipio = jsonObject.getString("municipio");
                longitud = jsonObject.getString("longitud");
                latitud = jsonObject.getString("latitud");
                imagen = jsonObject.getString("imagen");

                //Se muestran los datos del cliente en su respectivo EditText
                et_id_cliente.setText(id_cliente);
                et_nombre.setText(nombre);
                et_apellido_paterno.setText(apellido_paterno);
                et_apellido_materno.setText(apellido_materno);
                et_telefono.setText(telefono);
                et_calle.setText(calle);
                et_colonia.setText(colonia);
                et_municipio.setText(municipio);
                et_longitud.setText(longitud);
                et_latidud.setText(latitud);
                URL newurl = new URL(images_url+imagen);
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
                iv_imagen.setImageBitmap(mIcon_val);

            }catch (JSONException e){
                Log.e("Error 102",e.getMessage());
            } catch (MalformedURLException e) {
                Log.e("Error 103",e.getMessage());
            } catch (IOException e) {
                Log.e("Error 104",e.getMessage());
            }
        }
    }
}
