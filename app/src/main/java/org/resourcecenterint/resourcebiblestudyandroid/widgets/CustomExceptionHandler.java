package org.resourcecenterint.resourcebiblestudyandroid.widgets;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;


public class CustomExceptionHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler defaultUEH;
    private String localPath;
    public CustomExceptionHandler() {
        this.localPath =  Environment.getExternalStorageDirectory().getPath();
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {
        String timestamp = String.valueOf(new Date().getTime());
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = timestamp + "_RES.txt";
        if (this.localPath != null) {
            writeToFile(stacktrace, filename);
        }
        this.defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String stacktrace, String filename) {
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(this.localPath + "/" + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}