package com.xxl.wechat.service.office;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 生成excel的抽象类
 *
 * XSSFWorkbook 是操作excel的核心对象。
 * 生成excel的过程，其实就是在构建workbook的过程，构建完毕调用api转化为流存储为xls文件即可
 * author wangpeng
 * since ebs-v-1.0
 */
public abstract class BaseExcelService {

    private static Logger logger = LoggerFactory.getLogger(BaseExcelService.class);

    private XSSFWorkbook getXSSFWorkbookByTemplateFile(String fileName) {

        File templateFile = new File(BaseExcelService.class.getResource("/" + fileName).getFile());

        XSSFWorkbook workbook = null;
        //如果文件名称为空，则自己生成一个新的空白excel文件
        if (StringUtils.isBlank(fileName)) {
            workbook = new XSSFWorkbook();
        } else {
            try {
                workbook = new XSSFWorkbook(XSSFWorkbook.openPackage(templateFile.getPath()));
            } catch (IOException e) {
                logger.error("XSSFWorkbook读取{}下的模板文件出现异常:",templateFile.getParent(),e);
                throw new RuntimeException("XSSFWorkbook读取"+templateFile.getPath()+"下的模板文件出现异常:",e);
            }
        }
        return workbook;
    }

    /**
     * 由子类实现workbook的数据填充
     * @param workbook
     * @param path
     */
    public abstract void generateXSSFWorkbook(XSSFWorkbook workbook, String path);


    /**
     * 生成文件入口
     * @param templatePath 模板路径（如果为空则建一个空白的workbook,如果有值则使用该模板路径构建一个workbook）
     * @param desPath  将要生成的目标文件路径
     */
    public void generateExcel(String templatePath, String desPath) {

        //先初始化workbook
        XSSFWorkbook workbook = getXSSFWorkbookByTemplateFile(templatePath);

        //模板模式，由子类实现
        generateXSSFWorkbook(workbook, desPath);

        //将workbook对象写入文件，生成excel
        writeBook(workbook, desPath);
    }

    /**
     * 写入目标文件中
     * @param workbook 操作excel的对象
     * @param path
     */
    private void writeBook(XSSFWorkbook workbook, String path) {
        File file = new File(path);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            workbook.write(out);
        } catch (FileNotFoundException e) {
            logger.error("workbook将要写入的目标文件不存在", e);
            throw new RuntimeException("workbook将要写入的目标文件不存在(path:"+path+")",e);
        } catch (IOException e) {
            logger.error("workbook写入目标文件出现异常"+path, e);
            throw new RuntimeException("workbook写入目标文件出现异常(path:"+path+")",e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("关闭流异常",e);
                }
            }
        }
    }

    /**
     * 子类需要用的工具类（支持2种格式，数字或字符串）
     * @param wb
     * @param c
     * @param headerType
     * @param value
     */
    protected  void fillCellValue(XSSFWorkbook wb,Cell c,String headerType,String value){
        if(headerType.equals("dou") && StringUtils.isNotBlank(value)){

            c.setCellValue(Double.parseDouble(value));
            XSSFCellStyle cellStyle = wb.createCellStyle();
            XSSFDataFormat format= wb.createDataFormat();
            cellStyle.setDataFormat(format.getFormat("#,##0"));
            c.setCellStyle(cellStyle);
        }else{
            c.setCellValue(wb.getCreationHelper().createRichTextString(value));
        }
    }

    public void mkdir(String folder){
        File file = new File(folder);
        if(!file.exists()){
            file.mkdirs();
        }
    }
}
