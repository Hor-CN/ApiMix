package cn.apimix.service.impl;

import cn.apimix.mapper.ApiExampleMapper;
import cn.apimix.model.entity.ApiExample;
import cn.apimix.service.ApiExampleService;
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
