package com.dragon.lucky.command6;


public interface CallbackListener {
    void onThreadCompile(String name, RunThread thread);

    void addThread(String name);
}
