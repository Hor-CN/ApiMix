package cn.apimix.service;

import cn.apimix.model.entity.Account;
import com.mybatisflex.core.service.IService;

/**
 * @Author: Hor
 * @Date: 2024/6/24 上午10:51
 * @Version: 1.0
 */
public interface IAccountService extends IService<Account> {


    /**
     * 获取账户余额
     */
    Account getAmount(Long userId);

    Boolean feDeduction(Long userId, Long amount);

}
