package cn.mvc.tools;

import java.io.*;
import java.util.ArrayList;

public class FileTools {

    public FileTools() {
    }

    public static void main(String[] args) {
        String flag = "20200826";
        String code = "112007020511V1612001B14";
        // 远程路径
        String fileUrlHot = "D:" + File.separator + "HOTDATA";
        String fileUrlTest = "D:" + File.separator + "TestData";
        // 本地存储路径
        String localPathHot = "D:" + File.separator + "Data" + File.separator + flag + File.separator + "HOTDATA";
        String localPathTest = "D:" + File.separator + "Data"  + File.separator + flag + File.separator + "TestData";
        ArrayList<String> hotFileNames = getFileNames(fileUrlHot, code);
        if (hotFileNames.iterator().hasNext()) {
            for (String fileName : hotFileNames) {
                readFromFile(fileUrlHot + File.separator + fileName, localPathHot);
            }
        }
        ArrayList<String> testFileNames = getFileNames(fileUrlTest, code);
        if (testFileNames.iterator().hasNext()) {
            for (String fileName : testFileNames) {
                readFromFile(fileUrlTest + File.separator + fileName, localPathTest);
            }
        }
        System.out.println("导出完成。");
    }

    public static ArrayList<String> getFileNames(String path, String code) {
        boolean b = false;
        File file = new File(path);
        ArrayList<String> fileNames = new ArrayList<String>();
        // 如果这个路径是文件夹
        if (!file.exists()) {
            System.out.println("no such Directory");
        } else {
            if (file.isDirectory()) {
                // 获取路径下的所有文件
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        // 如果还是文件夹 递归获取里面的文件 文件夹
                        b = f.getName().contains(code);
                        if (b) {
                            fileNames.add(f.getName());
                        }
                    }
                } else {
                    System.out.println("no such folder");
                }
            } else {
                System.out.println("文件：" + file.getPath());
            }
        }
        return fileNames;
    }


    public static void readFromFile(String fileUrl, String localPath) {
        File localFile = null;
        InputStream bis = null;
        OutputStream bos = null;
        int len = 0;
        File directory = new File(localPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            File file = new File(fileUrl);
            String fileName = file.getName();
            bis = new BufferedInputStream(new FileInputStream(file));
            localFile = new File(localPath + File.separator + fileName);
            System.out.println("localfile==" + localFile);
            bos = new BufferedOutputStream(new FileOutputStream(localFile));
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                bos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
