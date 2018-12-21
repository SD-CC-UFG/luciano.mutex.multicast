package com.lucianomoura.sdmutexmulticast2.Model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lucianomoura.sdmutexmulticast2.MainActivity;
import com.lucianomoura.sdmutexmulticast2.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class MyProcess implements ProcessActions{

    Integer pid;
    Integer lamportClock;
    ArrayList<Message> msgList;
    ArrayList<Boolean> mutexList;
    ProcessState state;
    TextView textView;
    RelativeLayout relativeLayout;
    Activity activity;
    int color;

    public MyProcess(Integer pid, RelativeLayout relativeLayout, TextView txtPar, Activity activity){
        this.pid = pid;
        this.lamportClock = pid;
        this.mutexList  =  new ArrayList<>();
        this.mutexList.add(false);this.mutexList.add(false);this.mutexList.add(false);this.mutexList.add(false);
        this.msgList = new ArrayList<>();
        this.relativeLayout = relativeLayout;
        this.textView = txtPar;
        this.activity = activity;
        color = ((ColorDrawable)relativeLayout.getBackground()).getColor();
    }

    @Override
    public void inicializa(/*ArrayList<MyProcess> processListPar*/) {
        //this.processList = processListPar;
        this.state = ProcessState.RELEASED;
        disparaProcessosAleatorios();
        mutexList.set(pid, true); // Seta para ele sempre true - detalhe de implementacao


        //Fica escutando a cada 1 segundos
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //LISTA

                        boolean answer = true;

                        //Se todas respostas forem verdadeiras, acessa RC
                        for(int i = 0 ; i<mutexList.size(); i++){
                            if(mutexList.get(i) == false){
                                answer = false;
                            }

                        }
                        if (answer){
                            acessaRegiaoCritica(activity, relativeLayout);
                            limpaListaMutex();
                            //cancel();
                        }
                    };
                });
            }
        }, 0, 2000);//1000 is a Refreshing Time (1second)
    }

    private void limpaListaMutex() {
        this.mutexList.clear();
        this.mutexList.add(false);this.mutexList.add(false);this.mutexList.add(false);this.mutexList.add(false);
        this.mutexList.set(pid,true);
    }

    @Override
    public void solicitaRegiaoCritica() {
        //TODO: Broadcast para todos
        this.state = ProcessState.WANTED;
        for(MyProcess process :   ((MainActivity)activity).getProcessList()){

            if(process != this){ // Se o processo não for ele mesmo

                // Construtor, (senderProcess, receiverProcess,  senderLamportClock)
                Message mensagem = new Message(this, process, this.lamportClock);
                enviaMensagem(mensagem);

            }

        }

    }

    @Override
    public void recebeSolicitacaoRegiaoCritica(Message msg) {


            if(this.state == ProcessState.RELEASED){
                ((MainActivity)activity).getProcessList().get(msg.getSenderProcess().getPid()).setMutexAuthorization(this.pid);
                //msg.getSenderProcess().setMutexAuthorization(this.pid);
            }

            else if(this.state == ProcessState.HELD){
                enfileira(msg);
            }

            else if(this.state == ProcessState.WANTED){

                if(this.lamportClock < msg.getSenderLamportClock()){
                    enfileira(msg);
                }else{
                    msg.getSenderProcess().setMutexAuthorization(this.pid);
                }
            }

        if(this.lamportClock < msg.getSenderLamportClock()){
                this.lamportClock = msg.getSenderLamportClock() +1 ;
        }else{
                this.lamportClock++;
        }
        textView.setText(String.valueOf(this.lamportClock));
    }

    @Override
    public void acessaRegiaoCritica(final Activity activity, RelativeLayout relativeLayoutPar) {
        this.state = ProcessState.HELD;

        ((MainActivity)activity).ligaFlash(String.valueOf(this.pid));

        manageBlinkEffect(activity, relativeLayoutPar);

        //Espera tempo aleatório para terminar o acesso da SC
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 8 seconds
                liberaRegiaoCritica(activity);
            }
        }, 4000);
    }

    @Override
    public void liberaRegiaoCritica(Activity activity) {
        ((MainActivity)activity).desligaFlash();
        this.state = ProcessState.RELEASED;
        desenfileira();
    }

    @Override
    public void enviaMensagem(Message msg) {
        msg.getReceiverProcess().recebeSolicitacaoRegiaoCritica(msg);
    }

    @Override
    public void enfileira(Message msg) {
        msgList.add(msg);
    }

    @Override
    public void desenfileira() {

        int object = 0;

        if(msgList.size()==0){
            ((MainActivity)activity).msgRelatorio("Lista de Solicitacoes de "+pid +"Vazia");
        }else{
            int menorValor = 99999;
            int menorId = 0;

            for(Message msgPar : msgList){

                if(msgPar.getSenderLamportClock() < menorValor){
                    menorValor = msgPar.getSenderLamportClock();
                    menorId = object;
                }

            object++;
            }
            enviaMensagem(msgList.get(menorId));
        }


    }

    public void disparaProcessosAleatorios(){
        final int[] random = {ThreadLocalRandom.current().nextInt(1, 5)};
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //random[0] += lamportClock;
                        lamportClock += random[0];
                        textView.setText(String.valueOf(lamportClock));
                    };
                });
            }
        }, 0, 4000);//1000 is a Refreshing Time (1second)

    }

    @SuppressLint("WrongConstant")
    private void manageBlinkEffect(final Activity activity, final RelativeLayout relativeLayout) {
        relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.White));
        //Espera tempo aleatório para terminar o acesso da SC
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 8 seconds
                relativeLayout.setBackgroundColor(color);
            }
        }, 4000);
    }

    public void setMutexAuthorization(Integer pid) {
        this.mutexList.set( pid, true);
    }

    public int getPid(){
        return this.pid;
    }

}
