package com.xxl.wechat.service.office;

import com.xxl.wechat.cache.DictCache;
import com.xxl.wechat.constant.StatusConstant;
import com.xxl.wechat.model.generator.BookRoomTask;
import com.xxl.wechat.model.generator.FixAssetTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class BookRoomExcelService extends BaseExcelService {

    private List<BookRoomTask>  bookList;

    public BookRoomExcelService(List<BookRoomTask> bookList) {
        this.bookList = bookList;
    }

    @Override
    public void generateXSSFWorkbook(XSSFWorkbook wb, String path) {


        CreationHelper createHelper = wb.getCreationHelper();

        Sheet s = wb.createSheet();

        String[] header = {"序号","流水号","场地", "预订人","预约日期","预约开始时间", "预订结束时间", "使用部门","使用事由","准备器材","是否拍照","是否摄像","特殊要求","负责人","预订人","预订时间"};
        String[] headerColumn = new String[]{"","ID",  "ROOM_NAME","BOOK_USER_NAME","BOOK_DATE", "SHORT_START_TIME", "SHORT_END_TIME", "DEPART", "USE_REASON", "DEVICE", "NEED_PHOTO", "NEED_CAMERA","SPECIAL_REQUIRE", "RESPONSIBLE_USER", "CREATE_USER_NAME","CREATE_DATE"};

        Row r = s.createRow(0);

        for (int cellnum = 0; cellnum < header.length; cellnum++) {
            //第一行标题先渲染
            Cell c = r.createCell(cellnum);
            c.setCellValue(createHelper.createRichTextString(header[cellnum]));
        }
        //接下来渲染数据行
        for (int rownum = 0; rownum < bookList.size(); rownum++) {
            int actalRownum = rownum + 1;
            Row r1 = s.createRow(actalRownum);
            BookRoomTask bookRoomTask = bookList.get(rownum);
            for (int cellnum = 0; cellnum < header.length; cellnum++) {
                Cell c = r1.createCell(cellnum);
                //其它行是数据
                String value = "";
                if (cellnum == 0) {
                    //第一竖列输出序号
                    value = actalRownum + "";
                } else{
                    //其它列输出数据
                    Object val = bookRoomTask.get(headerColumn[cellnum]);
                    value = (val != null) ? String.valueOf(val) : "";
                    if (headerColumn[cellnum].equals("NEED_PHOTO") && StringUtils.isNotBlank(value)) {
                        value = value.equals("0") ? "否":"是";
                    }  else if(headerColumn[cellnum].equals("NEED_CAMERA") && StringUtils.isNotBlank(value)){
                        //其它列输出数据
                        value = value.equals("0") ? "否":"是";
                    }
                }


                c.setCellValue(createHelper.createRichTextString(value));

            }
        }
    }
}
