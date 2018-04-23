package com.kaishengit.tms.service;


import com.kaishengit.tms.entity.Ticket;
import com.kaishengit.tms.entity.TicketInRecord;

import java.util.List;

public interface TicketService {

    /*   
     *��Ʊҵ����
     * @author ��ò�  
     * @date 2018/4/20
     */
    void saveTicketInRecord(TicketInRecord ticketInRecord);


    /*
     *����accountId��ѯ����¼
     * @author ��ò�
     * @date 2018/4/21
     */
    List<TicketInRecord> findAllTicketInRecordByAccountId(Integer id);


    /*
     *����idɾ��TicketInRecord�����Ticket����
     * @author ��ò�
     * @date 2018/4/21
     */
    void delTicketInRecordById(Integer id);

    /*
     *��ѯ���е���Ʊ
     * @author ��ò�
     * @date 2018/4/21
     */
    List<Ticket> findAllTicket();

}
