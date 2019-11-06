package com.example.medicheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import static com.example.medicheck.NFCManager.intentFiltersArray;
import static com.example.medicheck.NFCManager.mCurrentTag;
import static com.example.medicheck.NFCManager.mDialog;
import static com.example.medicheck.NFCManager.mNfcManager;
import static com.example.medicheck.NFCManager.mNfcMessage;
import static com.example.medicheck.NFCManager.nfcAdpt;
import static com.example.medicheck.NFCManager.pendingIntent;
import static com.example.medicheck.NFCManager.techList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inittializeUI();
        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        intentFiltersArray = new IntentFilter[]{};
        techList = new String[][]{{android.nfc.tech.Ndef.class.getName()}, {android.nfc.tech.NdefFormatable.class.getName()}};
        nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWrite();
            }
        });*/
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
    private void inittializeUI() {

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
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            mCurrentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (mNfcMessage != null) {
                mNfcManager.writeTag(mCurrentTag, mNfcMessage);
                mDialog.dismiss();
                Toast.makeText(this, "Tag written", Toast.LENGTH_SHORT).show();
            }else{
                String mensaje=mNfcManager.readTag(mCurrentTag);
                if(mensaje!=null){
                    //txt.setText(mensaje);
                }
            }
        }
    }

    /*public void onWrite() {
        String content = txt.getText().toString();
        switch (rGroup.getCheckedRadioButtonId()) {
            case R.id.rb_text:
                mNfcMessage = mNfcManager.createTextMessage(content);
                break;
            case R.id.rb_uri:
                mNfcMessage = mNfcManager.createUriMessage(content, "https://www.");
                break;
            case R.id.rb_phone:
                mNfcMessage = mNfcManager.createUriMessage(content, "tel:");
                break;
            case R.id.rb_location:
                mNfcMessage = mNfcManager.createGeoMessage();
                break;
        }
        if (mNfcMessage != null) {
            mDialog = new ProgressDialog(MainActivity.this);
            ((ProgressDialog) mDialog).setMessage("Tag NFC Tag please");
            mDialog.show();
        }
    }*/
}
