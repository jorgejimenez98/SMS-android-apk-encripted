package com.example.george.secretssms.modelo;

import java.util.ArrayList;

public class Funciones_Ayuda {

    public String nombre_contacto_x_numero(ArrayList<String> list, String numero) {
        String nombre, num;
        for (String contacto : list) {
            String[] lista = contacto.split(":");
            nombre = lista[0];
            num = lista[1].replace(" ", "");
            if (numero.equals(num)) {
                return nombre;
            }
        }
        return "# no esta en los contactos";
    }

    public String sms_resumido(String sms) {
        if (sms.length() > 50) {
            String res = "";
            char[] lista = sms.toCharArray();
            for (int i = 0; i < 50; i++) {
                res += String.valueOf(lista[i]);
            }
            return res + "...";
        }
        return sms;
    }

    public int existe_conversacion(ArrayList<Chat> list, String numero) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNumero_contacto().trim().equals(numero.trim())) {
                return i;
            }
        }
        return -1;
    }

    public void quicksort_x_string(ArrayList<String> lista, int start, int end) {
        int i = start, j = end;
        if (j - i >= 1) {
            String pivot = lista.get(i);
            while (j > i) {
                while (lista.get(i).compareToIgnoreCase(pivot) <= 0 && i < end && j > i) {
                    i++;
                }
                while (lista.get(j).compareToIgnoreCase(pivot) >= 0 && j > start && j >= i) {
                    j--;
                }
                if (j > i) {
                    swap(lista, i, j);
                }
            }
            swap(lista, start, j);
            quicksort_x_string(lista, start, j - 1);
            quicksort_x_string(lista, j + 1, end);
        }
    }

    public void orden_x_seleccion(ArrayList<Chat> lista) {
        int minimo;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).date_cal_ult_sms() != null){
                minimo = i;
                for (int e = i; e < lista.size(); e++) {
                    if (lista.get(minimo).date_cal_ult_sms() != null && lista.get(e).date_cal_ult_sms() != null){
                        if (lista.get(e).date_cal_ult_sms().compareTo(lista.get(minimo).date_cal_ult_sms()) < 0) {
                            minimo = e;
                        }
                    }
                }
                swap_chat(lista, i, minimo);
            }
        }
    }

    private void swap_chat(ArrayList<Chat> lista, int i, int j) {
        Chat aux = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, aux);
    }

    private void swap(ArrayList<String> lista, int i, int j) {
        String aux = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, aux);
    }

    public boolean existe_contacto(ArrayList<String> sms_list, String contacto) {
        for (int i = 0; i < sms_list.size(); i++) {
            if (sms_list.get(i).equalsIgnoreCase(contacto)) {
                return true;
            }
        }
        return false;
    }
}
