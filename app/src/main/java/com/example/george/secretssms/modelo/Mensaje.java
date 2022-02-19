package com.example.george.secretssms.modelo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;


public class Mensaje implements Parcelable {

    private String nombre_contacto;
    private String numero_contacto;
    private String mensaje;
    private boolean encrpitado;
    private boolean enviado;
    private String fecha;

    public Mensaje(String nombre_contacto, String mensaje, String numero_contacto, boolean encrpitado,
                   boolean enviado, String fecha) {
        this.nombre_contacto = nombre_contacto;
        this.mensaje = mensaje;
        this.numero_contacto = numero_contacto;
        this.encrpitado = encrpitado;
        this.enviado = enviado;
        this.fecha = fecha;
    }


    public void cifrar() {
        byte[] encode = Base64.encode(getMensaje().getBytes(), Base64.DEFAULT);
        setMensaje(new String(encode) + " =");
        setEncrpitado(true);
    }

    public void descifrar() {
        String []x = getMensaje().split(" ");
        byte[] encode = Base64.decode(x[0].getBytes(), Base64.DEFAULT);
        setMensaje(new String(encode));
        setEncrpitado(false);
    }

    public boolean isEncrpitado() {
        return encrpitado;
    }

    public void setEncrpitado(boolean encrpitado) {
        this.encrpitado = encrpitado;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNumero_contacto() {
        return numero_contacto;
    }

    public void setNumero_contacto(String numero_contacto) {
        this.numero_contacto = numero_contacto;
    }

    public String getNombre_contacto() {
        return nombre_contacto;
    }

    public void setNombre_contacto(String nombre_contacto) {
        this.nombre_contacto = nombre_contacto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    protected Mensaje(Parcel in) {
        nombre_contacto = in.readString();
        mensaje = in.readString();
        numero_contacto = in.readString();
        encrpitado = in.readByte() != 0;
        enviado = in.readByte() != 0;
        fecha = in.readString();
    }

    public static final Creator<Mensaje> CREATOR = new Creator<Mensaje>() {
        @Override
        public Mensaje createFromParcel(Parcel source) {
            return new Mensaje(source);
        }

        @Override
        public Mensaje[] newArray(int size) {
            return new Mensaje[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel doest, int flags) {
        doest.writeString(nombre_contacto);
        doest.writeString(mensaje);
        doest.writeString(numero_contacto);
        doest.writeByte((byte) (encrpitado ? 1 : 0));
        doest.writeByte((byte) (enviado ? 1 : 0));
        doest.writeString(fecha);
    }


}
