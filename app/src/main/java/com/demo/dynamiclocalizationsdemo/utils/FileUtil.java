package com.demo.dynamiclocalizationsdemo.utils;

/**
 * Created by rahulhariharan on 11/07/18.
 */

import com.demo.dynamiclocalizationsdemo.DemoApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtil {
    public static String readString(String fileName) {
        return readString(DemoApplication.getContext().getFilesDir(), fileName);
    }

    public static String readString(File directory, String fileName) {
        String ret = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            File file = new File(directory, fileName);
            if (file.exists()) {
                StringBuilder builder = new StringBuilder();
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                String output;
                while ((output = bufferedReader.readLine()) != null) {
                    builder.append(output);
                }
                ret = builder.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();

                if (fileReader != null)
                    fileReader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return ret;
    }

    public static void writeString(String fileName, String data) {
        writeString(DemoApplication.getContext().getFilesDir(), fileName, data);
    }

    public static void writeString(File directory, String fileName, String data) {
        FileWriter fileWriter = null;
        try {
            File file = new File(directory, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            fileWriter = new FileWriter(file);
            fileWriter.write(data);

        } catch (Exception ex) {
            removeFile(fileName);
        } finally {
            try {
                if (fileWriter != null)
                    fileWriter.close();
            } catch (Exception ex) {
                removeFile(fileName);
            }
        }
    }

    public static void removeFile(String fileName) {
        removeFile(DemoApplication.getContext().getFilesDir(), fileName);
    }

    public static void removeFile(File directory, String fileName) {
        try {
            File file = new File(directory, fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
