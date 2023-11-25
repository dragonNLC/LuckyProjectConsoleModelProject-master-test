package com.dragon.lucky.command14;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.command13.NotExistsBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessRunner implements CallbackListener {

    public volatile static ProcessRunner sRunner;
    private List<String> mCommands;
    private int mCommandIdx;

    private ProcessRunner() {
        mCommandIdx = 0;
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
        if (!Utils.isEmpty(CommandAnalysis.getInstance().getCommand().getCommandsPath())) {
            //读取批量条件
            if (!Utils.isEmpty(commandBean.getCommandsPath())) {
                if (!FileReadHelper.checkFileExists(commandBean.getCommandsPath())) {
                    Log.i("预加载文件不存在");
                    return;
                }
                mCommands = FileReadHelper.readFile(commandBean.getCommandsPath());
                CommandBean singleCommand = new CommandBean();
                String[] commandSplit = mCommands.get(mCommandIdx).split(" ");
                CommandAnalysis.getInstance().analysisCommand(commandSplit, singleCommand);
                new Thread(() -> {
                    try {
                        GenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                mCommandIdx++;
                Log.i("开始执行第：" + mCommandIdx + "条命令。");
            }
        } else {
            GenerateRunner.getInstance().run(commandBean, null);
        }
    }

    @Override
    public void onCompile() {
        System.gc();
        if (mCommandIdx < mCommands.size()) {
            CommandBean singleCommand = new CommandBean();
            String[] commandSplit = mCommands.get(mCommandIdx).split(" ");
            CommandAnalysis.getInstance().analysisCommand(commandSplit, singleCommand);
            new Thread(() -> {
                try {
                    GenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            mCommandIdx++;
            Log.i("开始执行第：" + mCommandIdx + "条命令。");
        } else {
            Log.i("所有数据处理完毕！");
        }
    }

}
