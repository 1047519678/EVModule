package cn.mvc.tools;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SmbTools {

    // 日志记录
    private static Logger logger = Logger.getLogger(SmbTools.class);

    public SmbTools() {
    }

    /**
     *
     * 从smbUrl读取文件并存储到localpath指定的路径
     * @param smbUrl  共享机器的文件,如smb://xxx:xxx@192.168.1.1/myDocument/测试文本.txt,xxx:xxx是共享机器的用户名密码
     * @param localPath 本地路径：本地用File，共享设备用SmbFile
     */
    public static void readFromSmb(String smbUrl, String localPath){
        logger.info("IN：readFromSmb(),PARAM IS " + smbUrl + " AND " + localPath);
        SmbFile localfile;
        InputStream bis = null;
        OutputStream bos = null;
        try {
            SmbFile directory = new SmbFile(localPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            SmbFile rmifile = new SmbFile(smbUrl);
            String filename = rmifile.getName();
            logger.info("读取文件流：" + smbUrl);
            bis = new BufferedInputStream(new SmbFileInputStream(rmifile));
            localfile = new SmbFile(localPath + filename);
            logger.info("输出文件流：" + localPath);
            bos = new BufferedOutputStream(new SmbFileOutputStream(localfile));
            int length = rmifile.getContentLength();
            byte[] buffer = new byte[length];
            bis.read(buffer);
            bos.write(buffer);
            logger.info("文件拷贝完成。");
        } catch (Exception e) {
            logger.error("文件拷贝出错："+e.getMessage(), e);
        } finally {
            try {
                bos.close();
                bis.close();
            } catch (IOException e) {
                logger.error("文件流关闭出错："+e.getMessage(), e);
            }
        }
        logger.info("OUT：readFromSmb()");
    }

    /**
     * 递归遍历目录下所有文件
     */
    public static ArrayList<SmbFile> getListFiles(Object obj) throws IOException {
        SmbFile directory = null;
        if (obj instanceof SmbFile) {
            directory = (SmbFile) obj;
        } else {
            directory = new SmbFile(obj.toString());
        }
        ArrayList<SmbFile> files = new ArrayList<>();
        if (directory.isFile()) {
            files.add(directory);
            return files;
        } else if (directory.isDirectory()) {
            SmbFile[] fileArr = directory.listFiles();
            for (SmbFile fileOne : fileArr) {
                files.addAll(getListFiles(fileOne));
            }
        }
        return files;
    }

    /**
     * 读取共享文件夹下的所有文件(文件夹)的名称
     */
    public static ArrayList<String> getSharedFileList(String remoteUrl,String code) {
        logger.info("IN：getSharedFileList(),PARAM IS " + remoteUrl + " AND " + code);
        SmbFile smbFile;
        boolean b = false;
        ArrayList<String> fileName = new ArrayList<String>();
        int i = 0;
        try {
            // smb://userName:passWord@host/path/
            smbFile = new SmbFile(remoteUrl);
            if (!smbFile.exists()) {
                logger.info("读取目录下文件时出错：目录" + remoteUrl + "不存在。");
            } else {
                ArrayList<SmbFile> files = getListFiles(smbFile);
                //SmbFile[] files = smbFile.listFiles();
                for (SmbFile f : files) {
                    //System.out.println(f.getName());
                    /*if (f.isDirectory()){
                        SmbFile[] filess = smbFile.listFiles();
                        for (SmbFile ff : files) {
                            b = ff.getName().contains(code);
                            if (b){
                                fileName.add(ff.getName());
                            }
                        }
                    }*/
                    b = f.getName().contains(code);
                    if (b){
                        fileName.add(f.getName());
                    }
                }
            }
        } catch (IOException e) {
            logger.error("读取目录下文件时出错："+e.getMessage(), e);
        }
        logger.info("OUT：getSharedFileList()");
        return fileName;
    }

    /**
     * 创建文件夹
     *
     * @param remoteUrl
     * @param folderName
     * @return
     */
    public static void smbMkDir(String remoteUrl, String folderName) {
        SmbFile smbFile;
        try {
            // smb://userName:passWord@host/path/folderName
            smbFile = new SmbFile(remoteUrl + folderName);
            if (!smbFile.exists()) {
                smbFile.mkdir();
            }
        } catch (MalformedURLException | SmbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param remoteUrl
     * @param shareFolderPath
     * @param localFilePath
     * @param fileName
     */
    public static void uploadFileToSharedFolder(String remoteUrl, String shareFolderPath, String localFilePath, String fileName) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File localFile = new File(localFilePath);
            inputStream = new FileInputStream(localFile);
            // smb://userName:passWord@host/path/shareFolderPath/fileName
            SmbFile smbFile = new SmbFile(remoteUrl + shareFolderPath + "/" + fileName);
            smbFile.connect();
            outputStream = new SmbFileOutputStream(smbFile);
            byte[] buffer = new byte[4096];
            int len = 0; // 读取长度
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 刷新缓冲的输出流
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载文件到浏览器
     *
     * @param httpServletResponse
     * @param remoteUrl
     * @param shareFolderPath
     * @param fileName
     */
    public static void downloadFileToBrowser(HttpServletResponse httpServletResponse, String remoteUrl, String shareFolderPath, String fileName) {
        SmbFile smbFile;
        SmbFileInputStream smbFileInputStream = null;
        OutputStream outputStream = null;
        try {
            // smb://userName:passWord@host/path/shareFolderPath/fileName
            smbFile = new SmbFile(remoteUrl + shareFolderPath + "/" + fileName);
            smbFileInputStream = new SmbFileInputStream(smbFile);
            httpServletResponse.setHeader("content-type", "application/octet-stream");
            httpServletResponse.setContentType("application/vnd.ms-excel;charset=UTF-8");
            httpServletResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);
            // 处理空格转为加号的问题
            httpServletResponse.setHeader("Content-Disposition", "attachment; fileName=" + fileName + ";filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
            outputStream = httpServletResponse.getOutputStream();
            byte[] buff = new byte[2048];
            int len;
            while ((len = smbFileInputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                smbFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载文件到指定文件夹
     *
     * @param remoteUrl
     * @param shareFolderPath
     * @param fileName
     * @param localDir
     */
    public static void downloadFileToFolder(String remoteUrl, String shareFolderPath, String fileName, String localDir) {
        InputStream in = null;
        OutputStream out = null;
        try {
            SmbFile remoteFile = new SmbFile(remoteUrl + shareFolderPath + File.separator + fileName);
            File localFile = new File(localDir + File.separator + fileName);
            in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
            out = new BufferedOutputStream(new FileOutputStream(localFile));
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                out.write(buffer);
                buffer = new byte[1024];
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param remoteUrl
     * @param shareFolderPath
     * @param fileName
     */
    public static void deleteFile(String remoteUrl, String shareFolderPath, String fileName) {
        SmbFile SmbFile;
        try {
            // smb://userName:passWord@host/path/shareFolderPath/fileName
            SmbFile = new SmbFile(remoteUrl + shareFolderPath + "/" + fileName);
            if (SmbFile.exists()) {
                SmbFile.delete();
            }
        } catch (MalformedURLException | SmbException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String code = "";
        // 远程路径
        String smbUrlHead = "smb://72454:123456@192.168.65.253/mes/test";
        //String smbUrlHead = "D:";
        String smbUrlHot = "/HOTDATA";
        String smbUrlTest = "/TestData";
        // 本地存储路径
        String localPathHead = "D:/temp/";
        String localPathHot = code + "/HOTDATA";
        String localPathTest = code + "/TestData";

        getSharedFileList(smbUrlHead+smbUrlHot,code);

        File dirHot = new File(localPathHead +localPathHot);
        File dirTest = new File(localPathHead +localPathTest);
        if (!dirHot.exists()) {
            dirHot.mkdirs();
        }
        if (!dirTest.exists()) {
            dirTest.mkdirs();
        }
        /*File fileHot = readFromSmb(smbUrlHead +smbUrlHot, localPathHead +localPathHot);
        File fileTest = readFromSmb(smbUrlHead +smbUrlTest, localPathHead +localPathTest);*/
        //System.out.println(file.getName());
        System.out.println("导出完成。");
    }
}
