package com.example.george.secretssms.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.george.secretssms.Adapters.SMS_Adapter;
import com.example.george.secretssms.R;
import com.example.george.secretssms.modelo.Mensaje;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Activity_Chat extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;
    private RecyclerView recyclerView;
    private FloatingActionButton btn_enviar;
    private TextInputEditText sms;
    private Bundle datos;
    private ArrayList<Mensaje> lista_mensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        textToSpeech = new TextToSpeech(this, this);
        annadir_titulo_actividad();

        datos = getIntent().getExtras();
        this.lista_mensajes = datos.getParcelableArrayList("lista_mensajes");

        btn_enviar = (FloatingActionButton) findViewById(R.id.fab_enviar);
        sms = (TextInputEditText) findViewById(R.id.txt_sms);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_chat_sms);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_Chat.this));
        final SMS_Adapter adaptor = new SMS_Adapter(getApplicationContext(), getLista_mensajes());
        recyclerView.setAdapter(adaptor);
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());

        adaptor.setClickListener(new SMS_Adapter.RecyclerTouchListener() {
            @Override
            public void onClickItem(final View view, final int position) {
                final Mensaje mensaje = getLista_mensajes().get(position);

                PopupMenu popupMenu = new PopupMenu(Activity_Chat.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.item_cifrar) {
                            if (mensaje.isEncrpitado()) {
                                Toast.makeText(getApplicationContext(), "Este sms ya está encriptado...", Toast.LENGTH_SHORT).show();
                                leer_sms_con_tts("Este mensaje ya está encriptado");
                            } else {
                                mensaje.cifrar();
                                resto_funcion_de_pop_menu(mensaje, view, adaptor, position);
                            }
                        } else if (item.getItemId() == R.id.item_descifrar) {
                            if (!mensaje.isEncrpitado()) {
                                Toast.makeText(getApplicationContext(), "Este sms no se puede descencriptar...", Toast.LENGTH_SHORT).show();
                                leer_sms_con_tts("Este mensaje no se puede descencriptar");
                            } else {
                                mensaje.descifrar();
                                resto_funcion_de_pop_menu(mensaje, view, adaptor, position);
                            }
                        } else {
                            if (mensaje.isEncrpitado()) {
                                Toast.makeText(getApplicationContext(), "Este mensaje no se puede leer. Desencríptalo", Toast.LENGTH_SHORT).show();
                                leer_sms_con_tts("Este mensaje no se puede leer. Desencríptalo");
                            } else {
                                leer_sms_con_tts(mensaje_traducido(getLista_mensajes().get(position).getMensaje()));
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sms.getText().toString();
                if (msg.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Escribe en el SMS (Está vacío)...", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Calendar c = Calendar.getInstance();
                        String act_a = Integer.toString(c.get(Calendar.YEAR)), act_m = Integer.toString(c.get(Calendar.MONTH)), act_d = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
                        String act_h = Integer.toString(c.get(Calendar.HOUR)), act_mi = Integer.toString(c.get(Calendar.MINUTE));
                        String fecha = act_d + "/" + act_m + "/" + act_a + " " + act_h + ":" + act_mi;
                        Mensaje mensaje = new Mensaje(nombre_contacto(), msg, numero_contacto(), false, true, fecha);
                        mensaje.cifrar();
                        SmsManager sms_manager = SmsManager.getDefault();
                        if (mensaje.getMensaje().length() > 150) {
                            ArrayList x = sms_manager.divideMessage(mensaje.getMensaje());
                            sms_manager.sendMultipartTextMessage(numero_contacto(), null, x, null, null);
                        } else {
                            sms_manager.sendTextMessage(numero_contacto(), null, mensaje.getMensaje(), null, null);
                        }
                        Toast.makeText(getApplicationContext(), "SMS enviado correctamente...", Toast.LENGTH_SHORT).show();
                        adaptor.getModelo().add(mensaje);
                        sms.setText("");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error de envío de mensaje...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String mensaje_traducido(String mensaje) {
        String[] x = mensaje.split(" ");
        String res = "";
        for (String i : x) {
            if (i.equals("q")) {
                res += "que ";
            } else {
                res += i + " ";
            }
        }
        return res;
    }

    private void leer_sms_con_tts(String mensaje) {
        textToSpeech.setLanguage(new Locale("spa", "MEX"));
        textToSpeech.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void resto_funcion_de_pop_menu(Mensaje mensaje, View view, SMS_Adapter adaptor, int position) {
        mostrar_animacion_de_mensaje(mensaje, view);
        adaptor.setModelo(getLista_mensajes());
        recyclerView.setAdapter(adaptor);
        recyclerView.smoothScrollToPosition(position + 1);
    }


    public void mostrar_animacion_de_mensaje(Mensaje mensaje, View view) {
        if (mensaje.isEnviado()) {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_right);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        } else {
            Animation animacion = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animacion);
        }
    }


    private void annadir_titulo_actividad() {
        if (numero_contacto().equals("Cubacel")) {
            setTitle("Cubacel...");
        } else if (nombre_contacto().equals("# no esta en los contactos")) {
            setTitle("#: " + numero_contacto());
        } else {
            setTitle(nombre_contacto() + ": " + numero_contacto());
        }
    }


    public String nombre_contacto() {
        return getIntent().getStringExtra("nombre_contacto");
    }

    public String numero_contacto() {
        return getIntent().getStringExtra("numero_contacto");
    }

    public ArrayList<Mensaje> getLista_mensajes() {
        return this.lista_mensajes;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_llamar) {
            try {
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                intentCall.setData(Uri.parse("tel: " + numero_contacto()));
                if (ActivityCompat.checkSelfPermission(Activity_Chat.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_Chat.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    Toast.makeText(getApplicationContext(), "Acepta el permiso de las llamadas", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intentCall);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error al realizar la llamada a " + nombre_contacto(), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(new Locale("spa", "MEX"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This languaje is not supported");
            }
        } else {
            Log.e("TTS", "Inicializacion fallida de texto");
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
