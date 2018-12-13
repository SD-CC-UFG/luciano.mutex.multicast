package com.lucianomoura.sdmulticastrl.old;

public class Mensagem {
    Processo pOrigem;
    Processo pDestino;
    int tempo;

    public Mensagem(Processo pOrigem, Processo pDestino, int tempo) {
        this.pOrigem = pOrigem;
        this.pDestino = pDestino;
        this.tempo = tempo;
    }

    public Processo getpOrigem(){return this.pOrigem;}

    public Processo getpDestino(){return this.pDestino;}
}
