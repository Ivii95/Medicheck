package com.example.medicheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.medicheck.NFC.NFCManager;
import com.example.medicheck.data.Avisos;
import com.example.medicheck.data.AvisosAdapter;
import com.example.medicheck.data.AvisosRepository;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    private RecyclerView reyclerView;
    private AvisosAdapter mAdapter;
    public static AvisosRepository lista = new AvisosRepository();

    public static NfcAdapter nfcAdpt;
    public static NFCManager mNfcManager;
    public static Tag mCurrentTag;
    public static NdefMessage mNfcMessage;
    public static Dialog mDialog;
    public static PendingIntent pendingIntent;
    public static IntentFilter[] intentFiltersArray;
    public static String[][] techList;


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
            setIntent(nfcIntent);
            loquehacealpasarlatarjeta(nfcIntent);
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
        setIntent(intent);
        loquehacealpasarlatarjeta(intent);
        super.onNewIntent(intent);
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
                lista.insert(new Avisos(LocalDate.now(),recibido));
                mAdapter = new AvisosAdapter(lista.obtenerDatos());
                reyclerView.setAdapter(mAdapter);
            }
        }
    }
}
