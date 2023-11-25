package com.dragon.lucky.command4;


public interface CallbackListener {
    void onThreadCompile(String name, RunThread thread);

    void addThread(String name);
}
