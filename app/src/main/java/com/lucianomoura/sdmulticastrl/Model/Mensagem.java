package com.lucianomoura.sdmulticastrl.Model;

public class Mensagem {
    Processo pOrigem;
    Processo pDestino;
    int tempo;

    public Mensagem(Processo pOrigem, Processo pDestino, int tempo) {
        this.pOrigem = pOrigem;
        this.pDestino = pDestino;
        this.tempo = tempo;
    }
}
