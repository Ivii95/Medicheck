package com.example.medicheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicheck.NFC.NFCManager;
import com.example.medicheck.data.Avisos;
import com.example.medicheck.data.AvisosAdapter;
import com.example.medicheck.data.AvisosRepository;
import com.example.medicheck.data.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyMgr;
    private String CHANNEL_ID = "abc";

    private RecyclerView reyclerView;
    private AvisosAdapter mAdapter;
    public AvisosRepository repositorio;

    public static NfcAdapter nfcAdpt;
    public static NFCManager mNfcManager;
    public static Tag mCurrentTag;
    public static NdefMessage mNfcMessage;
    public static Dialog mDialog;
    public static PendingIntent pendingIntentNFC;
    public static PendingIntent pendingIntentNotification;
    public static IntentFilter[] intentFiltersArray;
    public static String[][] techList;

    Usuario user;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarUI();
        nfcTools();
    }

    private void nfcTools() {
        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntentNFC = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        intentFiltersArray = new IntentFilter[]{};
        techList = new String[][]{{android.nfc.tech.Ndef.class.getName()}, {android.nfc.tech.NdefFormatable.class.getName()}};
        nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        mNfcManager = new NFCManager(this);
        try {
            mNfcManager.verifyNFC(this);
        } catch (NFCManager.NFCNotSupported nfcnsup) {
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_SHORT).show();
        } catch (NFCManager.NFCNotEnabled nfcnEn) {
            Toast.makeText(this, "NFC not enable", Toast.LENGTH_SHORT).show();
        }
        loquehacealpasarlatarjeta(getIntent());
    }

    private void iniciarHilo() {
        Thread hiloAvisar = new Thread(new hiloAvisar(), "hiloAvisar");
        hiloAvisar.run();
    }

    @SuppressLint("SetTextI18n")
    private void iniciarUI() {
        user = new Usuario(11, "Pablo", "Lorenzo", "Gutierrez");
        txt = (TextView) findViewById(R.id.textView);
        txt.setText(user.getNombre() + " " + user.getApellido1() + " " + user.getApellido2());
        repositorio = new AvisosRepository(this);
        reyclerView = (RecyclerView) findViewById(R.id.reyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        reyclerView.setHasFixedSize(true);
        reyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AvisosAdapter(repositorio.obtenerDatos());
        reyclerView.setAdapter(mAdapter);

    }


    public void notificacion(Avisos aviso) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "abc";
            String description = "hola mundo";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        mNotifyMgr = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        int icono = R.mipmap.ic_launcher;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntentNotification = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(icono)
                .setContentIntent(pendingIntentNotification)
                //.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle("Medicheck")
                .setContentText("Tiene que tomarse su medicamento de " + aviso.getFarmaco())
                .setVibrate(new long[]{100, 250, 100, 500})
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        mNotifyMgr.notify(1, mBuilder.build());
        notificationManager.notify(1, mBuilder.build());
    }

    class hiloAvisar extends Thread {
        public void run() {
            boolean bandera = true;
            do {
                Avisos aviso = AvisosRepository.comprobarFecha();
                if (aviso != null) {
                    notificacion(aviso);
                    bandera = false;
                } else {
                    try {
                        sleep(3600 * 1000); //para que se revise cada segundo, va por milisegundos
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            while (bandera);
        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        try {
            mNfcManager.verifyNFC(this);
            Intent nfcIntent = new Intent(this, getClass());
            setIntent(nfcIntent);
            loquehacealpasarlatarjeta(nfcIntent);
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntentNFC = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        } catch (NFCManager.NFCNotSupported | NFCManager.NFCNotEnabled nfcNotSupported) {
            nfcNotSupported.printStackTrace();
        }
        nfcAdpt.enableForegroundDispatch(this, pendingIntentNFC, intentFiltersArray, techList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        iniciarHilo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcManager.disableDispatch(this);
        repositorio.guardarDatos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repositorio.guardarDatos();
        iniciarHilo();
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        loquehacealpasarlatarjeta(intent);
        super.onNewIntent(intent);
    }

    public ArrayList<Avisos> dividirString(String recibido) {
        ArrayList<Avisos> avisos = new ArrayList<>();
        try {
            String[] parts = recibido.split(",");
            for (int i = 0; i < parts.length; i++) {
                String cod = parts[i]; // Codigo del usuario
                i++;
                String ano = parts[i]; // año
                i++;
                String mes = parts[i]; // mes
                i++;
                String dia = parts[i]; // dia
                i++;
                String farmaco = parts[i]; // farmaco
                int codigoUsuario = Integer.parseInt(cod);
                if (codigoUsuario == user.getId()) {
                    LocalDate fecha = LocalDate.of(Integer.parseInt("20" + ano), Integer.parseInt(mes), Integer.parseInt(dia));
                    Avisos aviso = new Avisos(fecha, farmaco);
                    avisos.add(aviso);
                }
            }
            return avisos;
        } catch (
                Exception e) {
            Toast.makeText(this, "Hubo algun error con el codigo", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void loquehacealpasarlatarjeta(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            mCurrentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String recibido = mNfcManager.readTag(mCurrentTag);
            if (recibido != null) {
                reyclerView = (RecyclerView) findViewById(R.id.reyclerView);
                reyclerView.setHasFixedSize(true);
                reyclerView.setLayoutManager(new LinearLayoutManager(this));
                ArrayList<Avisos> avisos = dividirString(recibido);
                for (int i = 0; i < avisos.size(); i++) {
                    Avisos aviso = avisos.get(i);
                    if (aviso != null) {
                        repositorio.insert(aviso);
                    }
                }
                mAdapter = new AvisosAdapter(repositorio.obtenerDatos());
                reyclerView.setAdapter(mAdapter);
            }
        }
    }


}
