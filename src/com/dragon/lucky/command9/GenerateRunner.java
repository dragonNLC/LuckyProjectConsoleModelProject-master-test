package com.dragon.lucky.command9;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.*;

public class GenerateRunner {

    private static GenerateRunner sInstance;
    private CommandBean mCommand;

    private GenerateRunner() {

    }

    public static GenerateRunner getInstance() {
        if (sInstance == null) {
            synchronized (GenerateRunner.class) {
                if (sInstance == null) {
                    sInstance = new GenerateRunner();
                }
            }
        }
        return sInstance;
    }

    private CallbackListener mCallBackListener;

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        if (command.getCheckData() != null) {
            List<ResultBean> printData = new ArrayList<>();
            if (!FileReadHelper.checkFileExists(command.getPreviewFilePath())) {
                Log.i("导入文件不存在");
                return;
            }
            List<String> previewData = FileReadHelper.readFile(command.getPreviewFilePath());
            List<ResultBean> previewResult = new ArrayList<>();
            for (int i = 0; i < previewData.size(); i++) {
                String d = previewData.get(i);
                if (!d.startsWith("[") || !d.endsWith("]")) {
                    continue;//去除不符合条件的
                }
                String line = d.replace("[", "").replace("]", "");
                String[] splitLine = line.split(", ");
                if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                    byte[] splitLineInt = new byte[splitLine.length];
                    for (int j = 0; j < splitLine.length; j++) {
                        if (Utils.isNumeric(splitLine[j])) {
                            splitLineInt[j] = Byte.parseByte(splitLine[j]);
                        }
                    }
                    previewResult.add(new ResultBean(splitLineInt, i, 1));
                }
            }

            //统计数字出现的次数
            Map<Byte, CollectBean> collectNumberSize = new HashMap<>();
            for (int i = 0; i < previewResult.size(); i++) {
                ResultBean rb = previewResult.get(i);
                for (int j = 0; j < rb.getData().length; j++) {
                    Byte b = rb.getData()[j];
                    if (!collectNumberSize.containsKey(b)) {
                        CollectBean cb = new CollectBean();
                        collectNumberSize.put(b, cb);
                    }
                    CollectBean tempCb = collectNumberSize.get(b);
                    tempCb.setB(b);
                    tempCb.setCount(tempCb.getCount() + 1);
                }
            }

            List<CollectBean> collectData = new ArrayList<>();

            for (Byte key :
                    collectNumberSize.keySet()) {
                collectData.add(collectNumberSize.get(key));
            }
            collectData.sort((o1, o2) -> Integer.compare(o2.getCount(), o1.getCount()));
            collectNumberSize.clear();

            Map<Byte, Integer> collectNumberPosition = new HashMap<>();
            for (int i = 0; i < collectData.size(); i++) {
                collectNumberPosition.put(collectData.get(i).getB(), i + 1);
            }

            List<Integer> exists4 = new ArrayList<>();
            List<Integer> exists5 = new ArrayList<>();
            for (int i = 0; i < previewResult.size(); i++) {
                ResultBean data = previewResult.get(i);
                int existsCount = 0;
                for (int k = 0; k < mCommand.getCheckData().length; k++) {
                    for (int j = 0; j < data.getData().length; j++) {
                        if (mCommand.getCheckData()[k] == data.getData()[j]) {
                            existsCount++;
                            break;
                        }
                    }
                }
                if (existsCount >= 4) {
                    if (existsCount == 4) {
                        exists4.add(i);
                    } else {
                        exists5.add(i);
                    }
                    printData.add(data);
                }
            }
            Log.i("校验完毕，出现总条数：" + printData.size());

            StringBuilder sb = new StringBuilder();
            Log.i("出现5位个数：" + exists5.size());
            sb.append("出现5位个数：").append(exists5.size());
            sb.append("\n");
            for (int i = 0; i < exists5.size(); i++) {
                Integer index = exists5.get(i);
                ResultBean rb = previewResult.get(index);
                for (int j = 0; j < rb.getData().length; j++) {
                    sb.append(rb.getData()[j]);
                    sb.append(",");
                    sb.append(collectNumberPosition.get(rb.getData()[j]));
                    if (j != rb.getData().length - 1) {
                        sb.append(",");
                        sb.append(" ");
                    }
                }
                sb.append("\n");
            }

            Log.i("出现4位个数：" + exists4.size());
            sb.append("出现4位个数：").append(exists4.size());
            sb.append("\n");
            for (int i = 0; i < exists4.size(); i++) {
                Integer index = exists4.get(i);
                ResultBean rb = previewResult.get(index);
                for (int j = 0; j < rb.getData().length; j++) {
                    sb.append(rb.getData()[j]);
                    sb.append(",");
                    sb.append(collectNumberPosition.get(rb.getData()[j]));
                    if (j != rb.getData().length - 1) {
                        sb.append(",");
                        sb.append(" ");
                    }
                }
                sb.append("\n");
            }
            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mCallBackListener != null) {
                mCallBackListener.onCompile();
            }
            return;
        }
    }

}
