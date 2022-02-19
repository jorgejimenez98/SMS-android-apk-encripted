package com.example.george.secretssms;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.secretssms.Activities.Activity_Chat;
import com.example.george.secretssms.Activities.Activity_Contactos;
import com.example.george.secretssms.Activities.Activity_Informacion;
import com.example.george.secretssms.Adapters.CustomAdaptorChat;
import com.example.george.secretssms.modelo.Chat;
import com.example.george.secretssms.modelo.*;
import com.example.george.secretssms.modelo.Funciones_Ayuda;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Chat> lista_conversaciones = new ArrayList<>();
    private RecyclerView recyclerView;
    ArrayList<String> lista_contactos = new ArrayList<>();
    private static final int READ_CONTACTS = 1;
    private static final int PERMISSION_READ = 2;
    private Funciones_Ayuda FUNC = new Funciones_Ayuda();
    private Cursor c;
    private TextView txt_conversaciones;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        lista_conversaciones = new ArrayList<>();
        premiso();
        super.onResume();
    }

    private void premiso() {
        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int permissioncheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permissioncheck == PackageManager.PERMISSION_GRANTED && permissioncheck2 == PackageManager.PERMISSION_GRANTED) {
            continueOnCreate();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS}, PERMISSION_READ);
        }
    }

    private void continueOnCreate() {
        setContentView(R.layout.activity_main);
        llenar_lista_contactos();
        read_sms();
        FUNC.orden_x_seleccion(lista_conversaciones);
        final CustomAdaptorChat adaptor = new CustomAdaptorChat(getApplicationContext(), lista_conversaciones);

        Intent intent = getIntent();
        if (intent != null) {
            String numero = intent.getStringExtra("numero_contacto");
            if (numero != null) {
                int indice = existe_conversacion(numero);
                Intent i = new Intent(getApplicationContext(), Activity_Chat.class);
                if (indice != -1) {
                    String nombre = lista_conversaciones.get(indice).getNombre_contacto();
                    ArrayList<Mensaje> lista_mensajes = lista_conversaciones.get(indice).getLista_mensajes();
                    i.putExtra("nombre_contacto", nombre);
                    i.putExtra("numero_contacto", numero);
                    i.putParcelableArrayListExtra("lista_mensajes", lista_mensajes);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    Toast.makeText(getApplicationContext(), "Se ha reiniciado la conversaición con " + nombre, Toast.LENGTH_SHORT).show();
                } else {
                    Chat new_chat = new Chat(nombre_contacto_x_numero(numero), numero, new ArrayList<Mensaje>());
                    i.putExtra("nombre_contacto", new_chat.getNombre_contacto());
                    i.putExtra("numero_contacto", new_chat.getNumero_contacto());
                    i.putParcelableArrayListExtra("lista_mensajes", new_chat.getLista_mensajes());
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    lanzar_bienvenida_al_usuario();
                    adaptor.getModel().add(new_chat);
                    Toast.makeText(getApplicationContext(), "Se ha iniciado un nuevo chat con " + new_chat.getNombre_contacto(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Collections.reverse(lista_conversaciones);

        txt_conversaciones = (TextView) findViewById(R.id.txt_cant_conversaciones);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_conversaciones);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adaptor);
        adaptor.setClickListener(new CustomAdaptorChat.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                Intent i = new Intent(getApplicationContext(), Activity_Chat.class);
                String nombre = lista_conversaciones.get(position).getNombre_contacto();
                String numero = lista_conversaciones.get(position).getNumero_contacto();
                ArrayList<Mensaje> lista_mensajes = lista_conversaciones.get(position).getLista_mensajes();
                i.putExtra("nombre_contacto", nombre);
                i.putExtra("numero_contacto", numero);
                i.putParcelableArrayListExtra("lista_mensajes", lista_mensajes);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // Cuando le das click al Floating Action Buttom
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Activity_Contactos.class);
                i.putStringArrayListExtra("lista_contactos", lista_contactos);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        analizar_txt_conversaciones();
        lanzar_notificacion_al_recibir_un_sms();
    }

    private void lanzar_notificacion_al_recibir_un_sms() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String[] x = intent.getStringExtra("sms").split(":");
                String numero = x[0].replace(" ", ""), sms = x[1];

                Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.fondo_capi);
                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText(sms);
                String nombre = FUNC.nombre_contacto_x_numero(lista_contactos, numero);
                String titulo;
                if (nombre.equals("# no esta en los contactos")) {
                    titulo = "Mensaje de: " + numero;
                } else {
                    titulo = "Mensaje de: " + nombre + "  " + numero;
                }
                bigText.setBigContentTitle(titulo);
                bigText.setSummaryText("Secrets SMS");

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher_app)
                        .setContentText(sms)
                        .setLargeIcon(icon1)
                        .setContentTitle(titulo)
                        .setVibrate(new long[]{0, 500})
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setStyle(bigText);
                Intent targetintent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, targetintent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, builder.build());
            }
        };
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SEND");
        registerReceiver(broadcastReceiver, intentFilter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_READ) {
            continueOnCreate();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), Activity_Informacion.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void llenar_lista_contactos() {
        c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        while (c.moveToNext()) {
            String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String body = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (body.startsWith("+53") || (body.length() == 8 && !body.startsWith("32"))) {
                String body_sin_espacios = body.replace(" ", "");
                if (!body_sin_espacios.startsWith("+53")) {
                    body_sin_espacios = "+53" + body_sin_espacios;
                }
                String contacto = number + ": " + body_sin_espacios;
                if (!FUNC.existe_contacto(lista_contactos, contacto)) {
                    lista_contactos.add(contacto);
                }
            }
        }
        FUNC.quicksort_x_string(lista_contactos, 0, lista_contactos.size() - 1);
        c.close();
    }

    private void read_sms() {
        Cursor c = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
        ArrayList<String> lista_sms = new ArrayList<>();
        while (c.moveToNext()) {
            String mili_seg = c.getString(c.getColumnIndex("date"));
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm aa");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(mili_seg));
            String fecha = dateFormat.format(calendar.getTime());
            String num = c.getString(c.getColumnIndex("address"));
            if (!num.startsWith("+53") && num.length() != 11 && !num.equals("Cubacel")) {
                num = "+53" + num;
            }
            String msg = c.getString(c.getColumnIndex("body"));
            String type = c.getString(c.getColumnIndex("type"));//1 recivido     2 enviado
            String sms = num + "-" + msg + "-" + type + "-" + fecha;
            lista_sms.add(sms);
        }
        Collections.reverse(lista_sms);
        for (String sms : lista_sms) {
            String[] lista = sms.split("-");
            String num = lista[0], msg = lista[1], type = lista[2], fecha = lista[3];
            String nombre = FUNC.nombre_contacto_x_numero(lista_contactos, num);
            int indice = FUNC.existe_conversacion(lista_conversaciones, num);
            if (indice != -1) {
                lista_conversaciones.get(indice).getLista_mensajes().add(mensaje(nombre, msg, num, type, fecha));
            } else {
                ArrayList<Mensaje> lista_mensajes = new ArrayList<>();
                lista_mensajes.add(mensaje(nombre, msg, num, type, fecha));
                Chat chat = new Chat(nombre, num, lista_mensajes);
                lista_conversaciones.add(chat);
            }
        }
    }

    public Mensaje mensaje(String nombre, String msg, String num, String type, String fecha) {
        Mensaje mensaje;
        boolean es_encriptado = false;
        if (msg.split(" ").length != 1 && msg.split(" ").length != 0 && msg.split(" ")[msg.split(" ").length - 1].equals("=")) {
            es_encriptado = true;
        }
        if (type.equals("1")) {
            mensaje = new Mensaje(nombre, msg, num, es_encriptado, false, fecha);
        } else {
            mensaje = new Mensaje("Yo", msg, num, es_encriptado, true, fecha);
        }
        return mensaje;
    }

    private void analizar_txt_conversaciones() {
        if (lista_conversaciones.size() == 0) {
            txt_conversaciones.setText("No hay conversaciones registradas...");
        } else {
            txt_conversaciones.setText("Hay " + lista_conversaciones.size() + " conversaciones...");
        }
    }

    public int existe_conversacion(String numero) {
        return FUNC.existe_conversacion(lista_conversaciones, numero);
    }

    public String nombre_contacto_x_numero(String numero) {
        return FUNC.nombre_contacto_x_numero(lista_contactos, numero);
    }

    private void lanzar_bienvenida_al_usuario() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.bienvenido, (ViewGroup) findViewById(R.id.layout_bienvenido));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
