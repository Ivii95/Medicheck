package com.example.medicheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Toast;

import com.example.medicheck.NFC.NFCManager;
import com.example.medicheck.R;
import com.example.medicheck.data.Avisos;
import com.example.medicheck.data.AvisosAdapter;
import com.example.medicheck.data.AvisosRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.medicheck.NFC.NFCManager.intentFiltersArray;
import static com.example.medicheck.NFC.NFCManager.mCurrentTag;
import static com.example.medicheck.NFC.NFCManager.mNfcManager;
import static com.example.medicheck.NFC.NFCManager.nfcAdpt;
import static com.example.medicheck.NFC.NFCManager.pendingIntent;
import static com.example.medicheck.NFC.NFCManager.techList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView reyclerView;
    private AvisosAdapter mAdapter;


    //TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarUI();
        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
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

    private void iniciarUI() {
        reyclerView = (RecyclerView) findViewById(R.id.reyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        reyclerView.setHasFixedSize(true);
        // use a linear layout manager
        reyclerView.setLayoutManager(new LinearLayoutManager(this));
        // specify an adapter with the list to show
        mAdapter = new AvisosAdapter(new AvisosRepository().obtenerDatos());
        reyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mNfcManager.verifyNFC(this);
            Intent nfcIntent = new Intent(this, getClass());
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        } catch (NFCManager.NFCNotSupported nfcNotSupported) {
            nfcNotSupported.printStackTrace();
        } catch (NFCManager.NFCNotEnabled nfcNotEnabled) {
            nfcNotEnabled.printStackTrace();
        }
        nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcManager.disableDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        loquehacealpasarlatarjeta(intent);
        super.onNewIntent(intent);
    }

    private void loquehacealpasarlatarjeta(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            mCurrentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String mensaje = mNfcManager.readTag(mCurrentTag);
            if (mensaje != null) {
                reyclerView = (RecyclerView) findViewById(R.id.reyclerView);
                reyclerView.setHasFixedSize(true);
                reyclerView.setLayoutManager(new LinearLayoutManager(this));
                List<Avisos> lista = new AvisosRepository().obtenerDatos();
                lista.add(new Avisos(LocalDateTime.of(LocalDate.now(), LocalTime.now())
                        , "mensaje"));
                mAdapter = new AvisosAdapter(new AvisosRepository().obtenerDatos());
                reyclerView.setAdapter(mAdapter);
                //txt.setText(mensaje);
                //txt.setText(mensaje);
            }
        }
    }
}
