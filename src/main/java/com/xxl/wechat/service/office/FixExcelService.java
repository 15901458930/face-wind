package com.xxl.wechat.service.office;

import com.xxl.wechat.cache.DictCache;
import com.xxl.wechat.constant.StatusConstant;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.vo.FixVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class FixExcelService extends BaseExcelService {

    private List<FixAssetTask>  fixList;

    public FixExcelService(List<FixAssetTask> fixList) {
        this.fixList = fixList;
    }

    @Override
    public void generateXSSFWorkbook(XSSFWorkbook wb, String path) {


        CreationHelper createHelper = wb.getCreationHelper();

        Sheet s = wb.createSheet();

        String[] header = {"序号","流水号",  "物品分类", "物品子分类", "其它修理物品", "物品位置","所属校区","维修原因","维修状态","申请人","申请时间","修理人","接单时间"};
        String[] headerColumn = new String[]{"","ID",  "ASSET_TYPE", "ASSET_SUB_TYPE", "ASSET_NAME", "ASSET_LOCATION", "BELONG_CAMPUS", "FIX_REASON", "STATUS", "APPLY_USER_NAME","APPLY_DATE", "FIX_USER_NAME", "START_FIX_DATE"};

        Row r = s.createRow(0);

        for (int cellnum = 0; cellnum < header.length; cellnum++) {
            //第一行标题先渲染
            Cell c = r.createCell(cellnum);
            c.setCellValue(createHelper.createRichTextString(header[cellnum]));
        }
        //接下来渲染数据行
        for (int rownum = 0; rownum < fixList.size(); rownum++) {
            int actalRownum = rownum + 1;
            Row r1 = s.createRow(actalRownum);
            FixAssetTask fixAssetTask = fixList.get(rownum);
            for (int cellnum = 0; cellnum < header.length; cellnum++) {
                Cell c = r1.createCell(cellnum);
                //其它行是数据
                String value = "";
                if (cellnum == 0) {
                    //第一竖列输出序号
                    value = actalRownum + "";
                } else{
                    //其它列输出数据
                    Object val = fixAssetTask.get(headerColumn[cellnum]);
                    value = (val != null) ? String.valueOf(val) : "";
                    if (headerColumn[cellnum].equals("STATUS") && StringUtils.isNotBlank(value)) {
                        value = StatusConstant.getStatusMap(Integer.parseInt(value));
                    }  else if(headerColumn[cellnum].equals("ASSET_TYPE") && StringUtils.isNotBlank(value)){
                        //其它列输出数据
                        value = DictCache.mainCategoryMap.get(value);
                    } else if(headerColumn[cellnum].equals("ASSET_SUB_TYPE") && StringUtils.isNotBlank(value)){
                        //其它列输出数据
                        String[] assetSubTypes = StringUtils.split(value,",");
                        StringBuilder sb = new StringBuilder();
                        for(String singleSubType : assetSubTypes){
                            sb.append(DictCache.subCategoryMap.get(singleSubType));
                            sb.append(",");
                        }
                        value = StringUtils.chop(sb.toString());

                    }else if(headerColumn[cellnum].equals("BELONG_CAMPUS") && StringUtils.isNotBlank(value)){
                        //其它列输出数据
                        value = DictCache.belongCampusMap.get(value);
                    }
                }


                c.setCellValue(createHelper.createRichTextString(value));

            }
        }
    }
}
