package com.kaishengit.tms.service;


import com.github.pagehelper.PageInfo;
import com.kaishengit.tms.entity.Ticket;
import com.kaishengit.tms.entity.TicketInRecord;
import com.kaishengit.tms.entity.TicketOutRecord;
import com.kaishengit.tms.exception.ServiceException;

import java.util.List;
import java.util.Map;

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

    /*
     *根据当前页号查询所有的下发记录
     * @author 马得草
     * @date 2018/4/23
     */
    PageInfo<TicketOutRecord> findTicketOutRecordByPageNo(Integer pageNo);

    
    /*  
     *保存新的年票下发记录
     * @date 2018/4/23
     * @param   
     * @return   
     */
    void saveTicketOutRecord(TicketOutRecord ticketOutRecord)throws ServiceException;

    /**
     * 统计各个状态的年票数量
     * @return
     */
    Map<String,Long> countTicketByState();


    /**
     * 删除订单
     * @param id
     */
    void delOutRecordById(Integer id);

    /**
     * 根据当前页号和查询参数查询下发列表
     * @param pageNo
     * @param queryParam
     * @return
     */
    PageInfo<TicketOutRecord> findTicketOutRecordByPageNoAndQueryParam(Integer pageNo, Map<String, Object> queryParam);


    /**
     * 根据主键查找对应的下发单
     * @param id
     * @return
     */
    TicketOutRecord findTicketOutRecordById(Integer id);


    /**
     * 根据ID对对应的下发订单进行支付-财务结算
     * @param id
     * @param payType
     */
    void payTicketOutRecord(Integer id, String payType);
}
