package com.blogspot.salvadorhm.webserviceheroku;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private String url = "https://ferreteriaacme.herokuapp" +
            ".com/api_clientes?user_hash=1234&action=get";
    public static final String ID_CLIENTE = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        lv_clientes_list = findViewById(R.id.lv_clientes_list);
        adapter = new ArrayAdapter(this, R.layout.cliente_item);
        lv_clientes_list.setAdapter(adapter);
        webServiceRest(url);

        lv_clientes_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("ITEM", lv_clientes_list.getItemAtPosition(position).toString());
            String datos_cliente[] =
                    lv_clientes_list.getItemAtPosition(position).toString().split(":");
            String id_cliente = datos_cliente[0];
            Log.e("ID_CLIENTE",id_cliente);
                Intent i = new Intent(MainActivity.this, ActivityView.class);
                i.putExtra(ID_CLIENTE,id_cliente);
                startActivity(i);
            }
        });
    }

    public void activityInsertOnClick(View view){
        Intent i = new Intent(MainActivity.this,ActivityInsert.class);
        startActivity(i);
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
                adapter.add(id_cliente + ":" + nombre + " " + apellido_paterno + " " + apellido_materno);
            }catch (JSONException e){
                Log.e("Error 102",e.getMessage());
            }
        }
    }
}
