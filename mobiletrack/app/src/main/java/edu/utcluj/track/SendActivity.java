package edu.utcluj.track;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SendActivity extends AppCompatActivity implements View.OnClickListener {
    private Executor executor = Executors.newFixedThreadPool(1);
    private volatile Handler msgHandler;
    private static final String macAddress = getMacAddress();
    private static final String STATIC_LOCATION = "{" +
            "\"terminalId\":\"%s\"," +
            "\"latitude\":\"%s\"," +
            "\"longitude\":\"%s\"" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Button sendButton = findViewById(R.id.button_send);
        sendButton.setOnClickListener(this);

        msgHandler = new MsgHandler(this);
    }

    private static String getMacAddress() {
        try {
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
            String stringMac = "";

            for (NetworkInterface networkInterface : networkInterfaceList) {
                if (networkInterface.getName().equalsIgnoreCase("wlon0")) ;
                {
                    for (int i = 0; i < networkInterface.getHardwareAddress().length; i++) {
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i] & 0xFF);
                        if (stringMacByte.length() == 1) {
                            stringMacByte = "0" + stringMacByte;
                        }
                        stringMac = stringMac + stringMacByte.toUpperCase() + ":";
                    }
                    break;
                }

            }
            return stringMac;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public void onClick(View v) {
        executor.execute(new Runnable() {
            public void run() {
                Message msg = msgHandler.obtainMessage();
                double latitude = 0.0;
                double longitude = 0.0;

                GpsTracker gpsTracker = new GpsTracker(SendActivity.this);

                if (gpsTracker.canGetLocation()) {
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();
                } else {
                    gpsTracker.showSettingsAlert();
                }

                // use MAC addr or IMEI as terminal id
                // read true position
                // replace static coordinates with the ones from the true position
                msg.arg1 = sendCoordinates(macAddress, Double.toString(latitude), Double.toString(longitude)) ? 1 : 0;
                msgHandler.sendMessage(msg);
            }
        });
    }

    private boolean sendCoordinates(String terminalId, String lat, String lng) {
        HttpURLConnection con = null;
        try {
            URL obj = new URL("http://10.0.2.2:8082/positions/save");
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(String.format(STATIC_LOCATION, terminalId, lat, lng).getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private static class MsgHandler extends Handler {
        private final WeakReference<Activity> sendActivity;

        public MsgHandler(Activity activity) {
            sendActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                Toast.makeText(sendActivity.get().getApplicationContext(),
                        "Success!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(sendActivity.get().getApplicationContext(),
                        "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
