package cn.apimix.mapper;


import cn.apimix.model.entity.Account;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * @Author: Hor
 * @Date: 2024/6/24 上午10:50
 * @Version: 1.0
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
