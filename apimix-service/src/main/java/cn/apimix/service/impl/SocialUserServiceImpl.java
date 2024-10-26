package cn.apimix.service.impl;

import cn.apimix.mapper.SocialUserMapper;
import cn.apimix.model.entity.SocialUser;
import cn.apimix.service.ISocialUserService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Hor
 * @since 2024-10-13
 */
@Service
public class SocialUserServiceImpl extends ServiceImpl<SocialUserMapper, SocialUser> implements ISocialUserService {

}
