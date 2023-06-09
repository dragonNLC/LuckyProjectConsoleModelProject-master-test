package com.dragon.lucky;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadFile {

    public static void main(String[] args) throws IOException {
        String path = "D:\\cp\\1条件\\5.7.12.26.33out.txt";
        FileInputStream fis = new FileInputStream(path);
        int len = 0;
        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while ((len = fis.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, len));
            if (sb.toString().length() > 100000) {
                break;
            }
        }
        System.out.println("sb = " + sb.toString());
    }

}
