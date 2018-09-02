package com.xxl.wechat.service;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.xxl.wechat.cache.DictCache;
import com.xxl.wechat.constant.StatusConstant;
import com.xxl.wechat.form.BookRoomForm;
import com.xxl.wechat.model.generator.BookRoomTask;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.util.DateUtil;
import com.xxl.wechat.vo.BookRoomVO;
import com.xxl.wechat.vo.LayuiResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BookRoomService {

    private static Logger log = LoggerFactory.getLogger(FixAssetsService.class);

    static BookRoomTask bookRoomTaskDao = new BookRoomTask().dao();

    static WeChatPushService weChatPushService = new WeChatPushService();

    static UserService userService = new UserService();

    public List<BookRoomVO> findRoomByRangeTime(String startTime,String endTime,Integer roomId,Integer id){

        //如果是新增，则全表查
        String sql = "";
        if(id  == null){
            sql = "select U.REAL_NAME,B.CREATE_DATE,B.ID,B.BOOK_START_TIME,B.BOOK_END_TIME,S.NAME AS ROOM_NAME from BOOK_ROOM_TASK B LEFT JOIN SY_ROOM S ON B.ROOM_ID = S.ID LEFT JOIN SY_USER U ON B.BOOK_USER_ID = U.ID WHERE B.BOOK_END_TIME >= ? AND B.BOOK_START_TIME < ? AND B.ROOM_ID = ?  " ;

        }else{

            //如果是修改，则排除自己查
            sql = "select U.REAL_NAME,B.CREATE_DATE,B.ID,B.BOOK_START_TIME,B.BOOK_END_TIME,S.NAME AS ROOM_NAME from BOOK_ROOM_TASK B LEFT JOIN SY_ROOM S ON B.ROOM_ID = S.ID LEFT JOIN SY_USER U ON B.BOOK_USER_ID = U.ID WHERE B.BOOK_END_TIME >= ? AND B.BOOK_START_TIME < ?   AND B.ROOM_ID = ?  AND  B.ID <>"+ id ;
        }
        List<BookRoomTask>  list = bookRoomTaskDao.find(sql, startTime, endTime,roomId);

        List<BookRoomVO> voList = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(BookRoomTask task : list){
                BookRoomVO vo = new BookRoomVO();
                vo.setId(task.getId());
                vo.setBookDate(DateUtil.format(task.getCreateDate(),DateUtil.DEFAULT_PATTERN));
                vo.setBookStartTime(DateUtil.format(task.getBookStartTime(),DateUtil.HH_MM_PATTERN));
                vo.setBookEndTime(DateUtil.format(task.getBookEndTime(),DateUtil.HH_MM_PATTERN));
                vo.setRoomName(task.get("ROOM_NAME"));
                vo.setUserName(task.get("REAL_NAME"));
                voList.add(vo);
            }
        }
        return voList;
    }


    /**
     * layui后台用
     * @param page
     * @param pageSize
     * @return
     */
    public LayuiResultVO<BookRoomTask> findAllBookRooms(int page, int pageSize, String status, String startDate, String endDate){

        Page<BookRoomTask> paginate = list(page,pageSize,status,startDate,endDate);

        LayuiResultVO<BookRoomTask> vo = LayuiResultVO.getInstance().assemblySuccess(paginate.getTotalRow(),paginate.getList());

        return vo;
    }

    /**
     * layui后台用
     * @param page
     * @param pageSize
     * @return
     */
    public Page<BookRoomTask> list(int page, int pageSize, String roomId, String startDate, String endDate) {

        String sqlExceptSelect = "from BOOK_ROOM_TASK B LEFT JOIN SY_USER U ON B.BOOK_USER_ID = U.ID LEFT JOIN SY_USER U2 ON B.CREATE_USER_ID = U2.ID LEFT JOIN SY_ROOM R ON B.ROOM_ID = R.ID  WHERE 1=1 ";

        if (StringUtils.isNotBlank(roomId)) {
            sqlExceptSelect += "  and  B.ROOM_ID = " + roomId;
        }
        if (StringUtils.isNotBlank(startDate)) {
            sqlExceptSelect += "  and  B.BOOK_START_TIME >= '" + startDate + "'";
        }
        if (StringUtils.isNotBlank(endDate)) {
            endDate += " 23:59:59";
            sqlExceptSelect += "  and  B.BOOK_START_TIME <= '" + endDate + "'";
        }

        sqlExceptSelect += " order by B.ID desc ";
        Page<BookRoomTask> paginate = bookRoomTaskDao.paginate(page, pageSize, "select B.*,U.REAL_NAME AS BOOK_USER_NAME,U2.REAL_NAME AS CREATE_USER_NAME,R.NAME AS ROOM_NAME ", sqlExceptSelect);

        for(BookRoomTask task : paginate.getList()){
            task.put("SHORT_START_TIME",DateUtil.format(task.getBookStartTime(),DateUtil.HH_MM_PATTERN));
            task.put("SHORT_END_TIME",DateUtil.format(task.getBookEndTime(),DateUtil.HH_MM_PATTERN));
        }

        return paginate ;
    }


    
    public BookRoomVO getVO(int id){

        String sql = "select U.REAL_NAME,B.*,S.NAME AS ROOM_NAME,U2.REAL_NAME AS CREATE_USER_NAME from BOOK_ROOM_TASK B LEFT JOIN SY_ROOM S ON B.ROOM_ID = S.ID LEFT JOIN SY_USER U ON B.BOOK_USER_ID = U.ID LEFT JOIN SY_USER U2 ON B.CREATE_USER_ID = U2.ID  WHERE B.ID  = ?" ;

        BookRoomTask task = bookRoomTaskDao.findFirst(sql,id);

        BookRoomVO vo = new BookRoomVO();
        vo.setId(task.getId());
        vo.setBookDate(DateUtil.format(task.getCreateDate(),DateUtil.DEFAULT_PATTERN));
        vo.setBookStartTime(DateUtil.format(task.getBookStartTime(),DateUtil.HH_MM_PATTERN));
        vo.setBookEndTime(DateUtil.format(task.getBookEndTime(),DateUtil.HH_MM_PATTERN));

        vo.setDepart(task.getDepart());
        vo.setResponsibleUser(task.getResponsibleUser());
        vo.setUseReason(task.getUseReason());
        vo.setDevice(task.getDevice());
        vo.setNeedPhoto(task.getNeedPhoto());
        vo.setNeedCamera(task.getNeedCamera());
        vo.setSpecialRequire(task.getSpecialRequire());

        vo.setBookStartDate(task.getBookStartTime());
        vo.setBookEndDate(task.getBookEndTime());
        vo.setCreateDate(DateUtil.format(task.getCreateDate(),DateUtil.DEFAULT_PATTERN));
        vo.setCreateUserName(task.get("CREATE_USER_NAME"));
        vo.setRoomName(task.get("ROOM_NAME"));
        vo.setUserName(task.get("REAL_NAME"));

        return vo;
    }

    /**
     * 默认进来显示当天前一天以后的所有预订记录
     * @param roomId
     * @param bookDate
     * @param userId
     * @return
     */
    public List<BookRoomVO> findRoomByDateAndRoom(String roomId,String bookDate,int userId){

        StringBuilder sql = new StringBuilder("select U.REAL_NAME,B.*,S.NAME AS ROOM_NAME,B.BOOK_USER_ID from BOOK_ROOM_TASK B LEFT JOIN SY_ROOM S ON B.ROOM_ID = S.ID LEFT JOIN SY_USER U ON B.BOOK_USER_ID = U.ID where 1=1 " );
        List<BookRoomTask>  list = new ArrayList<BookRoomTask>();
        if(StringUtils.isNotBlank(bookDate)){
            //指定查询日期的话，则不用时间限制
            sql.append(" and  BOOK_DATE = '").append(bookDate).append("'");
            if(StringUtils.isNotBlank(roomId)){
                sql.append(" and  ROOM_ID = ").append(roomId);
            }
        }else{
            String preDayZeroStr = DateUtil.getPreDayZeroStr();
            //没指定查询日期的话只查询预订时间是最近3天
            sql.append(" and BOOK_START_TIME > '").append(preDayZeroStr).append("'");
        }
        sql.append(" order by B.BOOK_START_TIME ASC");
        list  = bookRoomTaskDao.find(sql.toString());

        List<BookRoomVO> voList = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(BookRoomTask task : list){
                BookRoomVO vo = new BookRoomVO();
                vo.setId(task.getId());
                vo.setUseReason(task.getUseReason());
                vo.setResponsibleUser(task.getResponsibleUser());
                vo.setDepart(task.getDepart());
                vo.setCreateDate(DateUtil.format(task.getCreateDate(),DateUtil.DEFAULT_PATTERN));
                vo.setBookDate(DateUtil.format(task.getBookStartTime(),DateUtil.YMD_PATTERN));
                vo.setBookStartTime(DateUtil.format(task.getBookStartTime(),DateUtil.HH_MM_PATTERN));
                vo.setBookEndTime(DateUtil.format(task.getBookEndTime(),DateUtil.HH_MM_PATTERN));
                vo.setRoomName(task.get("ROOM_NAME"));
                vo.setUserName(task.get("REAL_NAME"));
                if(task.getBookUserId().equals(userId)){
                    //只能删除自己的
                    vo.setHasPower(true);
                }
                voList.add(vo);
            }
        }

        return voList;

    }

    public void delete(int id){
        bookRoomTaskDao.deleteById(id);
    }

    public BookRoomTask get(int id){
        return bookRoomTaskDao.findById(id);
    }

    public List<BookRoomVO> save(BookRoomForm form){

        List<BookRoomVO> list = new ArrayList<>();

        final List<BookRoomVO> roomByRangeTime = findRoomByRangeTime(form.getStartTime(), form.getEndTime(),form.getRoomId(),form.getId());

        if(roomByRangeTime != null && roomByRangeTime.size() > 0){

            //表示无法预订
            return roomByRangeTime;
        }

        BookRoomTask task = new BookRoomTask();

        task.setRoomId(form.getRoomId());
        task.setBookDate(form.getStartTime().substring(0,10).replaceAll("-",""));
        task.setBookStartTime(DateUtil.parseDate(form.getStartTime(),DateUtil.NO_SECONDS_PATTERN));
        task.setBookEndTime(DateUtil.parseDate(form.getEndTime(),DateUtil.NO_SECONDS_PATTERN));

        task.setDepart(form.getDepart());
        task.setResponsibleUser(form.getResponsibleUser());
        task.setUseReason(form.getUseReason());
        task.setDevice(form.getDevice());
        task.setNeedPhoto(form.getNeedPhoto());
        task.setNeedCamera(form.getNeedCamera());
        task.setSpecialRequire(form.getSpecialRequire());

        if(form.getId() == null){
            task.setBookUserId(form.getCuUserId());
            task.setVersion(1);
            task.setCreateDate(DateUtil.getCurrentDate());
            task.setCreateUserId(form.getCuUserId());
            task.save();
        }else{
            task.setId(form.getId());
            task.update();
        }

        String start = DateUtil.format(task.getBookStartTime(), DateUtil.HH_MM_PATTERN);
        String end = DateUtil.format(task.getBookEndTime(),DateUtil.HH_MM_PATTERN);

        String msg = "有新的预约场地申请！ (流水号:"+task.getId()+"，场地："+DictCache.roomMap.get(task.getRoomId())+"，预约时间："+task.getBookDate()+"日 "+start+"分至"+end+"分，使用部门："+ task.getDepart()+"，需要准备器材："+task.getDevice()+"     （更多详细信息，请点击下方的【报修预约】进入应用内查看！）";
        weChatPushService.save(userService.findFixUser("3,4,5"),msg);

        return list;
    }

    public static void main(String[] args) {



    }
}
