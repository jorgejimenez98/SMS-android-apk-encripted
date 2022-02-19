package com.example.george.secretssms.Activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.george.secretssms.Adapters.Adapter_Contactos;
import com.example.george.secretssms.MainActivity;
import com.example.george.secretssms.R;
import com.example.george.secretssms.modelo.Contacto;
import com.example.george.secretssms.modelo.Funciones_Ayuda;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import java.util.ArrayList;

public class Activity_Contactos extends AppCompatActivity {
    private Funciones_Ayuda FUNC = new Funciones_Ayuda();
    private RecyclerView recyclerView;
    private SearchView searchView;
    private Toolbar toolbar;
    private ArrayList<Contacto> list;
    private Bundle datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        datos = getIntent().getExtras();

        toolbar = (Toolbar) findViewById(R.id.toolbar_contactos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Busca tocando el ícono de búsqueda...");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_contactos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_Contactos.this));
        final Adapter_Contactos adaptor = new Adapter_Contactos(getApplicationContext(), lista_contactos());
        recyclerView.setAdapter(adaptor);
        onClickList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contactos, menu);
        MenuItem searchItem = menu.findItem(R.id.example_search);
        final SearchView toolbarSearch = (SearchView) searchItem.getActionView();
        toolbarSearch.setQueryHint("Buscar.....");
        toolbarSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                return true;
            }
        });
        toolbarSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                Toast.makeText(getApplicationContext(), "No hay resultados de esa búsqueda", Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Adapter_Contactos adap = (Adapter_Contactos) recyclerView.getAdapter();
                adap.getFilter().filter(newText);
                onClickList();
                return true;
            }
        });
        return true;
    }

    private void onClickList() {
        final Adapter_Contactos adapNew = (Adapter_Contactos) recyclerView.getAdapter();
        adapNew.setClickListener(new Adapter_Contactos.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                list = adapNew.currientList();
                String[] x = list.get(position).getContacto().split(":");
                String nombre = x[0];
                String numero = x[1].replace(" ", "");
                showAlertDialog(nombre, numero);
            }
        });
    }

    public ArrayList<String> list_contactos() {
        return datos.getStringArrayList("lista_contactos");
    }

    private void showAlertDialog(final String nombre, final String numero) {
        // Libreria Bottom Dialog
        new BottomDialog.Builder(this)
                .setTitle("Ejecutar Secrets SMS")
                .setContent("Seguro que desea escribir a " + nombre + "?")
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher_app)
                .setPositiveText("Si")
                .setNegativeText("No")
                .setPositiveTextColorResource(android.R.color.white)
                .setNegativeTextColorResource(android.R.color.black)
                .setPositiveBackgroundColorResource(android.R.color.holo_green_dark)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("numero_contacto", numero);
                        startActivity(intent);
                        finish();
                    }
                })
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        bottomDialog.dismiss();
                    }
                })
                .show();
    }

    public ArrayList<Contacto> lista_contactos() {
        ArrayList<Contacto> list = new ArrayList<>();
        String primera_letra = String.valueOf(list_contactos().get(0).charAt(0)).toUpperCase();
        Contacto primer_contacto = new Contacto(primera_letra, list_contactos().get(0));
        list.add(primer_contacto);
        for (int i = 1; i < list_contactos().size(); i++) {
            String letra = String.valueOf(list_contactos().get(i).charAt(0)).toUpperCase();
            if (letra.equalsIgnoreCase(primera_letra)) {
                Contacto c1 = new Contacto(null, list_contactos().get(i));
                list.add(c1);
            } else {
                primera_letra = String.valueOf(list_contactos().get(i).charAt(0)).toUpperCase();
                Contacto c2 = new Contacto(primera_letra, list_contactos().get(i));
                list.add(c2);
            }
        }
        return list;
    }
}
