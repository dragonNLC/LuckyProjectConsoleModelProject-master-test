package com.dragon.lucky.command23;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessRunner {

    public volatile static ProcessRunner sRunner;

    private ProcessRunner() {
    }

    public static ProcessRunner getInstance() {
        if (sRunner == null) {
            synchronized (ProcessRunner.class) {
                sRunner = new ProcessRunner();
            }
        }
        return sRunner;
    }

    public void run(CommandBean commandBean) throws IOException {
        GenerateRunner.getInstance().run(commandBean);
    }

    private String getDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
       return sdf.format(new Date());
    }

}
