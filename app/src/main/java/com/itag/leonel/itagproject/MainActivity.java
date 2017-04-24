package com.itag.leonel.itagproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText nombre, apellido;
    Button guardar;
    HttpClient cliente;
    HttpPost post;
    List<NameValuePair> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    nombre = (EditText) findViewById(R.id.txt_nombre);
    apellido = (EditText) findViewById(R.id.txt_apellido);
    guardar = (Button) findViewById(R.id.btn_guardar);

        guardar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nombre.getText().toString().equals(""))
            {
                Toast.makeText(MainActivity.this, "Nombre no puede ser vacio", Toast.LENGTH_SHORT).show();
            }
            else
                if(apellido.getText().toString().equals(""))
            {
                Toast.makeText(MainActivity.this, "Apellido no puede ser vacio", Toast.LENGTH_SHORT).show();
            }
            else
                new EnviarDatos(MainActivity.this).execute();

        }
    });
}
class EnviarDatos extends AsyncTask<String, String, String>
{
    private Activity contexto;

    EnviarDatos(Activity context)
    {
        this.contexto = context;
    }
    @Override
    protected String doInBackground(String... params) {
        if (datos())
        {
            contexto.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(contexto, "Datos enviados exitosamente", Toast.LENGTH_SHORT) .show();
                    nombre.setText("");
                    apellido.setText("");
                }
            });
        }
        else
        {
            contexto.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(contexto, "Datos no enviados", Toast.LENGTH_SHORT) .show();
                }
            });
        }


        return null;
    }
}

    private boolean datos()
    {
        cliente = new DefaultHttpClient();
        post = new HttpPost("http://192.168.0.33/dbandroid/ws/insertarUsuario.php");
        lista = new ArrayList<NameValuePair>(2);
        lista.add(new BasicNameValuePair("nombre",nombre.getText().toString().trim()));
        lista.add(new BasicNameValuePair("apellido",apellido.getText().toString().trim()));
        try
        {
            post.setEntity(new UrlEncodedFormEntity(lista));
            cliente.execute(post);
            return true;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

}
