package com.exam.controller;

import com.exam.common.Result;
import com.exam.dto.LoginDTO;
import com.exam.entity.SysUser;
import com.exam.service.AuthService;
import com.exam.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return Result.success(loginVO);
    }

    @GetMapping("/currentUser")
    public Result<SysUser> getCurrentUser() {
        SysUser user = authService.getCurrentUser();
        user.setPassword(null);
        return Result.success(user);
    }
}
