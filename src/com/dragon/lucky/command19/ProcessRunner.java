package com.dragon.lucky.command19;

import com.dragon.lucky.bean.TwoPoint;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessRunner implements CallbackListener{

    public volatile static ProcessRunner sRunner;
    private List<String> mCommands;
    private int mCommandIdx;

    public List<TwoPoint> redNumber;
    public List<TwoPoint> yellowNumber;
    public List<TwoPoint> oriNumber;
    public List<TwoPoint> blueNumber;

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

    public List<TwoPoint> getRedNumber() {
        return redNumber;
    }

    public void setRedNumber(List<TwoPoint> redNumber) {
        this.redNumber = redNumber;
    }

    public List<TwoPoint> getYellowNumber() {
        return yellowNumber;
    }

    public void setYellowNumber(List<TwoPoint> yellowNumber) {
        this.yellowNumber = yellowNumber;
    }

    public List<TwoPoint> getOriNumber() {
        return oriNumber;
    }

    public void setOriNumber(List<TwoPoint> oriNumber) {
        this.oriNumber = oriNumber;
    }

    public List<TwoPoint> getBlueNumber() {
        return blueNumber;
    }

    public void setBlueNumber(List<TwoPoint> blueNumber) {
        this.blueNumber = blueNumber;
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                mCommandIdx++;
                Log.i("开始执行第：" + mCommandIdx + "条命令。");
            }
        } else {
            GenerateRunner.getInstance().run(commandBean, null);
        }
    }

    private List<OnceGenerateBean> generateData = new ArrayList<>();

    @Override
    public void onCompile(OnceGenerateBean data) {
        System.gc();
        generateData.add(data);
        if (mCommandIdx < mCommands.size()) {
            CommandBean singleCommand = new CommandBean();
            String[] commandSplit = mCommands.get(mCommandIdx).split(" ");
            CommandAnalysis.getInstance().analysisCommand(commandSplit, singleCommand);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        GenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            mCommandIdx++;
            Log.i("开始执行第：" + mCommandIdx + "条命令。");
        } else {
            Log.i("所有命令处理完毕");
            String path = "";
            String name = "";
            if (!Utils.isEmpty(CommandAnalysis.getInstance().getCommand().getCommandsPath())) {
                path = CommandAnalysis.getInstance().getCommand().getCommandsOutputPath();
                name = new File(CommandAnalysis.getInstance().getCommand().getCommandsPath()).getName().split("\\.")[0];
                if (Utils.isEmpty(path)) {
                    path = new File(CommandAnalysis.getInstance().getCommand().getCommandsPath()).getParentFile().getAbsolutePath() + File.separator + name + "-" + getDataStr() + ".xls";
                }
            } else {
                path = CommandAnalysis.getInstance().getCommand().getInputPath();
                name = new File(path).getName().split("\\.")[0];
                path = new File(path).getParentFile().getAbsolutePath() + File.separator + name + "-" + getDataStr() + ".xls";
            }
            ExcelUtils.outputFile(path, name, generateData, redNumber, yellowNumber, oriNumber, blueNumber);
        }
    }

    private String getDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return sdf.format(new Date());
    }

}
