package com.example.cliente;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    List<Usuario> listaUsuarios;
    Retrofit cliente;
    ApiService apiService, mAPIService;
    TextView tvResult;
    EditText txtNombre;
    String cadena="";
    private ListView listview;
    private ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtNombre=findViewById(R.id.txtNombre);
        listview = (ListView) findViewById(R.id.list);
        cliente= new Retrofit.Builder().baseUrl(ApiService.URL).addConverterFactory(GsonConverterFactory.create()).build();
        apiService=cliente.create(ApiService.class);
        apiService.listaUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                Log.i("Cliente","Cliente Android");
                if (response.isSuccessful()){
                    listaUsuarios=response.body();
                    for (Usuario usuario:listaUsuarios){
                        Log.i("Usuario",usuario.toString());
                        JsonParser parser = new JsonParser();
                        JsonArray gsonArr = parser.parse(usuario.toString()).getAsJsonArray();
                        for (JsonElement obj : gsonArr) {
                            // Object of array
                            JsonObject gsonObj = obj.getAsJsonObject();
                            // Primitives elements of object
                            names.add("id: "+ gsonObj.get("id").getAsString()+" nombre: "+gsonObj.get("nombre").getAsString()+" edad: "+gsonObj.get("edad").getAsString()+" genero: "+gsonObj.get("genero").getAsString());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.i("Error",t.getMessage());
            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);
    }
    public void ObtenerUsuario(View view) {
        String nombre=txtNombre.getText().toString();
        cliente= new Retrofit.Builder().baseUrl(ApiService.URL).addConverterFactory(GsonConverterFactory.create()).build();
        apiService=cliente.create(ApiService.class);
        apiService.obtenerUsuario(nombre).enqueue(new Callback<List<Usuario>>() {
                                                              @Override
                                                              public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                                                                  Log.i("Correcto","Datos del servicio PHP \n");
                                                                  if (response.isSuccessful()){
names.removeAll(names);
                                                                      listaUsuarios=response.body();
                                                                      for (Usuario usuario:listaUsuarios){
                                                                          Log.i("Usuario",usuario.toString());
                                                                          JsonParser parser = new JsonParser();
                                                                          JsonArray gsonArr = parser.parse(usuario.toString()).getAsJsonArray();
                                                                          for (JsonElement obj : gsonArr) {

                                                                              // Object of array
                                                                              JsonObject gsonObj = obj.getAsJsonObject();

                                                                              // Primitives elements of object
                                                                              names.add("id: "+ gsonObj.get("id").getAsString()+" nombre: "+gsonObj.get("nombre").getAsString()+" edad: "+gsonObj.get("edad").getAsString()+" genero: "+gsonObj.get("genero").getAsString());
                                                                          }
                                                                      }
                                                                  }
                                                              }
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.i("Error",t.getMessage());

            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);
    }

}