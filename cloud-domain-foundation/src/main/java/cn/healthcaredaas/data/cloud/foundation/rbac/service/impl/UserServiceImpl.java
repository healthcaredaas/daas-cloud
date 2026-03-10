package cn.healthcaredaas.data.cloud.foundation.rbac.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.healthcaredaas.data.cloud.foundation.rbac.dao.RbacResourceDao;
import cn.healthcaredaas.data.cloud.foundation.rbac.dao.RbacUserDao;
import cn.healthcaredaas.data.cloud.foundation.rbac.dao.RbacUserDepartmentDao;
import cn.healthcaredaas.data.cloud.foundation.rbac.dao.RbacUserRoleDao;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacResource;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUser;
import cn.healthcaredaas.data.cloud.foundation.rbac.enums.AccountStatusEnum;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUserDepartment;
import cn.healthcaredaas.data.cloud.foundation.rbac.model.RbacUserRole;
import cn.healthcaredaas.data.cloud.foundation.rbac.service.IUserService;
import cn.healthcaredaas.data.cloud.foundation.rbac.vo.RbacUserVO;
import cn.healthcaredaas.data.cloud.security.core.properties.SecurityProperties;
import cn.healthcaredaas.data.cloud.security.core.utils.SecurityUtils;
import cn.healthcaredaas.data.cloud.data.mybatisplus.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**

 * @ClassName： UserServiceImpl.java
 * @Author： chenpan
 * @Date：2024/12/1 15:48
 * @Modify：
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<RbacUserDao, RbacUser>
        implements IUserService {

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private RbacUserRoleDao userRoleDao;

    @Resource
    private RbacResourceDao authorityDao;

    @Resource
    private RbacUserDepartmentDao userDepartmentDao;

    @Override
    public RbacUser findUser(String username) {
        LambdaQueryWrapper<RbacUser> qw = new LambdaQueryWrapper<>();
        qw.apply("lower(username) = {0}", username.toLowerCase())
                .or()
                .eq(RbacUser::getPhone, username);
        return super.getOne(qw);
    }

    @Override
    public RbacUser findUserByPhone(String phone) {
        LambdaQueryWrapper<RbacUser> qw = new LambdaQueryWrapper<>();
        qw.eq(RbacUser::getPhone, phone);
        return super.getOne(qw);
    }

    /**
     * 获取用户权限编码列表
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> getUserAuthorities(String userId) {
        List<RbacResource> authorities = authorityDao.findUserResource(userId);
        return authorities.stream().map(RbacResource::getResourceCode)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
    }

    /**
     * 获取用户科室列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserDepartments(String userId) {
        LambdaQueryWrapper<RbacUserDepartment> qw = new LambdaQueryWrapper<>();
        qw.eq(RbacUserDepartment::getUserId, userId);
        List<RbacUserDepartment> departments = userDepartmentDao.selectList(qw);
        if (CollectionUtil.isNotEmpty(departments)) {
            return departments.stream()
                    .map(RbacUserDepartment::getDeptId)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updatePassword(String userId, String password) {
        RbacUser user = getById(userId);
        user.setPassword(SecurityUtils.encrypt(password));
        super.updateById(user);
    }

    @Override
    public void changeStatus(String userId, AccountStatusEnum status) {
        RbacUser user = getById(userId);
        if (ObjectUtil.isNotEmpty(user)) {
            user.setStatus(status);
            super.updateById(user);
        }
    }

    @Override
    public void changeStatusByUsername(String username, AccountStatusEnum status) {
        RbacUser user = findUser(username);
        if (ObjectUtil.isNotEmpty(user)) {
            user.setStatus(status);
            super.updateById(user);
        }
    }

    /**
     * 新增用户
     *
     * @param user
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void addUser(RbacUserVO user) {
        checkLogicUnique(user, null);
        setUserDefaultValue(user, true);
        save(user);
        saveUserRoleAndDept(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void updateUser(RbacUserVO user) {
        checkLogicUnique(user, user.getId());
        setUserDefaultValue(user, false);
        updateNotNull(user);
        deleteUserRoleAndDept(user.getId());
        saveUserRoleAndDept(user);
    }

    /**
     * 更新账户信息
     *
     * @param user
     */
    @Override
    public void updateUserProfile(RbacUser user) {
        user.setUsername(null);
        user.setPassword(null);

        this.updateNotNull(user);
    }

    /**
     * 设置用户默认值
     *
     * @param user
     */
    private void setUserDefaultValue(RbacUserVO user, boolean isNew) {
        if (isNew) {
            if (StrUtil.isBlank(user.getPassword())) {
                user.setPassword(securityProperties.getDefaultPasswd());
            }
            user.setPassword(SecurityUtils.encrypt(user.getPassword()));
        }
        if (user.getStatus() == null) {
            user.setStatus(AccountStatusEnum.VALID);
        }
    }

    /**
     * 删除用户角色和科室信息
     *
     * @param userId
     */
    private void deleteUserRoleAndDept(String userId) {
        LambdaQueryWrapper<RbacUserRole> userRoleQueryWrapper = Wrappers.lambdaQuery(RbacUserRole.class);
        userRoleQueryWrapper.eq(RbacUserRole::getUserId, userId);
        userRoleDao.delete(userRoleQueryWrapper);

        LambdaQueryWrapper<RbacUserDepartment> userDeptQueryWrapper = Wrappers.lambdaQuery(RbacUserDepartment.class);
        userDeptQueryWrapper.eq(RbacUserDepartment::getUserId, userId);
        userDepartmentDao.delete(userDeptQueryWrapper);
    }

    /**
     * 保存用户角色和科室信息
     *
     * @param user
     */
    private void saveUserRoleAndDept(RbacUserVO user) {
        if (user.getRoleIds() != null) {
            for (String roleId : user.getRoleIds()) {
                RbacUserRole userRole = new RbacUserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoleDao.insert(userRole);
            }
        }
        if (user.getDeptIds() != null) {
            for (String deptId : user.getDeptIds()) {
                RbacUserDepartment userDepartment = new RbacUserDepartment();
                userDepartment.setUserId(user.getId());
                userDepartment.setDeptId(deptId);
                userDepartmentDao.insert(userDepartment);
            }
        }
    }
}
