package com.kaishengit.tms.service;

import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.exception.ServiceException;

/*   
 *系统账号的业务类
 * @author 马得草  
 * @date 2018/4/12   
 */
public interface AccountService {


    Account login(String accountMobile, String password, String requestIp)throws ServiceException;
}
