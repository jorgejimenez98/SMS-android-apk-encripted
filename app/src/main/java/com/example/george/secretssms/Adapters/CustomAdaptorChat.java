package com.example.george.secretssms.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.george.secretssms.R;
import com.example.george.secretssms.modelo.Chat;
import com.example.george.secretssms.modelo.Funciones_Ayuda;

import java.util.ArrayList;


public class CustomAdaptorChat extends RecyclerView.Adapter<CustomAdaptorChat.MyHolder> {
    private RecyclerTouchListener listener;
    private Funciones_Ayuda FUNC = new Funciones_Ayuda();

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<Chat> model;

    public CustomAdaptorChat(Context context, ArrayList<Chat> model) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    public ArrayList<Chat> getModel() {
        return model;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_view_row_conversaciones, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Chat chat = model.get(position);
        holder.nombre_contacto.setText("   " + chat.getNombre_contacto());
        String usuario = chat.nombre_usuario_ult_sms();
        if (usuario.equals("# no esta en los contactos")) {
            if (chat.getNumero_contacto().equals("Cubacel")) {
                usuario = "Cubacel";
            }
        }
        holder.ultimo_mensaje.setText("   " + FUNC.sms_resumido(usuario + ": " + chat.getUltimoSMS()));
        holder.numero_contacto.setText(chat.getNumero_contacto());
        holder.fecha.setText(chat.fecha_ultimo_sms());
        holder.hora.setText(chat.hora_ultimo_sms());
        holder.am_pm.setText(chat.am_pm_ultimo_sms());
    }

    public void setClickListener(RecyclerTouchListener value) {
        this.listener = value;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nombre_contacto, ultimo_mensaje, numero_contacto, fecha, hora, am_pm;

        public MyHolder(final View view) {
            super(view);
            nombre_contacto = (TextView) view.findViewById(R.id.nombre_contacto_chat);
            ultimo_mensaje = (TextView) view.findViewById(R.id.ultimo_mensaje_chat);
            numero_contacto = (TextView) view.findViewById(R.id.numero_contacto_chat);
            fecha = (TextView) view.findViewById(R.id.txt_fecha_chat);
            hora = (TextView) view.findViewById(R.id.txt_hora_chat);
            am_pm = (TextView) view.findViewById(R.id.txt_am_pm);
            view.setTag(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClickItem(view, getAdapterPosition());
                }
            });
        }
    }



}
