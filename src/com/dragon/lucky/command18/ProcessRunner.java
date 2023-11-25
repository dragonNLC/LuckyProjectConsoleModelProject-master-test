package com.dragon.lucky.command18;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                        if (FileReadHelper.readFileLen(singleCommand.getPreviewPath()) > 1024 * 1024 * 500) {
                            LargeDataGenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                        } else {
                            GenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                mCommandIdx++;
                Log.i("开始执行第：" + mCommandIdx + "条命令。");
            }
        } else {
            if (FileReadHelper.readFileLen(commandBean.getPreviewPath()) > 1024 * 1024 * 500) {
                LargeDataGenerateRunner.getInstance().run(commandBean, ProcessRunner.this);
            } else {
                GenerateRunner.getInstance().run(commandBean, ProcessRunner.this);
            }
        }
    }

    @Override
    public void onCompile() {
        System.gc();
        if (mCommands != null && mCommandIdx < mCommands.size()) {
            CommandBean singleCommand = new CommandBean();
            String[] commandSplit = mCommands.get(mCommandIdx).split(" ");
            CommandAnalysis.getInstance().analysisCommand(commandSplit, singleCommand);
            new Thread(() -> {
                try {
                    if (FileReadHelper.readFileLen(singleCommand.getPreviewPath()) > 1024 * 1024 * 500) {
                        LargeDataGenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                    } else {
                        GenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                    }
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

    private String getDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
       return sdf.format(new Date());
    }

}
