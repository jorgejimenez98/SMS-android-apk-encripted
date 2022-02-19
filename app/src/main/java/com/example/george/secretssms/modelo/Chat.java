package com.example.george.secretssms.modelo;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Chat {
    private String nombre_contacto;
    private String numero_contacto;
    private ArrayList<Mensaje> lista_mensajes;

    public Chat(String nombre_contacto, String numero_contacto, ArrayList<Mensaje> lista_mensajes) {
        this.nombre_contacto = nombre_contacto;
        this.numero_contacto = numero_contacto;
        this.lista_mensajes = lista_mensajes;

    }

    public boolean es_numero_contacto(String value) {
        return getNumero_contacto().equals(value);
    }

    public String nombre_usuario_ult_sms() {
        if (getLista_mensajes().size() == 0) {
            return "No hay mensajes en el chat";
        }
        return getLista_mensajes().get(getLista_mensajes().size() - 1).getNombre_contacto();
    }

    public String getUltimoSMS() {
        if (getLista_mensajes().size() == 0) {
            return "No hay mensajes en el chat";
        }
        return getLista_mensajes().get(getLista_mensajes().size() - 1).getMensaje();
    }

    public String getNombre_contacto() {
        return nombre_contacto;
    }

    public void setNombre_contacto(String nombre_contacto) {
        this.nombre_contacto = nombre_contacto;
    }

    public String getNumero_contacto() {
        return numero_contacto;
    }

    public void setNumero_contacto(String numero_contacto) {
        this.numero_contacto = numero_contacto;
    }

    public ArrayList<Mensaje> getLista_mensajes() {
        return lista_mensajes;
    }

    public void setLista_mensajes(ArrayList<Mensaje> lista_mensajes) {
        this.lista_mensajes = lista_mensajes;
    }

    public String fecha_ultimo_sms() {
        if (getLista_mensajes().size() == 0) {
            return "dd/MM/yy hh:mm aa";
        }
        try {
            return getLista_mensajes().get(getLista_mensajes().size() - 1).getFecha().split(" ")[0];
        } catch (Exception e) {
            return "00/00/00";
        }
    }

    public String fec_ultimo_sms() {
        if (getLista_mensajes().size() == 0) {
            return "dd/MM/yy hh:mm aa";
        }
        return getLista_mensajes().get(getLista_mensajes().size() - 1).getFecha();
    }

    public String hora_ultimo_sms() {
        if (getLista_mensajes().size() == 0) {
            return "No hay sms";
        }
        try {
            return getLista_mensajes().get(getLista_mensajes().size() - 1).getFecha().split(" ")[1];
        } catch (Exception e) {
            return "00:00";
        }
    }

    public String am_pm_ultimo_sms() {
        if (getLista_mensajes().size() == 0) {
            return "No hay sms";
        }
        try {
            return getLista_mensajes().get(getLista_mensajes().size() - 1).getFecha().split(" ")[2];
        } catch (Exception e) {
            return "XX";
        }
    }

    public Calendar date_cal_ult_sms() {
        if (getLista_mensajes().size() == 0) {
            return null;
        }
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm aa");
            cal.setTime(sdf.parse(fec_ultimo_sms()));
            return cal;
        } catch (Exception e) {
            return null;
        }
    }

}
