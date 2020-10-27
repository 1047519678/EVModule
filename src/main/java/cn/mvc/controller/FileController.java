package cn.mvc.controller;

import cn.mvc.pojo.ExcelData;
import cn.mvc.tools.FileTools;
import cn.mvc.tools.POIUtils;
import cn.mvc.tools.SmbTools;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/excel")
public class FileController {

    @RequestMapping(value = "/jumpShow")
    public String jumpShow() {
        return "readFile";
    }

    @RequestMapping(value = "/read")
    @ResponseBody
    public JSONObject readFile(MultipartFile file)  {
        JSONObject json = new JSONObject();
        List list = null;
        String flag = "";
        int sheetNum = 0;
        String fileName = file.getOriginalFilename();
        List<ExcelData> dataList = new ArrayList<>();
        Workbook workbook = null;
        try{
        if (fileName != null) {
            InputStream fs = file.getInputStream();
                if (fileName.endsWith("xls")) {
                    //  2003版本
                    workbook = new HSSFWorkbook(fs);
                } else if (fileName.endsWith("xlsx")) {
                    //  2007版本
                    workbook = new XSSFWorkbook(fs);
                }
                fs.close();
            }else {
                json.put("code", 1);
                json.put("msg", "解析出错，请确保文档格式正确后，刷新重试！");
                return json;
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        if (workbook != null){
            sheetNum = workbook.getNumberOfSheets();
            for (int i = 0;i < sheetNum;i++){
                Sheet sheet = workbook.getSheetAt(i);
                flag = sheet.getSheetName();
                try {
                    list = POIUtils.readExcel(file, i, ExcelData.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (list != null) {
                    if (list.iterator().hasNext()) {
                        for (Object object : list) {
                            ExcelData excelData = (ExcelData) object;
                            String serialNo = excelData.getSerialNo();
                            //String serialNo = "112007000021V1901001A06";
                            String fileUrlHot = "smb://32801:123456@192.168.60.201/EV_TestInfo/HOTDATA/";
                            String fileUrlTest = "smb://32801:123456@192.168.60.201/EV_TestInfo/TestData/";
                            // 本地存储路径
                            String localPathHot = "smb://32801:123456@192.168.60.201/EV_TestInfo/Data/" + flag + "/HOTDATA/";
                            String localPathTest = "smb://32801:123456@192.168.60.201/EV_TestInfo/Data/" + flag + "/TestData/";
                            // 本地存储路径（测试用）
                            //String localPathHot = "smb://32801:123456@192.168.60.201/EV_TestInfo/Test/" + flag + "/HOTDATA/";
                            //String localPathTest = "smb://32801:123456@192.168.60.201/EV_TestInfo/Test/" + flag + "/TestData/";
                            ArrayList<String> hotFileNames = SmbTools.getSharedFileList(fileUrlHot, serialNo);
                            if (hotFileNames.iterator().hasNext()) {
                                for (String name : hotFileNames) {
                                    SmbTools.readFromSmb(fileUrlHot + File.separator + name, localPathHot);
                                }
                            }
                            ArrayList<String> testFileNames = SmbTools.getSharedFileList(fileUrlTest, serialNo);
                            if (testFileNames.iterator().hasNext()) {
                                for (String name : testFileNames) {
                                    SmbTools.readFromSmb(fileUrlTest + File.separator + name, localPathTest);
                                }
                            }
                        }
                    }
                }
            }
            json.put("code", 0);
            json.put("msg", "文件解析完成，数据已导出到" + "/Data/" + flag + "目录下！");
            //json.put("dataList", dataList);
        } else {
            json.put("code", 2);
            json.put("msg", "解析完成，该文档无数据。");
        }
        return json;
    }
}
