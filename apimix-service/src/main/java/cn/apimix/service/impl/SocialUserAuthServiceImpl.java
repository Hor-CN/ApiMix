package cn.apimix.service.impl;

import cn.apimix.mapper.SocialUserAuthMapper;
import cn.apimix.model.entity.SocialUserAuth;
import cn.apimix.service.ISocialUserAuthService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Hor
 * @since 2024-10-13
 */
@Service
public class SocialUserAuthServiceImpl extends ServiceImpl<SocialUserAuthMapper, SocialUserAuth> implements ISocialUserAuthService {

}
