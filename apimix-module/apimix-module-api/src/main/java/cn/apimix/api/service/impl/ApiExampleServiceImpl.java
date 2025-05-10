package cn.apimix.api.service.impl;

import cn.apimix.api.mapper.ApiExampleMapper;
import cn.apimix.api.model.entity.ApiExample;
import cn.apimix.api.service.ApiExampleService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author: Hor
 * @Date: 2025/1/19 21:54
 * @Version: 1.0
 */
@Service
public class ApiExampleServiceImpl extends ServiceImpl<ApiExampleMapper, ApiExample> implements ApiExampleService {
}
