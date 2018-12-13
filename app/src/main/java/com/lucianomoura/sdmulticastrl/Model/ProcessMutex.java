package com.lucianomoura.sdmulticastrl.Model;

import java.util.ArrayList;

public class ProcessMutex {

    int id;
    String idBluetooth;
    int relogioLamport;

    ArrayList<ProcessMutex> listaDeOutrosProcessos;
    EstadoProcesso estado;

    ArrayList<Boolean> respostaDosProcessos;


    public ProcessMutex(int id){
        this.id = id;
    }

    public int getRelogioLamport(){return this.relogioLamport;}

    public String getId(){return String.valueOf(this.id);}

    public ProcessMutex(int idPar, String idBluetooth){
        this.id = idPar;
        this.idBluetooth = idBluetooth;
        estado = EstadoProcesso.RELEASED;
        relogioLamport = 0;
    }

    public void solicitaEntradaNaSeccaoCritica(){
        estado = EstadoProcesso.HELD;
        enviaMulticastParaTodosProcessos();
    }

    public void enviaMulticastParaTodosProcessos(){
        int idParaEnvioRelogio = this.relogioLamport;
        for(ProcessMutex processMutex : listaDeOutrosProcessos){
            //Envia mensagem bluetooth
        }
    }

    private void attemptSend() {
    }

    public void recebeMulticastDeProcesso(MensagemMutex mensagemRecebida){
        //Recebe mensagem bluetooth

        if(estado == EstadoProcesso.HELD ||
                (estado == EstadoProcesso.WANTED &&
                        (this.relogioLamport < mensagemRecebida.getTempoDoRemetente()) ) ){



            for(ProcessMutex processo : listaDeOutrosProcessos){
                if(processo.id == mensagemRecebida.getIdRemetente()){
                    listaDeOutrosProcessos.add(processo);
                }
            }

        }else{

        }
    }

    public boolean checkaSeRecebiTodasConfirmacoes(){

        boolean retorno = true;

        for(Boolean respostaDeProcesso: respostaDosProcessos){

            if(respostaDeProcesso == false){
                return false;
            }

        }

        return true;
    }



    private void entraNaSecaoCritica(){
        //Acessa Firebase
        //Pisca a tela
    }


}
