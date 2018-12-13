package com.lucianomoura.sdmulticastrl;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lucianomoura.sdmulticastrl.Model.ProcessMutex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    RelativeLayout relativeBg;
    TextView txtEstado;
    Button btnSolicitarEntrada;

    ProcessMutex processo;

    private ServerSocket serverSocket;
    Handler updateConversationHandler;

    Thread serverThread = null;
    public static final int SERVERPORT = 6000;


    private Socket socketCliente;
    private static final int SERVERPORTCLIENTE = 5000;

    private static final String SERVER_IP = "10.0.2.2";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeBg = findViewById(R.id.main_relative);
        txtEstado = findViewById(R.id.main_txt_estado);
        btnSolicitarEntrada = findViewById(R.id.main_btn_solicitarEntrada);

        //(int idPar, String idBluetooth){
        processo = new ProcessMutex(0, "idBluetooth");

        inicializaSocketServidor();

        btnSolicitarEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicializaSocketCliente();
            }
        });


    }
    private void inicializaSocketServidor() {
        updateConversationHandler = new Handler();

        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();
    }

    private void inicializaSocketCliente() {
        new Thread(new ClientThread()).start();
    }

    private String getToken() {
       return PreferenceManager.getDefaultSharedPreferences(this).getString("token", "0");
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();

    }


    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socketCliente = new Socket(serverAddr, SERVERPORTCLIENTE);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    class ServerThread implements Runnable {

        public void run() {
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(SERVERPORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {

                try {

                    socket = serverSocket.accept();

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            while (!Thread.currentThread().isInterrupted()) {

                try {

                    String read = input.readLine();

                    updateConversationHandler.post(new updateUIThread(processo.getId()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    class updateUIThread implements Runnable {
        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            txtEstado.setText(txtEstado.getText().toString()+"Client Says: "+ msg + "\n");
        }
    }
}
