package com.kaishengit.tms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.tms.entity.*;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.mapper.*;
import com.kaishengit.tms.service.TicketService;
import com.kaishengit.tms.util.shiro.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private TicketOutRecordMapper ticketOutRecordMapper;

    @Autowired
    private TicketStoreMapper ticketStoreMapper;



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

        logger.info("新增年票入库： {} 入库人：{}",ticketInRecord);

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
    public void delTicketInRecordById(Integer id){

        TicketInRecord ticketInRecord = ticketInRecordMapper.selectByPrimaryKey(id);
        if(ticketInRecord != null) {
            //查找该记录对应的年票
            List<Ticket> ticketList = ticketMapper.findByBeginNumAndEndNumAndState(ticketInRecord.getBeginTicketNum(),ticketInRecord.getEndTicketNum(),Ticket.TICKET_STATE_IN_STORE);

            //判断年票数量和入库记录总数量是否相同，如果不同，则表示有的年票状态已经发生修改，不能删除
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
     *查询所有的年票
     * @author 马得草  
     * @date 2018/4/21   
     */
    @Override
    public List<Ticket> findAllTicket() {
        return ticketMapper.selectAllTicket();

    }


    /*
     *根据当前页号查询所有的下发记录
     * @author 马得草
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
     *保存新的年票下发记录
     * @date 2018/4/23
     * @param   
     * @return   
     */
    @Override
    public void saveTicketOutRecord(TicketOutRecord ticketOutRecord) throws ServiceException {

        //判断当前票段内是否有非[已入库]状态的票，如果有则不能下发
        List<Ticket> ticketList = ticketMapper.findByBeginNumAndEndNum(ticketOutRecord.getBeginTicketNum(),ticketOutRecord.getEndTicketNum());

        for(Ticket ticket : ticketList) {
            if(!Ticket.TICKET_STATE_IN_STORE.equals(ticket.getTicketState())) {
                throw new ServiceException("The ticket has already been issued");
            }
        }

        //获取当前下发的售票点对象，并赋值售票点名称
        TicketStore ticketStore = ticketStoreMapper.selectByPrimaryKey(ticketOutRecord.getStoreAccountId());
        ticketOutRecord.setStoreAccountName(ticketStore.getStoreName());

        //选择的总数量
        int totalSize = ticketList.size();
        //总价格
        BigDecimal totalPrice = ticketOutRecord.getPrice().multiply(new BigDecimal(totalSize));

        //当前登录的对象
        Account account = shiroUtil.getCurrentAccount();
        ticketOutRecord.setCreateTime(new Date());
        ticketOutRecord.setContent(ticketOutRecord.getBeginTicketNum() + " - " + ticketOutRecord.getEndTicketNum());
        ticketOutRecord.setOutAccountId(account.getId());
        ticketOutRecord.setOutAccountName(account.getAccountName());
        ticketOutRecord.setTotalNum(totalSize);
        ticketOutRecord.setState(TicketOutRecord.STATE_NO_PAY);
        ticketOutRecord.setTotalprice(totalPrice);

        ticketOutRecordMapper.insertSelective(ticketOutRecord);

        logger.info("新增年票下发记录：{}",ticketOutRecord);


    }

    /**
     * 统计各个状态的年票数量
     *
     * @return
     */
    @Override
    public Map<String, Long> countTicketByState() {

        return ticketMapper.countByState();
    }

    /**
     * 删除订单
     *
     * @param id
     */
    @Override
    public void delOutRecordById(Integer id) {
        TicketOutRecord record = ticketOutRecordMapper.selectByPrimaryKey(id);

        if(record != null) {
            //只有未支付的才能被删除
            if(TicketOutRecord.STATE_NO_PAY.equals(record.getState())){
                ticketOutRecordMapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 根据当前页号和查询参数查询下发列表
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
     * 根据主键查找对应的下发单
     *
     * @param id
     * @return
     */
    @Override
    public TicketOutRecord findTicketOutRecordById(Integer id) {
        return  ticketOutRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据ID对对应的下发订单进行支付-财务结算
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

            //将对应的年票状态修改为【已下发】
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


    /*
     *根据售票点ID查询年票数量
     * @date 2018/4/27
     * @param
     * @return
     */
    @Override
    public Map<String, Long> countTicketByStateAndStoreAccountId(Integer id) {
        return ticketMapper.countByStateAndStoreAccountId(id);
    }
}
