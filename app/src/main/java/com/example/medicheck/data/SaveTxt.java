package com.example.medicheck.data;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class SaveTxt {
    File archivoFinal;
    FileReader fr;
    BufferedReader br;

    public SaveTxt(Context context) {
        try {
            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            context.openFileOutput("/data/prueba_int.txt", Context.MODE_PRIVATE));
            fout.write("Texto de prueba.");
            fout.close();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }

        /*File archivo = new File(Environment.getRootDirectory().getPath() + "/data/avisos.txt");
        if (archivo.exists()) {
            if (archivo.canRead()) {
                archivoFinal = archivo;
                Toast.makeText(context, "Archivo obtenido", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                if (archivo.createNewFile()) {
                    archivoFinal = archivo;
                    Toast.makeText(context, "Archivo creado por que no existe", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    }
}
