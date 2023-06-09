package com.dragon.lucky.command;


public interface CallbackListener {
    void onThreadCompile(String name, RunThread thread);

    void addThread(String name);
}
