package cn.apimix.user.service.impl;

import cn.apimix.user.mapper.AccountMapper;
import cn.apimix.model.entity.UserAccount;
import cn.apimix.model.entity.table.UserAccountTableDef;
import cn.apimix.user.service.UserAccountService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author: Hor
 * @Date: 2024/6/24 下午12:26
 * @Version: 1.0
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<AccountMapper, UserAccount> implements UserAccountService {



    /**
     * 获取账户余额
     *
     * @param userId 用户ID
     */
    @Override
    public UserAccount getAmount(Long userId) {
        return getOne(query().where(
                UserAccountTableDef.USER_ACCOUNT.USER_ID.eq(userId)
        ));
    }

    /**
     * 扣除金额
     * @param userId 用户ID
     * @param amount 金额
     */
    @Override
    public Boolean deductionAmount(Long userId, Long amount) {
        UserAccount userAccount = getOne(query().where(
                UserAccountTableDef.USER_ACCOUNT.USER_ID.eq(userId)
        ));
        Long oldAmount = userAccount.getAmount();

        userAccount.setAmount(oldAmount - amount);
        // 行锁+状态机: 状态机字段 amount=[修改前的余额]
        return update(userAccount, query().where(
                UserAccountTableDef.USER_ACCOUNT.AMOUNT.eq(oldAmount))
                .and(UserAccountTableDef.USER_ACCOUNT.ID.eq(userAccount.getId()))
                .and(UserAccountTableDef.USER_ACCOUNT.USER_ID.eq(userId))
        );
    }

    /**
     * 新增余额
     *
     * @param userId 用户ID
     * @param amount 金额
     */
    @Override
    public Boolean increaseAmount(Long userId, Long amount) {
        UserAccount userAccount = getOne(query().where(
                UserAccountTableDef.USER_ACCOUNT.USER_ID.eq(userId)
        ));
        Long oldAmount = userAccount.getAmount();
        userAccount.setAmount(oldAmount + amount);
        userAccount.setAmountTotal(userAccount.getAmountTotal() + amount);
        // 行锁+状态机: 状态机字段 amount=[修改前的余额]
        return update(userAccount, query()
                .where(UserAccountTableDef.USER_ACCOUNT.AMOUNT.eq(oldAmount))
                .and(UserAccountTableDef.USER_ACCOUNT.ID.eq(userAccount.getId()))
                .and(UserAccountTableDef.USER_ACCOUNT.USER_ID.eq(userId))
        );
    }


}
