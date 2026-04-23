package com.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exam.context.UserContext;
import com.exam.dto.LoginDTO;
import com.exam.entity.SysRole;
import com.exam.entity.SysUser;
import com.exam.entity.SysUserRole;
import com.exam.exception.BusinessException;
import com.exam.mapper.SysRoleMapper;
import com.exam.mapper.SysUserMapper;
import com.exam.mapper.SysUserRoleMapper;
import com.exam.util.JwtUtil;
import com.exam.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginVO login(LoginDTO loginDTO) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, loginDTO.getUsername());
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用");
        }

        if (!"123456".equals(loginDTO.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 获取用户角色
        LambdaQueryWrapper<SysUserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(SysUserRole::getUserId, user.getId());
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(userRoleWrapper);

        List<String> roleCodes = new java.util.ArrayList<>();
        if (!userRoles.isEmpty()) {
            List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);
            roleCodes = roles.stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return new LoginVO(token, user.getId(), user.getUsername(), user.getRealName(), roleCodes);
    }

    public SysUser getCurrentUser() {
        Long userId = UserContext.getUserId();
        return sysUserMapper.selectById(userId);
    }
}
