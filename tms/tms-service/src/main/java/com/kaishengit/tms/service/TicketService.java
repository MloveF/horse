package com.kaishengit.tms.service;


import com.kaishengit.tms.entity.Ticket;
import com.kaishengit.tms.entity.TicketInRecord;

import java.util.List;

public interface TicketService {

    /*   
     *年票业务类
     * @author 马得草  
     * @date 2018/4/20
     */
    void saveTicketInRecord(TicketInRecord ticketInRecord);


    /*
     *根据accountId查询入库记录
     * @author 马得草
     * @date 2018/4/21
     */
    List<TicketInRecord> findAllTicketInRecordByAccountId(Integer id);


    /*
     *根据id删除TicketInRecord对象和Ticket对象
     * @author 马得草
     * @date 2018/4/21
     */
    void delTicketInRecordById(Integer id);

    /*
     *查询所有的年票
     * @author 马得草
     * @date 2018/4/21
     */
    List<Ticket> findAllTicket();

}
