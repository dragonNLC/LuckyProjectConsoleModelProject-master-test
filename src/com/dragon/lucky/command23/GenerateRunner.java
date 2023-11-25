package com.dragon.lucky.command23;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateRunner {

    private static GenerateRunner sInstance;


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

    public void run(CommandBean command) throws IOException {
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        List<String> inputData = FileReadHelper.readFile(command.getInputPath());
        List<AssistContentBean> result = new ArrayList<>();
        for (int i = 0; i < inputData.size(); i++) {
            String input = inputData.get(i);
            File file = new File(input);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            List<String> data = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (Utils.isEmpty(line)) {
                    continue;
                }
                if (line.startsWith("[")) {
                    break;
                }
                data.add(line);
            }
            if (data.size() >= 6) {
                int size = Integer.parseInt(data.get(0).split("：")[1].replace("-", "").replace("条", ""));
                int z1 = 0;
                if (data.get(1).contains("%")) {
                    z1 = Integer.parseInt(data.get(1).trim().split(" {5}")[0].split("：")[1]);
                } else {
                    z1 = Integer.parseInt(data.get(1).trim().split("：")[1]);
                }
                int z2 = 0;
                if (data.get(2).contains("%")) {
                    z2 = Integer.parseInt(data.get(2).trim().split(" {5}")[0].split("：")[1]);
                } else {
                    z2 = Integer.parseInt(data.get(2).trim().split("：")[1]);
                }
                int z3 = 0;
                if (data.get(3).contains("%")) {
                    z3 = Integer.parseInt(data.get(3).trim().split(" {5}")[0].split("：")[1]);
                } else {
                    z3 = Integer.parseInt(data.get(3).trim().split("：")[1]);
                }
                int z4 = 0;
                if (data.get(4).contains("%")) {
                    z4 = Integer.parseInt(data.get(4).trim().split(" {5}")[0].split("：")[1]);
                } else {
                    z4 = Integer.parseInt(data.get(4).trim().split("：")[1]);
                }
                int z5 = 0;
                if (data.get(5).contains("%")) {
                    z5 = Integer.parseInt(data.get(5).trim().split(" {5}")[0].split("：")[1]);
                } else {
                    z5 = Integer.parseInt(data.get(5).trim().split("：")[1]);
                }
                result.add(new AssistContentBean(file.getName().split("\\.")[0], size, z1, z2, z3, z4, z5));
            }
        }
        String path = "";
        String name = "";
        if (!Utils.isEmpty(command.getOutputPath())) {
            name = new File(command.getOutputPath()).getName().split("\\.")[0];
            path = new File(command.getOutputPath()).getParentFile().getAbsolutePath() + File.separator + name + "-" + getDataStr() + ".xls";
        } else {
            path = CommandAnalysis.getInstance().getCommand().getInputPath();
            name = new File(path).getName().split("\\.")[0];
            path = new File(path).getParentFile().getAbsolutePath() + File.separator + name + "-" + getDataStr() + ".xls";
        }
        ExcelUtils.write2File(path, result, command.getRowItem());
    }

    private String getDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return sdf.format(new Date());
    }

}
