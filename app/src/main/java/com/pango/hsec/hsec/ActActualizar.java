package com.pango.hsec.hsec;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


public class ActActualizar extends AppCompatActivity {
    Dialog customDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_actualizar);
        final String urlPlay = "https://play.google.com/store/apps/details?id=com.pango.hsec.hsec";

        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new Dialog(this,R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog);

        TextView titulo = (TextView) customDialog.findViewById(R.id.titulo);
        titulo.setText("Nueva Actualización");

        TextView contenido = (TextView) customDialog.findViewById(R.id.contenido);
        contenido.setText("Estimado usuario hay una nueva versión de HSEC Móvil disponible, ¿Deseas instalarla ahora?");
        ((Button) customDialog.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();
                //Toast.makeText(ActActualizar.this, "Actualizar", Toast.LENGTH_SHORT).show();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPlay)));
                } catch (Exception e) {
                    Log.e(TAG, "Aplicación no instalada.");
                    //Abre url de pagina.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPlay)));
                }
                finish();
            }
        });

        ((Button) customDialog.findViewById(R.id.cancelar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                //Toast.makeText(ActActualizar.this, "Recordar más tarde", Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent().setClass(ActActualizar.this, Login.class);
                startActivity(mainIntent);
                finish();
            }
        });

        customDialog.show();
    }
}




