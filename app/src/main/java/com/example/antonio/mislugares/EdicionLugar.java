package com.example.antonio.mislugares;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Antonio on 30/04/2015.
 */
public class EdicionLugar extends Activity {
    private long id;
    private Lugar lugar;
    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);

        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        lugar = Lugares.elemento((int) id);
        nombre = (EditText) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        //ImageView logo_tipo = (ImageView) findViewById(R.id.logo_tipo);
        //logo_tipo.setImageResource(lugar.getTipo().getRecurso());
        //TextView tipo = (TextView) findViewById(R.id.tipo);
        //tipo.setText(lugar.getTipo().getTexto());
         direccion = (EditText) findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());

            telefono = (EditText) findViewById(R.id.telefono);
            telefono.setText(String.valueOf(lugar.getTelefono()));

        url = (EditText) findViewById(R.id.url);
        url.setText(lugar.getUrl());
        comentario = (EditText) findViewById(R.id.comentario);
        comentario.setText(lugar.getComentario());

        tipo = (Spinner) findViewById(R.id.tipo);
        ArrayAdapter adaptador = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        tipo.setSelection(lugar.getTipo().ordinal());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.accion_guardar){
            lugar.setNombre(nombre.getText().toString());
            lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
            lugar.setDireccion(direccion.getText().toString());
            lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
            lugar.setUrl(url.getText().toString());
            lugar.setComentario(comentario.getText().toString());
            Lugares.actualizaLugar((int) this.id, lugar);
            finish();
            return true;
        }
        if(id==R.id.accion_cancelar){
            if(getIntent().getExtras().getBoolean("nuevo", false)) {
                Lugares.borrar((int)this.id);
            }
            finish();
            return true;
        }
        return true;
    }
}
