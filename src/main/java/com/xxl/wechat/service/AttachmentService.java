package com.xxl.wechat.service;

import com.jfinal.json.FastJson;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.xxl.wechat.model.generator.SyAttachment;
import com.xxl.wechat.util.DateUtil;
import com.xxl.wechat.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class AttachmentService {


    private static Logger log = LoggerFactory.getLogger(AttachmentService.class);

    static SyAttachment syAttachmentDao = new SyAttachment().dao();


    public SyAttachment findAttachmentByPid(String id) {

        return syAttachmentDao.findById(id);
    }

    public List<SyAttachment> findAttachmentByBid(int bid) {

        return syAttachmentDao.find("select * from SY_ATTACHMENT where BUSINESS_ID = " + bid);
    }

    public Map<Integer,String> findAttachmentByBids(String sqlin) {
        Map<Integer,String> map  = new HashMap<>();
        List<SyAttachment> list = syAttachmentDao.find("select * from SY_ATTACHMENT where BUSINESS_ID in ("+sqlin+")");
        for(SyAttachment s : list){
            if(!map.containsKey(s.getBusinessId())){
                map.put(s.getBusinessId(),s.getId());
            }
        }
        return map;
    }

    public String[] findAttachmentIdsByBid(int bid) {

        List<SyAttachment> ss =  this.findAttachmentByBid(bid);

        if(ss != null && ss.size() > 0){
            String[] aa = new String[ss.size()];
            for(int i=0;i<ss.size();i++){
                SyAttachment syAttachment = ss.get(i);
                aa[i] = syAttachment.getId();
            }

            return aa;
        }

        return null;
    }

    public void save(SyAttachment syAttachment) {
        //新增
        syAttachment.setCreateDate(DateUtil.getCurrentDate());
        syAttachment.setCreateUserId(1);//TODO 当前用户ID
        syAttachment.save();

        log.info("保存图片附件表成功：{}",FastJson.getJson().toJson(syAttachment));



    }

    public SyAttachment uploadImg(String img) {

        StringBuffer sb = new StringBuffer();

        //分隔base64
        String[] imgs = StringUtils.split(img,",");

        //取图片后缀
        String imgType = imgs[0].toString().substring(11, imgs[0].toString().length()-7);

        //图片存放路径
        String foderPath = PropKit.get("upload.img");

        String curMonth = DateUtil.getYMStr();

        //以年+月作为一个文件夹存放
        //String monthPath = foderPath + File.separator + curMonth;

        //文件名（不含文件后缀）
        String attachId = UUID.randomUUID().toString().replace("-", "");

        if (!new File(foderPath).exists()) {
            new File(foderPath).mkdirs();
        }

        String filePath = foderPath + File.separator + attachId+"."+imgType;

        //保存
        long size = FileUtil.generateImage(imgs[1], filePath);

        SyAttachment syAttachment = new SyAttachment();
        syAttachment.setId(attachId);
        syAttachment.setUrl(filePath);
        syAttachment.setName(attachId);
        syAttachment.setSuffix(imgType);
        syAttachment.setStatus(1);
        syAttachment.setSize(size);
        syAttachment.setCreateDate(DateUtil.getCurrentDate());
        syAttachment.setType(1);
        syAttachment.setCreateUserId(1);

        syAttachment.save();

        return syAttachment;
    }

    public void delete(String id) {

        SyAttachment syAttachment = syAttachmentDao.findById(id);
        syAttachmentDao.deleteById(id);

        log.info("删除文件成功{}",syAttachment);

    }


    /**
     * 批量更新
     *
     * @param attachmentIds
     * @param pid
     */
    public void batchUpdateAttachment(String[] attachmentIds, int pid) {

        List<SyAttachment> batchList = new ArrayList<SyAttachment>();

        for (String attachmentId : attachmentIds) {
            SyAttachment syAttachment = new SyAttachment();
            syAttachment.setId(attachmentId);
            syAttachment.setBusinessId(pid);
            batchList.add(syAttachment);
        }
        //批量更新
        int[] batchResult = Db.batchUpdate(batchList, 50);

        log.info("批量更新Attachment表中的业务ID：{}",Arrays.toString(attachmentIds));
    }
}
