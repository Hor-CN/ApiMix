package cn.apimix.user.service;

import cn.apimix.model.entity.UserAccount;
import com.mybatisflex.core.service.IService;

/**
 * @Author: Hor
 * @Date: 2024/6/24 上午10:51
 * @Version: 1.0
 */
public interface UserAccountService extends IService<UserAccount> {


    /**
     * 获取账户余额
     */
    UserAccount getAmount(Long userId);


    /**
     * 消费金额
     */
    Boolean deductionAmount(Long userId, Long amount);


    /**
     * 新增余额
     *
     * @param userId 用户ID
     * @param amount 金额
     */
    Boolean increaseAmount(Long userId, Long amount);

}
