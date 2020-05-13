package com.java2nb.novel.service;


import com.java2nb.novel.entity.Author;
import com.java2nb.novel.entity.FriendLink;

import java.util.List;

/**
 * @author 11797
 */
public interface AuthorService {

    /**
     * 校验笔名是否存在
     * @param penName 校验的笔名
     * @return true：存在该笔名，false: 不存在该笔名
     * */
    Boolean checkPenName(String penName);

    /**
     * 作家注册
     * @param userId 注册用户ID
     *@param author 注册信息
     * @return 返回错误信息
     * */
    String register(Long userId, Author author);

    /**
     * 判断是否是作家
     * @param userId 用户ID
     * @return true：是作家，false: 不是作家
     * */
    Boolean isAuthor(Long userId);

    /**
     * 查询作家信息
     * @param userId 用户ID
     * @return 作家对象
     * */
    Author queryAuthor(Long userId);
}
