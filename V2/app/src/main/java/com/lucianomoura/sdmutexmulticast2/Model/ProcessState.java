package com.lucianomoura.sdmutexmulticast2.Model;

public enum  ProcessState {

        RELEASED,   //Fora da seção crítica
        WANTED,     //Quer entrar na seção crítica
        HELD        //Está na seção crítica

}
