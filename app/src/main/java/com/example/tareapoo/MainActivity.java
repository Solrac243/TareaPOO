package com.example.tareapoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //region Referencias
    private TextInputLayout tilSerie, tilDescripcion, tilValor;
    private Spinner spnTipo;
    private Button btnGrabar, btnEliminar, btnRetroceder, btnAvanzar, btnBD;
    private TextView tvIndice;

    private String[] tipos;
    private ArrayList<Equipo> losEquipos;

    private ArrayAdapter<String> miAdaptador;

    private int indiceActual;
    //endregion

    //region Métodos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        referencias();

        poblar();

        eventos();

        limpiarPantalla();

        obtenerUltimoIndice();
    }

    private void guardarIndiceActual() {
        SharedPreferences sp = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sp.edit();

        editorSP.putInt("Indice", indiceActual);
        editorSP.putString("Serie", tilSerie.getEditText().getText().toString());

        editorSP.commit();
    }

    private void obtenerUltimoIndice(){
        SharedPreferences sp = getSharedPreferences("datos", Context.MODE_PRIVATE);
        indiceActual= sp.getInt("indice", -1);
        mostrarDatos();
    }

    @Override
    protected void onDestroy() {
        guardarIndiceActual();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        guardarIndiceActual();
        super.onPause();
    }

    private void poblar(){
        tipos = new String[5];
        tipos[0] = "Seleccione tipo"; tipos[1] = "PC"; tipos[2] = "Notebook";
        tipos[3] = "Mouse"; tipos[4] = "Teclados"; tipos[5] = "Accesorio";

        losEquipos = new ArrayList<>();
        losEquipos.add(new Equipo(111, "HP 200SE", 10000, "PC"));
        losEquipos.add(new Equipo(222, "Genius", 3000, "Mouse"));
        losEquipos.add(new Equipo(333, "Pendrive", 1500, "Accesorio"));

    }

    //TODO: Hay que hacer que muestre los datos
    private void mostrarDatos(){

        if(indiceActual <= losEquipos.size() && indiceActual>=0) {
            Equipo e = losEquipos.get(indiceActual);
            String serieStr = String.valueOf(e.getSerie());
            String valorStr = String.valueOf(e.getValor());
            tilDescripcion.getEditText().setText(e.getDescripcion());
            tilSerie.getEditText().setText(serieStr);
            tilValor.getEditText().setText(valorStr);
            if(e.getTipo().equals("PC")) spnTipo.setSelection(2);
            if(e.getTipo().equals("Mouse")) spnTipo.setSelection(3);
            if(e.getTipo().equals("Teclado")) spnTipo.setSelection(4);
            if(e.getTipo().equals("Accesorio")) spnTipo.setSelection(5);

            tvIndice.setText((indiceActual + 1) + " de" + losEquipos.size());
        }else {
            //TODO: hacer un elif para cuando es mayor o menor que el tamaño del array, y usar el respectivo "setenable"
            Log.d("TAG_", "Indice actual " + indiceActual);

            btnAvanzar.setEnabled(false);
            btnRetroceder.setEnabled(false);
        }
    }

    //TODO: Validar 1) que no sea el mismo equipo, 2) datos obligatorios (setError)
    private void grabarEquipo(){
        Equipo e = new Equipo();
        e.setSerie(Integer.parseInt(tilSerie.getEditText().getText().toString()));
        e.setDescripcion(tilDescripcion.getEditText().getText().toString());
        e.setTipo(spnTipo.getSelectedItem().toString());
        e.setValor(Integer.parseInt(tilValor.getEditText().getText().toString()));

        losEquipos.add(e);

        Toast.makeText(this, "Grabado exitosamente", Toast.LENGTH_SHORT).show();
    }

    private void limpiarPantalla(){
        tilSerie.getEditText().setText("");
        tilDescripcion.getEditText().setText("");
        tilValor.getEditText().setText("");
        spnTipo.setSelection(0);
        tvIndice.setText("[" + losEquipos.size() + "]");

        indiceActual = -1;
    }

    private void crearBD() {
        AdministradorBaseDatos abds = new AdministradorBaseDatos(MainActivity.this, "BDPrueba", null, 1);
        try {
            SQLiteDatabase miBD = abds.getWritableDatabase();

            if (miBD != null) {

                miBD.execSQL("insert into equipos (serie,descripcion) values(111,'Equipo de usuario')");
            }

            ContentValues registro = new ContentValues();
            registro.put("serie", 222);
            registro.put("descripcion", "Equipo de usuario 2");
            miBD.insert("equipos",null , registro);

            miBD.close();

        } catch (Exception ex) {
            Log.e("TAG_", ex.toString());

        } finally {

        }
    }

    //endregion

    //region referencias y eventos

    private void eventos() {
        btnAvanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                indiceActual = indiceActual+1;
                if(indiceActual == losEquipos.size())
                    indiceActual = 0;

                mostrarDatos();
            }
        });

        btnRetroceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                indiceActual = indiceActual-1;
                if(indiceActual == -1)
                    indiceActual = losEquipos.size() -1;

                mostrarDatos();
            }
        });

        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grabarEquipo();

            }
        });

        //TODO: Validar que exista la serie en pantalla, crear metodo externo
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                losEquipos.remove(indiceActual);
                limpiarPantalla();

            }
        });

        btnBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearBD();
            }
        });

    }

    private void referencias(){
     tilSerie = findViewById(R.id.tilSerie);
     tilDescripcion = findViewById(R.id.tilDescripcion);
     tilValor = findViewById(R.id.tilValor);
     spnTipo = findViewById(R.id.spnTipo);

     btnGrabar = findViewById(R.id.btnGrabar);
     btnEliminar = findViewById(R.id.btnEliminar);
     btnRetroceder = findViewById(R.id.btnRetroceder);
     btnAvanzar = findViewById(R.id.btnAvanzar);
     tvIndice = findViewById(R.id.tvIndice);
     btnBD = findViewById(R.id.btnBD);

     miAdaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
     spnTipo.setAdapter(miAdaptador);
    }
    //endregion
}