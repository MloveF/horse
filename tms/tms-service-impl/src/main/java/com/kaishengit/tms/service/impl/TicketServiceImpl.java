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
 *年票业务类
 * @author 马得草
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
     *保存一个入库记录
     * @author 马得草  
     * @date 2018/4/20   
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveTicketInRecord(TicketInRecord ticketInRecord) {

        //设置入库时间
        ticketInRecord.setCreateTime(new Date());
        //获取总数量=截至票号-起始票号+1
        BigInteger start = new BigInteger(ticketInRecord.getBeginTicketNum());
        BigInteger end = new BigInteger(ticketInRecord.getEndTicketNum());

        BigInteger totalNum = end.subtract(start).add(new BigInteger(String.valueOf(1)));
        ticketInRecord.setTotalNum(totalNum.intValue());

        //获取当前登录的对象
        Account account = shiroUtil.getCurrentAccount();
        ticketInRecord.setAccountId(account.getId());
        ticketInRecord.setAccountName(account.getAccountName());

        //设置入库的内容
        ticketInRecord.setContent(ticketInRecord.getBeginTicketNum()+"-"+ticketInRecord.getEndTicketNum());

        ticketInRecordMapper.insertSelective(ticketInRecord);

        logger.info("新增年票入库： {} 入库人：{}",ticketInRecord,account);

        //添加totalNum条年票记录
        List<Ticket> ticketList = new ArrayList<>();
        for(int i = 0;i < totalNum.intValue();i++) {
            Ticket  ticket = new Ticket();
            ticket.setCreateTime(new Date());
            ticket.setTicketInTime(new Date());
            ticket.setTicketNum(start.add(new BigInteger(String.valueOf(i))).toString());
            ticket.setTicketState(Ticket.TICKET_STATE_IN_STORE);
            ticketList.add(ticket);
        }

        //批量保存年票记录
        ticketMapper.batchInsert(ticketList);
        System.out.println("你好");
    }


    /*
     *根据accountId查询入库记录
     * @author 马得草
     * @date 2018/4/21
     */
    @Override
    public List<TicketInRecord> findAllTicketInRecordByAccountId(Integer id) {

        return ticketMapper.findAllTicketInRecordByAccountId(id);

    }


    /*
     *根据id删除TicketInRecord对象和Ticket对象
     * @author 马得草
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
     *查询所有的年票
     * @author 马得草  
     * @date 2018/4/21   
     */
    @Override
    public List<Ticket> findAllTicket() {
        return ticketMapper.selectAllTicket();

    }
}
