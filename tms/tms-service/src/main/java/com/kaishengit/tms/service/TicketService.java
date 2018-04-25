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

    /*
     *���ݵ�ǰҳ�Ų�ѯ���е��·���¼
     * @author ��ò�
     * @date 2018/4/23
     */
    PageInfo<TicketOutRecord> findTicketOutRecordByPageNo(Integer pageNo);

    
    /*  
     *�����µ���Ʊ�·���¼
     * @date 2018/4/23
     * @param   
     * @return   
     */
    void saveTicketOutRecord(TicketOutRecord ticketOutRecord)throws ServiceException;

    /**
     * ͳ�Ƹ���״̬����Ʊ����
     * @return
     */
    Map<String,Long> countTicketByState();


    /**
     * ɾ������
     * @param id
     */
    void delOutRecordById(Integer id);

    /**
     * ���ݵ�ǰҳ�źͲ�ѯ������ѯ�·��б�
     * @param pageNo
     * @param queryParam
     * @return
     */
    PageInfo<TicketOutRecord> findTicketOutRecordByPageNoAndQueryParam(Integer pageNo, Map<String, Object> queryParam);


    /**
     * �����������Ҷ�Ӧ���·���
     * @param id
     * @return
     */
    TicketOutRecord findTicketOutRecordById(Integer id);


    /**
     * ����ID�Զ�Ӧ���·���������֧��-�������
     * @param id
     * @param payType
     */
    void payTicketOutRecord(Integer id, String payType);
}
