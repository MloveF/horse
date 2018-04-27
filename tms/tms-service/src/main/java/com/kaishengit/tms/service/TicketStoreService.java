package com.kaishengit.tms.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.tms.entity.StoreAccount;
import com.kaishengit.tms.entity.StoreLoginLog;
import com.kaishengit.tms.entity.TicketStore;

import java.util.List;
import java.util.Map;


/**
 * ��Ʊ��Ʊ��ҵ����
 * @author fankay
 */
public interface TicketStoreService {

    /*
     *�����µ���Ʊ��
     * @author ��ò�
     * @date 2018/4/19
     */
    void saveNewTicktStore(TicketStore ticketStore);

    /*
     *���ݵ�ǰҳ��Ͳ�ѯ������ѯ���۵�
     * @author ��ò�
     * @date 2018/4/20
     */
    PageInfo<TicketStore> findAllTicketStoreByPage(Integer pageNo, Map<String, Object> queryParam);

    /*
     *����ID���Ҷ�Ӧ����Ʊ��
     * @author ��ò�
     * @date 2018/4/20
     */
    TicketStore findTicketStoreById(Integer id);

    /*
     *������Ʊ���StoreAccountId����������Ʊ���˺Ŷ���
     * @author ��ò�
     * @date 2018/4/20
     */
    StoreAccount findStoreAccountById(Integer id);

    /*   
     *�޸���Ʊ��
     * @author ��ò�  
     * @date 2018/4/20   
     */
    void updateTicketStore(TicketStore ticketStore);

    /*
     *��StroeAccount��״̬�޸�Ϊ���ã�disable��
     * @author ��ò�
     * @date 2018/4/22
     */
    void editStroeAccountState(Integer id);

    /*
     *����TicketStore��ID���Ҷ�Ӧ��StoreAccount����
     * @date 2018/4/23
     * @param
     * @return
     */
    StoreAccount findStoreAccountByTicketStoreId(Integer id);

    /*
     *��StroeAccount��״̬�޸�Ϊ������normal��
     * @author ��ò�
     * @date 2018/4/23
     */
    void normalAccountState(Integer id);


    /*
     *������Ʊ���IDɾ����Ʊ�����Ʊ�˻�
     * @author ��ò�
     * @date 2018/4/23
     */
    void delTicketStoreWithAccountById(Integer id);

    /**
     * ��ѯ���е���Ʊ��
     * @return
     */
    List<TicketStore> findAllTicketStore();

    /**
     * �����˺ţ��ֻ����룩������Ʊ���¼�˺Ŷ���
     * @param
     * @return
     */
    StoreAccount findStoreAccountByName(String name);


    /**
     * ������Ʊ��ĵ�¼��־
     * @param
     */
    void saveStoreAccountLoginLog(StoreLoginLog storeLoginLog);
}
