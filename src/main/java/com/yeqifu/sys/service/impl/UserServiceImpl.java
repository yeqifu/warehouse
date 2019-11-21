package com.yeqifu.sys.service.impl;

import com.yeqifu.sys.entity.User;
import com.yeqifu.sys.mapper.UserMapper;
import com.yeqifu.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * InnoDB free: 9216 kB; (`deptid`) REFER `warehouse/sys_dept`(`id`) ON UPDATE CASC 服务实现类
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
