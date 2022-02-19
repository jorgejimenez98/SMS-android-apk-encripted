package com.example.george.secretssms.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.george.secretssms.R;
import com.example.george.secretssms.modelo.Mensaje;

import java.util.ArrayList;

public class SMS_Adapter extends RecyclerView.Adapter<SMS_Adapter.MyHolder> {
    RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }


    Context context;
    LayoutInflater inflater;
    ArrayList<Mensaje> modelo;

    public SMS_Adapter(Context context, ArrayList<Mensaje> model) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.modelo = model;
    }

    public ArrayList<Mensaje> getModelo() {
        return modelo;
    }

    public void setModelo(ArrayList<Mensaje> modelo) {
        this.modelo = modelo;
    }

    public int getItemCount() {
        return modelo.size();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_mensaje, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Mensaje sms = modelo.get(position);
        if (!sms.isEnviado()) {
            holder.fecha.setBackgroundColor(Color.WHITE);
            holder.fecha.setTextColor(Color.BLACK);
            holder.izquierdo.setBackgroundColor(Color.WHITE);
            holder.derecho.setBackgroundColor(Color.WHITE);
            holder.mensaje.setBackgroundColor(Color.WHITE);
            holder.mensaje.setTextColor(Color.BLACK);
            holder.layout.setGravity(Gravity.START);
            holder.fecha.setGravity(Gravity.START);
            holder.fecha.setText("      " + sms.getFecha());
        } else {
            holder.fecha.setBackgroundColor(Color.DKGRAY);
            holder.fecha.setTextColor(Color.WHITE);
            holder.izquierdo.setBackgroundColor(Color.DKGRAY);
            holder.derecho.setBackgroundColor(Color.DKGRAY);
            holder.mensaje.setBackgroundColor(Color.DKGRAY);
            holder.mensaje.setTextColor(Color.WHITE);
            holder.layout.setGravity(Gravity.END);
            holder.fecha.setGravity(Gravity.END);
            holder.fecha.setText(sms.getFecha() + "       ");
        }
        holder.mensaje.setText(sms.getMensaje());
    }

    public void setClickListener(RecyclerTouchListener value) {
        this.listener = value;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView izquierdo, mensaje, derecho, fecha;
        LinearLayout layout;

        public MyHolder(final View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout_sms);
            izquierdo = (TextView) view.findViewById(R.id.izquierdo);
            derecho = (TextView) view.findViewById(R.id.derecho);
            mensaje = (TextView) view.findViewById(R.id.mensaje);
            fecha = (TextView) view.findViewById(R.id.txt_fecha_sms);
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
