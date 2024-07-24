package cn.apimix.service.impl;

import cn.apimix.mapper.CategoryApiMapper;
import cn.apimix.model.entity.CategoryApi;
import cn.apimix.service.ICategoryApiService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Hor
 * @since 2024-05-31
 */
@Service
public class CategoryApiServiceImpl extends ServiceImpl<CategoryApiMapper, CategoryApi> implements ICategoryApiService {

}
