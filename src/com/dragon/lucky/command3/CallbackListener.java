package com.dragon.lucky.command3;


public interface CallbackListener {
    void onThreadCompile(String name, RunThread thread);

    void addThread(String name);
}
