package com.example.medicheck.NFC;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

import com.example.medicheck.MainActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;

import static com.example.medicheck.MainActivity.nfcAdpt;

public class NFCManager {

    public NFCManager(MainActivity mainActivity) {

    }

    public void verifyNFC(Activity activity) throws NFCNotSupported, NFCNotEnabled {
        nfcAdpt = NfcAdapter.getDefaultAdapter(activity);
        if (nfcAdpt == null)
            throw new NFCNotSupported();
        if (!nfcAdpt.isEnabled())
            throw new NFCNotEnabled();
    }
    @SuppressLint("MissingPermission")
    public void disableDispatch(Activity activity) {
        nfcAdpt.disableForegroundDispatch(activity);
    }


    public void writeTag(Tag tag, NdefMessage message) {
        if (tag != null) {
            try {
                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag == null) {
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null) {
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                    }
                } else {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String readTag(Tag tag) {
        NdefMessage Ndefmessage;
        try {
            if (tag != null) {
                Ndef ndefTag = Ndef.get(tag);
                ndefTag.connect();
                Ndefmessage = ndefTag.getNdefMessage();
                ndefTag.close();
                NdefRecord[] records = Ndefmessage.getRecords();
                for (NdefRecord ndefRecord : records) {
                    if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                        try {
                            return readText(ndefRecord);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException | FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readText(NdefRecord ndefRecord) throws UnsupportedEncodingException {
        byte[] payload = ndefRecord.getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    public NdefMessage createUriMessage(String content, String type) {
        NdefRecord record = NdefRecord.createUri(type + content);
        NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
        return msg;
    }

    public NdefMessage createTextMessage(String content) {
        try {
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NdefMessage createGeoMessage() {
        String geoUri = "geo:" + 48.471066 + "," + 35.038664;
        NdefRecord geoUriRecord = NdefRecord.createUri(geoUri);
        return new NdefMessage(new NdefRecord[]{geoUriRecord});
    }


    public static class NFCNotSupported extends Exception {
        public NFCNotSupported() {
            super();
        }
    }

    public static class NFCNotEnabled extends Exception {
        public NFCNotEnabled() {
            super();
        }
    }

}

