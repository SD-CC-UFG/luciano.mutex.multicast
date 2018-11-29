package com.lucianomoura.sdmulticastrl.Model;

import java.util.ArrayList;

public class Processo implements acoesProcesso {

    /*
    1.  Manda mensagem para todos os outros (com seu timestamp)
    2.       

     */


    //TODO: Implementar apenas para uma secao Critica toda no sistema
    //TODO:
    int id;
    EstadoProcesso estadoAtual;
    EstadoProcesso estadoQueQueroAcessar;
    int relogio;
    ArrayList<Processo> listaDeProcessos;
    ArrayList<Mensagem> filaDeMensagens;
    ArrayList<Boolean> listaDeRespostasSC;
    //ArrayList<Integer> listaDeSecaoCritica;

    public Processo(int id){
        this.id = id;
        inicializa();
        listaDeProcessos = new ArrayList<>();
        filaDeMensagens = new ArrayList<>();
        listaDeRespostasSC = new ArrayList<>();
    }


    @Override
    public void inicializa() {
        estadoAtual = EstadoProcesso.RELEASED;
        relogio = 0;
    }

    @Override
    public void enviaPedido(Mensagem mensagem) {
        //Comunicacao RMI para processoDestino (que será WANTED)

        //COLOCAR APENAS UMA SECAO CRITICA
        estadoQueQueroAcessar = EstadoProcesso.WANTED;

        multicast(); //Envia para todos estados que quer entrar na secao de Pi
        //espera todas respostas de n-1

        //estadoAtual = EstadoProcesso.HELD;
    }

    @Override
    public void multicast() {

        for(Processo processo : listaDeProcessos){
            if(processo.getId() != this.id){
                Mensagem mensagem = new Mensagem(this, processo, this.getRelogio());
                enviaPedido(mensagem);
            }
        }
    }

    @Override
    public void recebePedido(Mensagem mensagem/*, int secao*/){

        for(Mensagem msg : filaDeMensagens){

            //Se eu tiver uma mensagem para aquele mesmo destino
            if(mensagem.pDestino == msg.pDestino){

            }

        }

        if(mensagem.pDestino == this){
            //Se for pra mim
            filaDeMensagens.add(mensagem);
        }

        if(this.estadoAtual == EstadoProcesso.HELD
                || (this.estadoAtual == EstadoProcesso.WANTED
                && this.relogio < mensagem.pOrigem.relogio)){   //Se eu estiver na SC
                                                        //Ou tiver querendo e tiver menor relógio
            this.relogio = relogio+1;
            enfileraPedido(mensagem.pOrigem);
        }else{
            this.relogio = relogio+1;
            responde(mensagem.pDestino, EstadoProcesso.RELEASED);
        }
    }

    @Override
    public void entraNaSecao() {
        estadoAtual = EstadoProcesso.HELD;
        //executa acao
        saiDaSecao();
    }

    @Override
    public void saiDaSecao() {
        estadoAtual = EstadoProcesso.RELEASED;
        respondeEnfi3
        +leirados();
    }


    @Override
    public void enfileraPedido(Mensagem mensagem) {
        filaDeMensagens.add(mensagem);
    }

    @Override
    public void responde(Processo processo, EstadoProcesso estado) {
            //Envia mensagem para processo -
    }

    @Override
    public void respondeEnfileirados() {
        int menorTempoDeProcesso = retornaMenorTempoDaFilaDePedidos();
        for(Mensagem mensagem: filaDeMensagens){
            if(mensagem.pOrigem.getRelogio() == menorTempoDeProcesso){
                responde(mensagem.pOrigem, EstadoProcesso.RELEASED);
            }
        }
    }

    public int retornaMenorTempoDaFilaDePedidos()
    {
        int  menor = this.relogio;
        for(int i = 0; i< filaDeMensagens.size(); i++)
        {
            Processo processo = (Processo) filaDeMensagens.get(i);
            if(processo.getRelogio() < menor)
            {
                menor = processo.getRelogio();
            }
        }
        return menor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EstadoProcesso getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(EstadoProcesso estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public int getRelogio() {
        return relogio;
    }

    public void setRelogio(int relogio) {
        this.relogio = relogio;
    }

    public ArrayList<Processo> getListaDeProcessos() {
        return listaDeProcessos;
    }

    public void setListaDeProcessos(ArrayList<Processo> listaDeProcessos) {
        this.listaDeProcessos = listaDeProcessos;
    }
}
