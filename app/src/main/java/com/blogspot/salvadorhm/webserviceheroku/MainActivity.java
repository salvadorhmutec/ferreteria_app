package com.blogspot.salvadorhm.webserviceheroku;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private ListView lv_clientes_list;
    private ArrayAdapter adapter;
    private String getAllContactsURL = "http://ferreteriaacme.herokuapp.com/api_clientes?user_hash=1234&action=get";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        lv_clientes_list = (ListView)findViewById(R.id.lv_clientes_list);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        lv_clientes_list.setAdapter(adapter);
        webServiceRest(getAllContactsURL);
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
                adapter.add(id_cliente + ":" + nombre + " " + apellido_paterno);
            }catch (JSONException e){
                Log.e("Error 102",e.getMessage());
            }
        }
    }
}
