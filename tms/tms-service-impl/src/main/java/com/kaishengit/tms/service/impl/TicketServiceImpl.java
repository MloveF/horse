package com.kaishengit.tms.service.impl;

import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.entity.Ticket;
import com.kaishengit.tms.entity.TicketExample;
import com.kaishengit.tms.entity.TicketInRecord;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.mapper.AccountMapper;
import com.kaishengit.tms.mapper.TicketInRecordMapper;
import com.kaishengit.tms.mapper.TicketMapper;
import com.kaishengit.tms.service.TicketService;
import com.kaishengit.tms.util.shiro.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 *��Ʊҵ����
 * @author ��ò�
 * @date 2018/4/20
 */
@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private ShiroUtil shiroUtil;

    @Autowired
    private TicketInRecordMapper ticketInRecordMapper;
    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private AccountMapper accountMapper;



    /*   
     *����һ������¼
     * @author ��ò�  
     * @date 2018/4/20   
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveTicketInRecord(TicketInRecord ticketInRecord) {

        //�������ʱ��
        ticketInRecord.setCreateTime(new Date());
        //��ȡ������=����Ʊ��-��ʼƱ��+1
        BigInteger start = new BigInteger(ticketInRecord.getBeginTicketNum());
        BigInteger end = new BigInteger(ticketInRecord.getEndTicketNum());

        BigInteger totalNum = end.subtract(start).add(new BigInteger(String.valueOf(1)));
        ticketInRecord.setTotalNum(totalNum.intValue());

        //��ȡ��ǰ��¼�Ķ���
        Account account = shiroUtil.getCurrentAccount();
        ticketInRecord.setAccountId(account.getId());
        ticketInRecord.setAccountName(account.getAccountName());

        //������������
        ticketInRecord.setContent(ticketInRecord.getBeginTicketNum()+"-"+ticketInRecord.getEndTicketNum());

        ticketInRecordMapper.insertSelective(ticketInRecord);

        logger.info("������Ʊ��⣺ {} ����ˣ�{}",ticketInRecord,account);

        //���totalNum����Ʊ��¼
        List<Ticket> ticketList = new ArrayList<>();
        for(int i = 0;i < totalNum.intValue();i++) {
            Ticket  ticket = new Ticket();
            ticket.setCreateTime(new Date());
            ticket.setTicketInTime(new Date());
            ticket.setTicketNum(start.add(new BigInteger(String.valueOf(i))).toString());
            ticket.setTicketState(Ticket.TICKET_STATE_IN_STORE);
            ticketList.add(ticket);
        }

        //����������Ʊ��¼
        ticketMapper.batchInsert(ticketList);
        System.out.println("���");
    }


    /*
     *����accountId��ѯ����¼
     * @author ��ò�
     * @date 2018/4/21
     */
    @Override
    public List<TicketInRecord> findAllTicketInRecordByAccountId(Integer id) {

        return ticketMapper.findAllTicketInRecordByAccountId(id);

    }


    /*
     *����idɾ��TicketInRecord�����Ticket����
     * @author ��ò�
     * @date 2018/4/21
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delTicketInRecordById(Integer id) throws ServiceException {

        TicketInRecord ticketInRecord = ticketInRecordMapper.selectByPrimaryKey(id);
        int beginNum =  Integer.parseInt(ticketInRecord.getBeginTicketNum());
        int endNum = Integer.parseInt(ticketInRecord.getEndTicketNum());

        for(int i=beginNum; i<=endNum; i++) {
            ticketMapper.deleteByPrimaryKey(i);
        }

        ticketInRecordMapper.deleteByPrimaryKey(id);

    }

    
    /*   
     *��ѯ���е���Ʊ
     * @author ��ò�  
     * @date 2018/4/21   
     */
    @Override
    public List<Ticket> findAllTicket() {
        return ticketMapper.selectAllTicket();

    }
}
