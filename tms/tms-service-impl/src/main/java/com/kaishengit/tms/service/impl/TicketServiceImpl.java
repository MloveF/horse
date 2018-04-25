package com.kaishengit.tms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.tms.entity.*;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.mapper.*;
import com.kaishengit.tms.service.TicketService;
import com.kaishengit.tms.util.shiro.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private TicketOutRecordMapper ticketOutRecordMapper;

    @Autowired
    private TicketStoreMapper ticketStoreMapper;



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
    public void delTicketInRecordById(Integer id){

        TicketInRecord ticketInRecord = ticketInRecordMapper.selectByPrimaryKey(id);
        if(ticketInRecord != null) {
            //���Ҹü�¼��Ӧ����Ʊ
            List<Ticket> ticketList = ticketMapper.findByBeginNumAndEndNumAndState(ticketInRecord.getBeginTicketNum(),ticketInRecord.getEndTicketNum(),Ticket.TICKET_STATE_IN_STORE);

            //�ж���Ʊ����������¼�������Ƿ���ͬ�������ͬ�����ʾ�е���Ʊ״̬�Ѿ������޸ģ�����ɾ��
            if(!ticketInRecord.getTotalNum().equals(ticketList.size())) {
                throw new ServiceException("This batch status has changed and cannot be deleted");
            }

            int beginNum =  Integer.parseInt(ticketInRecord.getBeginTicketNum());
            int endNum = Integer.parseInt(ticketInRecord.getEndTicketNum());

            for(int i=beginNum; i<=endNum; i++) {
                ticketMapper.deleteByPrimaryKey(i);
            }

            ticketInRecordMapper.deleteByPrimaryKey(id);

        }

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


    /*
     *���ݵ�ǰҳ�Ų�ѯ���е��·���¼
     * @author ��ò�
     * @date 2018/4/23
     */
    @Override
    public PageInfo<TicketOutRecord> findTicketOutRecordByPageNo(Integer pageNo) {
        PageHelper.startPage(pageNo,10);
        TicketOutRecordExample ticketOutRecordExample = new TicketOutRecordExample();
        ticketOutRecordExample.setOrderByClause("id desc");
        List<TicketOutRecord> ticketOutRecordList = ticketOutRecordMapper.selectByExample(ticketOutRecordExample);

        return new PageInfo<>(ticketOutRecordList);

    }

    /*  
     *�����µ���Ʊ�·���¼
     * @date 2018/4/23
     * @param   
     * @return   
     */
    @Override
    public void saveTicketOutRecord(TicketOutRecord ticketOutRecord) throws ServiceException {

        //�жϵ�ǰƱ�����Ƿ��з�[�����]״̬��Ʊ������������·�
        List<Ticket> ticketList = ticketMapper.findByBeginNumAndEndNum(ticketOutRecord.getBeginTicketNum(),ticketOutRecord.getEndTicketNum());

        for(Ticket ticket : ticketList) {
            if(!Ticket.TICKET_STATE_IN_STORE.equals(ticket.getTicketState())) {
                throw new ServiceException("The ticket has already been issued");
            }
        }

        //��ȡ��ǰ�·�����Ʊ����󣬲���ֵ��Ʊ������
        TicketStore ticketStore = ticketStoreMapper.selectByPrimaryKey(ticketOutRecord.getStoreAccountId());
        ticketOutRecord.setStoreAccountName(ticketStore.getStoreName());

        //ѡ���������
        int totalSize = ticketList.size();
        //�ܼ۸�
        BigDecimal totalPrice = ticketOutRecord.getPrice().multiply(new BigDecimal(totalSize));

        //��ǰ��¼�Ķ���
        Account account = shiroUtil.getCurrentAccount();
        ticketOutRecord.setCreateTime(new Date());
        ticketOutRecord.setContent(ticketOutRecord.getBeginTicketNum() + " - " + ticketOutRecord.getEndTicketNum());
        ticketOutRecord.setOutAccountId(account.getId());
        ticketOutRecord.setOutAccountName(account.getAccountName());
        ticketOutRecord.setTotalNum(totalSize);
        ticketOutRecord.setState(TicketOutRecord.STATE_NO_PAY);
        ticketOutRecord.setTotalprice(totalPrice);

        ticketOutRecordMapper.insertSelective(ticketOutRecord);

        logger.info("������Ʊ�·���¼��{}",ticketOutRecord);


    }

    /**
     * ͳ�Ƹ���״̬����Ʊ����
     *
     * @return
     */
    @Override
    public Map<String, Long> countTicketByState() {

        return ticketMapper.countByState();
    }

    /**
     * ɾ������
     *
     * @param id
     */
    @Override
    public void delOutRecordById(Integer id) {
        TicketOutRecord record = ticketOutRecordMapper.selectByPrimaryKey(id);

        if(record != null) {
            //ֻ��δ֧���Ĳ��ܱ�ɾ��
            if(TicketOutRecord.STATE_NO_PAY.equals(record.getState())){
                ticketOutRecordMapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * ���ݵ�ǰҳ�źͲ�ѯ������ѯ�·��б�
     *
     * @param pageNo
     * @param queryParam
     * @return
     */
    @Override
    public PageInfo<TicketOutRecord> findTicketOutRecordByPageNoAndQueryParam(Integer pageNo, Map<String, Object> queryParam) {
        PageHelper.startPage(pageNo,10);

        TicketOutRecordExample ticketOutRecordExample = new TicketOutRecordExample();
        TicketOutRecordExample.Criteria criteria = ticketOutRecordExample.createCriteria();

        String state = (String) queryParam.get("state");

        if(StringUtils.isNotEmpty(state)) {
            criteria.andStateEqualTo(state);
        }

        ticketOutRecordExample.setOrderByClause("id desc");

        List<TicketOutRecord> ticketOutRecordList = ticketOutRecordMapper.selectByExample(ticketOutRecordExample);
        return new PageInfo<>(ticketOutRecordList);
    }

    /**
     * �����������Ҷ�Ӧ���·���
     *
     * @param id
     * @return
     */
    @Override
    public TicketOutRecord findTicketOutRecordById(Integer id) {
        return  ticketOutRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * ����ID�Զ�Ӧ���·���������֧��-�������
     *
     * @param id
     * @param payType
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void payTicketOutRecord(Integer id, String payType) {

        TicketOutRecord ticketOutRecord = ticketOutRecordMapper.selectByPrimaryKey(id);

        if(ticketOutRecord != null && TicketOutRecord.STATE_NO_PAY.equals(ticketOutRecord.getState())){
            ticketOutRecord.setPayType(payType);

            Account account = shiroUtil.getCurrentAccount();

            ticketOutRecord.setFinanceAccountId(account.getId());
            ticketOutRecord.setFinanceAccountName(account.getAccountName());

            ticketOutRecord.setState(TicketOutRecord.STATE_PAY);

            ticketOutRecordMapper.updateByPrimaryKeySelective(ticketOutRecord);

            //����Ӧ����Ʊ״̬�޸�Ϊ�����·���
            List<Ticket> ticketList = ticketMapper.findByBeginNumAndEndNum(ticketOutRecord.getBeginTicketNum(),ticketOutRecord.getEndTicketNum());

            for (Ticket ticket : ticketList) {
                ticket.setTicketState(Ticket.TICKET_STATE_OUT_STORE);
                ticket.setStoreAccountId(ticketOutRecord.getStoreAccountId());
                ticket.setTicketOutTime(DateTime.now().toString("YYYY-MM-dd"));
                ticket.setUpdateTime(new Date());

                ticketMapper.updateByPrimaryKeySelective(ticket);
            }
        }

    }
}
