package com.example.george.secretssms.modelo;


public class Contacto {
    private String letra;
    private String contacto;

    public Contacto(String letra, String contacto) {
        this.letra = letra;
        this.contacto = contacto;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}
