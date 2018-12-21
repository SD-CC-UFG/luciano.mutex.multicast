package com.lucianomoura.sdmutexmulticast2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lucianomoura.sdmutexmulticast2.Model.MyProcess;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RelativeLayout relativeLayout0, relativeLayout1, relativeLayout2, relativeLayout3;
    TextView txt0, txt1, txt2, txt3;

    MyProcess process0, process1, process2, process3;

    public ArrayList<MyProcess> processList;

    private static final int CAMERA_REQUEST = 123;
    boolean hasCameraFlash = false;

    Camera cam;
    Camera.Parameters p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind de estruturas de tela
        relativeLayout0 = findViewById(R.id.main_relative_0);
        relativeLayout1 = findViewById(R.id.main_relative_1);
        relativeLayout2 = findViewById(R.id.main_relative_2);
        relativeLayout3 = findViewById(R.id.main_relative_3);

        txt0 = findViewById(R.id.main_txt_0);
        txt1 = findViewById(R.id.main_txt_1);
        txt2 = findViewById(R.id.main_txt_2);
        txt3 = findViewById(R.id.main_txt_3);


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


        Camera cam = Camera.open();
        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        //Criação dos processos
        process0 = new MyProcess(0, relativeLayout0, txt0, this);
        process1 = new MyProcess(1, relativeLayout1, txt1, this);
        process2 = new MyProcess(2, relativeLayout2, txt2, this);
        process3 = new MyProcess(3, relativeLayout3, txt3, this);

        processList = new ArrayList<>();

        processList.add(process0);
        processList.add(process1);
        processList.add(process2);
        processList.add(process3);

        process0.inicializa(/*processList*/);
        process1.inicializa(/*processList*/);
        process2.inicializa(/*processList*/);
        process3.inicializa(/*processList*/);


        if(hasCameraFlash){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 8 seconds
                    //process4.acessaRegiaoCritica(MainActivity.this);
                }
            }, 5000);
        }

        relativeLayout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process0.solicitaRegiaoCritica();
            }
        });

        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process1.solicitaRegiaoCritica();
            }
        });


        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process2.solicitaRegiaoCritica();
            }
        });


        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process3.solicitaRegiaoCritica();
            }
        });

    }

    public void msgRegiaoCriticaAcessada(String id){
        Toast.makeText(getBaseContext(), "RC acessada por "+id, Toast.LENGTH_SHORT).show();
    }

    public void msgRelatorio(String relatorio){
        Toast.makeText(getBaseContext(), ""+relatorio, Toast.LENGTH_SHORT).show();
    }

    public void ligaFlash(String id){
        msgRegiaoCriticaAcessada(id);
        cam = Camera.open();
        p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();
    }

    public void desligaFlash(){
        cam.stopPreview();
        cam.release();
    }

    public ArrayList<MyProcess> getProcessList(){
        return this.processList;
    }

}