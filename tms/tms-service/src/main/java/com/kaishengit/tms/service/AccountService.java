package com.kaishengit.tms.service;

import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.exception.ServiceException;

/*   
 *ϵͳ�˺ŵ�ҵ����
 * @author ��ò�  
 * @date 2018/4/12   
 */
public interface AccountService {


    Account login(String accountMobile, String password, String requestIp)throws ServiceException;
}
