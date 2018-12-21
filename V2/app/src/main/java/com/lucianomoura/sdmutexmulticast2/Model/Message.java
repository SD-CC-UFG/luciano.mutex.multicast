package com.lucianomoura.sdmutexmulticast2.Model;

public class Message {

    MyProcess senderProcess;
    MyProcess receiverProcess;
    int senderLamportClock;

    public Message(MyProcess senderProcess, MyProcess receiverProcess, int senderLamportClock) {
        this.senderProcess = senderProcess;
        this.receiverProcess = receiverProcess;
        this.senderLamportClock = senderLamportClock;
    }

    public MyProcess getSenderProcess() {
        return senderProcess;
    }

    public void setSenderProcess(MyProcess senderProcess) {
        this.senderProcess = senderProcess;
    }

    public MyProcess getReceiverProcess() {
        return receiverProcess;
    }

    public void setReceiverProcess(MyProcess receiverProcess) {
        this.receiverProcess = receiverProcess;
    }

    public int getSenderLamportClock() {
        return senderLamportClock;
    }

    public void setSenderLamportClock(int senderLamportClock) {
        this.senderLamportClock = senderLamportClock;
    }
}
