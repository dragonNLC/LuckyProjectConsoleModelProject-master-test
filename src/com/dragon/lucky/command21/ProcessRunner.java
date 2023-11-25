package com.dragon.lucky.command21;

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
                        GenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                mCommandIdx++;
                Log.i("开始执行第：" + mCommandIdx + "条命令。");
            }
        } else {
            GenerateRunner.getInstance().run(commandBean, ProcessRunner.this);
        }
    }

    private List<OnceGenerateBean> generateData = new ArrayList<>();

    @Override
    public void onCompile(OnceGenerateBean data) {
        System.gc();
        generateData.add(data);
        if (mCommands != null && mCommandIdx < mCommands.size()) {
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
            String path = "";
            String name = "";
            if (!Utils.isEmpty(CommandAnalysis.getInstance().getCommand().getCommandsPath())) {
                path = CommandAnalysis.getInstance().getCommand().getCommandsOutputPath();
                name = new File(CommandAnalysis.getInstance().getCommand().getCommandsPath()).getName().split("\\.")[0];
                if (Utils.isEmpty(path)) {
                    path = new File(CommandAnalysis.getInstance().getCommand().getCommandsPath()).getParentFile().getAbsolutePath() + File.separator + name + "-" + getDataStr() + ".xlsx";
                }
            } else {
                path = CommandAnalysis.getInstance().getCommand().getInputPath();
                name = new File(path).getName().split("\\.")[0];
                path = new File(path).getParentFile().getAbsolutePath() + File.separator + name + "-" + getDataStr() + ".xlsx";
            }
            try {
                POIExcelUtils.write2File(path, generateData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
       return sdf.format(new Date());
    }

}
