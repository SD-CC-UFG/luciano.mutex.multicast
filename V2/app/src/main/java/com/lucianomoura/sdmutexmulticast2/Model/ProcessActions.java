package com.lucianomoura.sdmutexmulticast2.Model;

import android.app.Activity;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public interface ProcessActions {

    public void inicializa(/*ArrayList<MyProcess> processList*/);
    public void solicitaRegiaoCritica();
    public void recebeSolicitacaoRegiaoCritica(Message msg);
    public void acessaRegiaoCritica(Activity activity,  RelativeLayout relativeLayoutPar);
    public void liberaRegiaoCritica(Activity activity);
    public void enviaMensagem(Message msg);
    public void enfileira(Message msg);
    public void desenfileira();
}
