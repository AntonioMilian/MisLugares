package com.example.antonio.mislugares;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Antonio on 29/04/2015.
 */
public class VistaLugarFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    private long id;
    private Lugar lugar;
    //private ImageView imageView;
    final static int RESULTADO_EDITAR= 1;
    final static int RESULTADO_GALERIA= 2;
    final static int RESULTADO_FOTO= 3;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor,
                             Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.vista_lugar, contenedor, false);
        setHasOptionsMenu(true);
        LinearLayout pUrl = (LinearLayout) vista.findViewById(R.id.p_url);
        pUrl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pgWeb(null);
            }
        });
        ImageView pCall = (ImageView) vista.findViewById(R.id.logo_telefono);
        pCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                llamadaTelefono(null);
            }
        });
        ImageView pDirec=(ImageView) vista.findViewById(R.id.logo_direccion);
        pDirec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                verMapa(null);
            }
        });
        ImageView pQuitFot = (ImageView) vista.findViewById(R.id.quitar_foto);
        pQuitFot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                eliminarFoto(null);
            }
        });
        ImageView pGaleria = (ImageView) vista.findViewById(R.id.galeria);
        pGaleria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                galeria(null);
            }
        });
        ImageView iconoHora = (ImageView) vista.findViewById(R.id.logo_hora);
        iconoHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarHora();
            }
        });
        ImageView iconoFecha = (ImageView) vista.findViewById(R.id.logo_fecha);
        iconoFecha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cambiarFecha();
            }
        });
        return vista;
    }

    public void cambiarHora() {
        DialogoSelectorHora dialogoHora = new DialogoSelectorHora();
        dialogoHora.setOnTimeSetListener(this);
        Bundle args = new Bundle();
        args.putLong("fecha", lugar.getFecha());
        dialogoHora.setArguments(args);
        dialogoHora.show(getActivity().getSupportFragmentManager(), "selectorHora");
    }

    public void cambiarFecha() {
        DialogoSelectorFecha dialogoFecha = new DialogoSelectorFecha();
        dialogoFecha.setOnDateSetListener(this);
        Bundle args = new Bundle();
        args.putLong("fecha", lugar.getFecha());
        dialogoFecha.setArguments(args);
        dialogoFecha.show(getActivity().getSupportFragmentManager(), "selectorFecha");
    }

    @Override
    public void onDateSet(DatePicker view, int anyo, int mes, int dia) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(lugar.getFecha());
        calendario.set(Calendar.YEAR, anyo);
        calendario.set(Calendar.MONTH, mes);
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        lugar.setFecha(calendario.getTimeInMillis());
        Lugares.actualizaLugar((int) id, lugar);
        TextView tFecha = (TextView) getView().findViewById(R.id.fecha);


        DateFormat formato =  DateFormat.getDateInstance();

        tFecha.setText(formato.format(new Date(lugar.getFecha())));

    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null) {
            id = extras.getLong("id", -1);
            if(id != -1) {
                actualizarVistas(id);
            }
        }
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_lugar);
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        lugar = Lugares.elemento((int) id);
        TextView nombre = (TextView) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        ImageView logo_tipo = (ImageView) findViewById(R.id.logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());
        TextView tipo = (TextView) findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());
        TextView direccion = (TextView) findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());
        if (lugar.getTelefono() == 0) {
            findViewById(R.id.telefono).setVisibility(View.GONE);
            findViewById(R.id.logo_telefono).setVisibility(View.GONE);
        } else {
            TextView telefono = (TextView) findViewById(R.id.telefono);
            telefono.setText(String.valueOf(lugar.getTelefono()));
        }
        TextView url = (TextView) findViewById(R.id.url);
        url.setText(lugar.getUrl());
        TextView comentario = (TextView) findViewById(R.id.comentario);
        comentario.setText(lugar.getComentario());
        TextView fecha = (TextView) findViewById(R.id.fecha);
        fecha.setText(DateFormat.getDateInstance().format(
                new Date(lugar.getFecha())));
        TextView hora = (TextView) findViewById(R.id.hora);
        imageView = (ImageView) findViewById(R.id.foto);
        hora.setText(DateFormat.getTimeInstance().format(
                new Date(lugar.getFecha())));
        RatingBar valoracion = (RatingBar) findViewById(R.id.valoracion);
        valoracion.setRating(lugar.getValoracion());
        valoracion.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float valor, boolean fromUser) {
                        lugar.setValoracion(valor);
                    }
                });
        actualizarVistas();

    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.vista_lugar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_compartir:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        lugar.getNombre() + " - "+ lugar.getUrl());
                startActivity(intent);
                return true;
            case R.id.accion_llegar:
                verMapa(null);
                return true;
            case R.id.accion_editar:
                Intent i = new Intent(getActivity(), EdicionLugar.class);
                i.putExtra("id", id);
                startActivityForResult(i, RESULTADO_EDITAR);
                return true;
            case R.id.accion_borrar:
                new AlertDialog.Builder(getActivity())
                        .setTitle("BORRADO DEL LUGAR")
                        .setMessage("¿Estás seguro de que quieres eliminar este lugar?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Lugares.borrar((int) id);
                                SelectorFragment selectorFragment = (SelectorFragment) getActivity().
                                        getSupportFragmentManager().findFragmentById(R.id.selector_fragment);
                                if (selectorFragment == null) {
                                    getActivity().finish();
                                } else {
                                    ((activity_main) getActivity()).muestraLugar(Lugares.primerId());
                                    ((activity_main) getActivity()).actualizaLista();
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualizarVistas(final long id) {
        this.id = id;
        lugar= Lugares.elemento((int) id);
        if(lugar != null) {
            View v = getView();

            TextView nombre = (TextView) v.findViewById(R.id.nombre);
            nombre.setText(lugar.getNombre());
            ImageView logo_tipo = (ImageView) v.findViewById(R.id.logo_tipo);
            logo_tipo.setImageResource(lugar.getTipo().getRecurso());
            TextView tipo = (TextView) v.findViewById(R.id.tipo);
            tipo.setText(lugar.getTipo().getTexto());
            TextView direccion = (TextView) v.findViewById(R.id.direccion);
            direccion.setText(lugar.getDireccion());
            if (lugar.getTelefono() == 0) {
                v.findViewById(R.id.telefono).setVisibility(View.GONE);
                v.findViewById(R.id.logo_telefono).setVisibility(View.GONE);
            } else {
                TextView telefono = (TextView) v.findViewById(R.id.telefono);
                telefono.setText(String.valueOf(lugar.getTelefono()));
            }
            TextView url = (TextView) v.findViewById(R.id.url);
            url.setText(lugar.getUrl());
            TextView comentario = (TextView) v.findViewById(R.id.comentario);
            comentario.setText(lugar.getComentario());
            TextView fecha = (TextView) v.findViewById(R.id.fecha);
            fecha.setText(DateFormat.getDateInstance().format(
                    new Date(lugar.getFecha())));
            TextView hora = (TextView) v.findViewById(R.id.hora);
            hora.setText(DateFormat.getTimeInstance().format(
                    new Date(lugar.getFecha())));
            RatingBar valoracion = (RatingBar) v.findViewById(R.id.valoracion);
            valoracion.setOnRatingBarChangeListener(null);
            valoracion.setRating(lugar.getValoracion());
            valoracion.setOnRatingBarChangeListener(
                    new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float valor, boolean fromUser) {
                            lugar.setValoracion(valor);
                            Lugares.actualizaLugar((int) id, lugar);
                        }
                    });
            ponerFoto((ImageView)getView().findViewById(R.id.foto), lugar.getFoto());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTADO_EDITAR) {
            actualizarVistas(id);
            getActivity().findViewById(R.id.scrollView1).invalidate();
        }else if (requestCode == RESULTADO_GALERIA
                && resultCode == Activity.RESULT_OK) {
            lugar.setFoto(data.getDataString());
            Lugares.actualizaLugar((int) id, lugar);
            ponerFoto((ImageView)getView().findViewById(R.id.foto), lugar.getFoto());
        }/*else if(requestCode == RESULTADO_FOTO
                && resultCode == Activity.RESULT_OK&& lugar!=null&& uriFoto!=null) {
            lugar.setFoto(uriFoto.toString());
            Lugares.actualizaLugar((int) id, lugar);
            ponerFoto(imageView, lugar.getFoto());
        }*/
    }

    protected void ponerFoto(ImageView imageView, String uri) {
        if (uri != null) {
            imageView.setImageURI(Uri.parse(uri));
        } else{
            imageView.setImageBitmap(null);
        }
    }

    public void verMapa(View view) {
        Uri uri;
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        if (lat != 0 || lon != 0) {
            uri = Uri.parse("geo:" + lat + "," + lon);
        } else {
            uri = Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void llamadaTelefono(View view) {
        startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + lugar.getTelefono())));
    }


    public void pgWeb(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(lugar.getUrl())));
    }

    public void galeria(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, RESULTADO_GALERIA);
    }

    public void eliminarFoto(View view) {
        lugar.setFoto(null);
        ponerFoto((ImageView)getView().findViewById(R.id.foto), null);
        Lugares.actualizaLugar((int) id, lugar);
    }

    @Override
    public void onTimeSet(TimePicker vista, int hora, int minuto) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(lugar.getFecha());
        calendario.set(Calendar.HOUR_OF_DAY, hora);
        calendario.set(Calendar.MINUTE, minuto);
        lugar.setFecha(calendario.getTimeInMillis());
        Lugares.actualizaLugar((int) id, lugar);
        TextView tHora = (TextView) getView().findViewById(R.id.hora);
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm",
                java.util.Locale.getDefault());
        tHora.setText(formato.format(new Date(lugar.getFecha())));
    }
}
