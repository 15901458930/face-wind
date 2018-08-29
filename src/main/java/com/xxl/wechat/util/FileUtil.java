package com.xxl.wechat.util;

import com.xxl.wechat.service.FixAssetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public class FileUtil {


    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Base64图片转文件
     * @param base64Img
     * @param filePath
     */
    public static long generateImage(String base64Img,String filePath){
        BASE64Decoder decoder = new BASE64Decoder();
        long fileSize = -1;
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(base64Img);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片

            //TODO 文件大小限制
            double size = (double)b.length/1024;

            fileSize = b.length;

            log.info("{}文件大小：{}KB,{} byte",filePath,size,b.length);
            File file = new File(filePath);
            OutputStream out = new FileOutputStream(file);
            out.write(b);
            out.flush();
            out.close();

        } catch (Exception e) {
            throw new RuntimeException("Base64图片转化为文件失败",e);
        }
        return fileSize;
    }
}
