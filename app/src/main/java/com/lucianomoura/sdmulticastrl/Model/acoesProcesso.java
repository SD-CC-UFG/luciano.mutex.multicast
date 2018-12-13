package com.lucianomoura.sdmulticastrl.Model;

import com.lucianomoura.sdmulticastrl.old.Mensagem;
import com.lucianomoura.sdmulticastrl.old.Processo;

public interface acoesProcesso {

    public void inicializa();
    //public void solicitaEntradaNaSecao(int relogio, Processo processo/*, int secao*/);
    public void enviaPedido(Mensagem mensagem/*, int secao*/);
    public void recebePedido(Mensagem mensagem/*, int secao*/);
    public void entraNaSecao();
    public void saiDaSecao();
    public void multicast();
    public void enfileraPedido(Mensagem mensagem);
    public void responde(Processo processo, EstadoProcesso estado);
    public void respondeEnfileirados();

}
