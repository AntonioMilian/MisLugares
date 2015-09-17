package com.example.antonio.mislugares;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class activity_main extends FragmentActivity implements  LocationListener{

    private static final long DOS_MINUTOS = 2 * 60 * 1000;
    private Button bAcercaDe;
    //public BaseAdapter adaptador;
    private MediaPlayer mp;
    private VistaLugarFragment fragmentVista;
    private LocationManager manejador;
    static Location mejorLocaliz;
    ListView listView;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        super.onCreate(estadoGuardado);
        setContentView(R.layout.activity_main);
        //adaptador = new AdaptadorLugares(this);
        Lugares.indicializaBD(this);
        /*adaptador = new SimpleCursorAdapter(this, R.layout.elemento_lista, Lugares.listado(),
                new String[] { "nombre", "direccion"}, new int[] { R.id.nombre, R.id.direccion}, 0);*/

        /*bAcercaDe = (Button) findViewById(R.id.Button03);
        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarAcercaDe(null);
            }
        });*/
        //Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        fragmentVista = (VistaLugarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.vista_lugar_fragment);
        if (fragmentVista != null) {
            fragmentVista.actualizarVistas(Lugares.primerId());
        }
        mp = new MediaPlayer().create(this,R.raw.audio);
        if(estadoGuardado==null) {
            mp.start();
        }else{
            int posicion = estadoGuardado.getInt("posicion");
            mp.seekTo(posicion);
        }
        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            actualizaMejorLocaliz(manejador.getLastKnownLocation(
                    LocationManager.GPS_PROVIDER));

        }
        if(manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
           actualizaMejorLocaliz(manejador.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER));

        }

    }

    public void muestraLugar(long id) {
        if (fragmentVista != null) {
            fragmentVista.actualizarVistas(id);
        } else {
            Intent intent = new Intent(this, VistaLugar.class);
            intent.putExtra("id", id);
            startActivityForResult(intent, 0);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }
        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }
        if (id == R.id.menu_buscar) {
            lanzarVistaLugar(null);
            return true;
        }

        if (id==R.id.menu_mapa) {
            Intent i = new Intent(this, Mapa.class);
            startActivityForResult(i, 0);
        }
        if (id==R.id.accion_nuevo) {
            long id2 = Lugares.nuevo();
            Intent i= new Intent(this, EdicionLugar.class);
            i.putExtra("nuevo", true);
            i.putExtra("id", id2);
            startActivityForResult(i, 0);
            return true;
        }
        return super.onOptionsItemSelected(item); /** true -> consumimos el item, no se propaga*/
    }


    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
    }

    public void mostrarPreferencias(View view) {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = "notificaciones: " + pref.getBoolean("notificaciones", true)
                + ", distancia mínima: " + pref.getString("distancia", "?")
                + " E-mail: " + pref.getString("e-mail", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void lanzarVistaLugar(View view) {
        final EditText entrada = new EditText(this);
        entrada.setText("0");
        new AlertDialog.Builder(this)
                .setTitle("Selección de lugar")
                .setMessage("indica su id:")
                .setView(entrada)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        long id = Long.parseLong(entrada.getText().toString());
                        Intent i = new Intent(activity_main.this, VistaLugar.class);
                        i.putExtra("id", id);
                        startActivity(i);
                    }
                })

                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void salir(View view) {
        finish();
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View vista, int position, long id) {
        Intent i = new Intent(this, VistaLugar.class);
        i.putExtra("id", id);
        startActivityForResult(i, 0);
    }*/

    @Override protected void onStart() {
        super.onStart();
        //Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onResume() {
        super.onResume();
        activarProveedores();
        mp.start();

        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onPause() {
        //Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        manejador.removeUpdates(this);
        super.onPause();
    }

    @Override protected void onStop() {
        super.onStop();
        //Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado){
        super.onSaveInstanceState(estadoGuardado);
        if (mp != null) {
            int pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos);
        }
    }

    /*@Override
    protected void onRestoreInstanceState(Bundle estadoGuardado){
        super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null) {
            int pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        }
    }*/
    private void activarProveedores() {
        if(manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20 * 1000, 5, this);
        }

        if(manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10 * 1000, 10, this);
        }
    }

    @Override public void onLocationChanged(Location location) {
        Log.d(Lugares.TAG, "Nueva localización: " + location);
        actualizaMejorLocaliz(location);


    }

    private void actualizaMejorLocaliz(Location localiz) {
        if (localiz == null) {
            return;
        }

        if(isBetterLocation(localiz)) {
            Log.d(Lugares.TAG, "Nueva mejor localización");
            mejorLocaliz = localiz;
            Lugares.posicionActual.setLatitud(localiz.getLatitude());
            Lugares.posicionActual.setLongitud(localiz.getLongitude());
        }
    }
    private boolean isBetterLocation(Location localiz) {
        return mejorLocaliz == null
                || localiz.getAccuracy() < 2 * mejorLocaliz.getAccuracy()
                || localiz.getTime() - mejorLocaliz.getTime() > DOS_MINUTOS;
    }


    @Override public void onProviderDisabled(String proveedor) {
        Log.d(Lugares.TAG, "Se deshabilita: "+proveedor);
        activarProveedores();
    }

    @Override    public void onProviderEnabled(String proveedor) {
        Log.d(Lugares.TAG, "Se habilita: "+proveedor);
        activarProveedores();
    }

    @Override
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
        Log.d(Lugares.TAG, "Cambia estado: "+proveedor);
        activarProveedores();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        actualizaLista();
    }

    public void actualizaLista() {
        ListView listView = (ListView) findViewById(R.id.listView);
        AdaptadorCursorLugares adaptador = (AdaptadorCursorLugares) listView.getAdapter();
        adaptador.changeCursor(Lugares.listado());
    }
}
