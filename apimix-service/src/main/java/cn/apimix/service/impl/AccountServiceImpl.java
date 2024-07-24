package cn.apimix.service.impl;

import cn.apimix.mapper.AccountMapper;
import cn.apimix.model.entity.Account;
import cn.apimix.model.entity.table.AccountTableDef;
import cn.apimix.service.IAccountService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author: Hor
 * @Date: 2024/6/24 下午12:26
 * @Version: 1.0
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {


    /**
     * 获取账户余额
     *
     * @param userId 用户ID
     */
    @Override
    public Account getAmount(Long userId) {
        return getOne(query().where(
                AccountTableDef.ACCOUNT.USER_ID.eq(userId)
        ));
    }

    @Override
    public Boolean feDeduction(Long userId, Long amount) {
        Account one = getOne(query().where(
                AccountTableDef.ACCOUNT.USER_ID.eq(userId)
        ));
        one.setAmount(one.getAmount() - amount);
        return updateById(one);
    }


}
