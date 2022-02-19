package com.example.george.secretssms.Activities;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.secretssms.R;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class Activity_Informacion extends SwipeBackActivity {
    Button btn_jorge, btn_manuel, btn_nota;
    Button imagen;
    TextView nombre, curso, anno, edad, proyecto, nombre_app, nota_final;
    Animation anim_left, move_right;
    RatingBar notas;
    SwipeBackLayout swipeBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        imagen = (Button) findViewById(R.id.imagen);
        btn_jorge = (Button) findViewById(R.id.btn_jorge);
        btn_manuel = (Button) findViewById(R.id.btn_manuel);
        btn_nota = (Button) findViewById(R.id.btn_nota);
        btn_jorge.setPaintFlags(btn_jorge.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_manuel.setPaintFlags(btn_manuel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_nota.setPaintFlags(btn_nota.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        nombre = (TextView) findViewById(R.id.txt_nombre);
        anno = (TextView) findViewById(R.id.txt_anno);
        edad = (TextView) findViewById(R.id.txt_edad);
        curso = (TextView) findViewById(R.id.txt_curso);
        proyecto = (TextView) findViewById(R.id.txt_proyecto);
        nombre_app = (TextView) findViewById(R.id.txt_nombre_app);
        notas = (RatingBar) findViewById(R.id.ratingBar);

        swipeBack = getSwipeBackLayout();
        int edgeFlag = SwipeBackLayout.EDGE_ALL;
        swipeBack.setEdgeTrackingEnabled(edgeFlag);

        analizar_botones();

        btn_jorge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_izquierda);
                imagen.setVisibility(View.VISIBLE);
                imagen.startAnimation(anim_left);
                imagen.setBackgroundResource(R.drawable.foto_jorge);

                move_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_izquierda);
                nombre.setVisibility(View.VISIBLE);
                anno.setVisibility(View.VISIBLE);
                edad.setVisibility(View.VISIBLE);
                curso.setVisibility(View.VISIBLE);
                nombre.startAnimation(move_right);
                anno.startAnimation(move_right);
                edad.startAnimation(move_right);
                curso.startAnimation(move_right);
                nombre.setText("Jorge Jimenez Diaz...");
                analizar_botones();

            }
        });

        btn_manuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_derecha);
                imagen.setVisibility(View.VISIBLE);
                imagen.startAnimation(anim_left);
                imagen.setBackgroundResource(R.drawable.foto_manuel);

                move_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_derecha);
                nombre.setVisibility(View.VISIBLE);
                anno.setVisibility(View.VISIBLE);
                edad.setVisibility(View.VISIBLE);
                curso.setVisibility(View.VISIBLE);
                nombre.startAnimation(move_right);
                anno.startAnimation(move_right);
                edad.startAnimation(move_right);
                curso.startAnimation(move_right);
                nombre.setText("Manuel Rodriguez Prieto...");
                analizar_botones();
            }
        });

        btn_nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float valor_notas = notas.getRating();
                try {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.layout_custom));
                    nota_final = (TextView) layout.findViewById(R.id.txt_nota_final);
                    nota_final.setText("La nota final es: " + valor_notas); // Arreglar esta parte
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 10, 10);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void analizar_botones() {
        if (nombre.getText().toString().equals("Jorge Jimenez Diaz...")) {
            btn_jorge.setEnabled(false);
            btn_manuel.setEnabled(true);
        } else {
            btn_manuel.setEnabled(false);
            btn_jorge.setEnabled(true);
        }
    }

}
