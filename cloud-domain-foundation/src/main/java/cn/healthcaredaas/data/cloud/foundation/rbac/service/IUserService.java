package cn.healthcaredaas.data.cloud.foundation.rbac.service;


import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUser;
import cn.healthcaredaas.data.cloud.foundation.rbac.enums.AccountStatusEnum;
import cn.healthcaredaas.data.cloud.foundation.rbac.vo.RbacUserVO;

import java.util.List;
import java.util.Set;

/**
 * @author chenpan
 * @date 2020/6/11 13:39
 */
public interface IUserService {

    RbacUser getById(String userId);

    RbacUser findUser(String username);

    /**
     * 根据手机号获取用户信息
     *
     * @param phone
     * @return
     */
    RbacUser findUserByPhone(String phone);

    /**
     * 获取用户权限编码列表
     *
     * @param userId
     * @return
     */
    Set<String> getUserAuthorities(String userId);

    /**
     * 获取用户科室列表
     *
     * @param userId
     * @return
     */
    List<String> getUserDepartments(String userId);

    /**
     * 修改密码
     *
     * @param userId
     * @param password
     */
    void updatePassword(String userId, String password);

    /**
     * 修改账户状态
     *
     * @param userId
     * @param status
     */
    void changeStatus(String userId, AccountStatusEnum status);

    /**
     * 根据用户名修改账户状态
     *
     * @param username
     * @param status
     */
    void changeStatusByUsername(String username, AccountStatusEnum status);


    /**
     * 新增用户
     *
     * @param user
     */
    void addUser(RbacUserVO user);

    /**
     * 更新用户
     *
     * @param user
     */
    void updateUser(RbacUserVO user);

    /**
     * 更新账户信息
     *
     * @param user
     */
    void updateUserProfile(RbacUser user);
}
