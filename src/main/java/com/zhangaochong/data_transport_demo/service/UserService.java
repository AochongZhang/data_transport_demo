package com.zhangaochong.data_transport_demo.service;

import com.zhangaochong.data_transport_demo.dao.UserDao;
import com.zhangaochong.data_transport_demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Aochong Zhang
 * @date 2021-04-09 15:05
 */
@Service
public class UserService {
    @Autowired
    private UserDao UserDao;

    public List<User> getUserList(String tableName) {
        return UserDao.selectTest(tableName);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(String tableName, Boolean b) {
        if (b) {
            throw new RuntimeException("测试事务回滚");
        }
        UserDao.update(tableName);
    }
}
