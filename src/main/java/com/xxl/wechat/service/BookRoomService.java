package com.xxl.wechat.service;

import com.xxl.wechat.form.BookRoomForm;
import com.xxl.wechat.model.generator.BookRoomTask;
import com.xxl.wechat.model.generator.FixAssetTask;
import com.xxl.wechat.util.DateUtil;
import com.xxl.wechat.vo.BookRoomVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BookRoomService {

    private static Logger log = LoggerFactory.getLogger(FixAssetsService.class);

    static BookRoomTask bookRoomTaskDao = new BookRoomTask().dao();


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
                vo.setBookStartDate(DateUtil.format(task.getBookStartTime(),DateUtil.HH_MM_PATTERN));
                vo.setBookEndDate(DateUtil.format(task.getBookEndTime(),DateUtil.HH_MM_PATTERN));
                vo.setRoomName(task.get("ROOM_NAME"));
                vo.setUserName(task.get("REAL_NAME"));
                voList.add(vo);
            }
        }

        return voList;


    }


    public List<BookRoomVO> findRoomByDateAndRoom(String roomId,String bookDate,String userId){

        String sql = "select U.REAL_NAME,B.CREATE_DATE,B.ID,B.BOOK_START_TIME,B.BOOK_END_TIME,S.NAME AS ROOM_NAME,B.BOOK_USER_ID from BOOK_ROOM_TASK B LEFT JOIN SY_ROOM S ON B.ROOM_ID = S.ID LEFT JOIN SY_USER U ON B.BOOK_USER_ID = U.ID where ROOM_ID = ? and BOOK_DATE = ? order by B.BOOK_START_TIME ASC" ;
        List<BookRoomTask>  list = bookRoomTaskDao.find(sql, roomId, bookDate);

        List<BookRoomVO> voList = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(BookRoomTask task : list){
                BookRoomVO vo = new BookRoomVO();
                vo.setId(task.getId());
                vo.setBookDate(DateUtil.format(task.getCreateDate(),DateUtil.DEFAULT_PATTERN));
                vo.setBookStartDate(DateUtil.format(task.getBookStartTime(),DateUtil.HH_MM_PATTERN));
                vo.setBookEndDate(DateUtil.format(task.getBookEndTime(),DateUtil.HH_MM_PATTERN));
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


        return list;
    }

    public static void main(String[] args) {



    }
}
