package com.lucianomoura.sdmulticastrl.Model;

public class MensagemMutex {

    public int tempoDoRemetente;
    public int idRemetente;

    public MensagemMutex(int tempoDoRemetentePar, int idRemetentePar){
        this.tempoDoRemetente = tempoDoRemetentePar;
        this.idRemetente = idRemetentePar;
    }

    public int getTempoDoRemetente(){
        return this.tempoDoRemetente;
    }

    public int getIdRemetente(){
        return this.idRemetente;
    }


}
