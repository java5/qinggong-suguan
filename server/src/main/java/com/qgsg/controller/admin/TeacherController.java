package com.qgsg.controller.admin;

import com.qgsg.config.mqtt.MqttInboundConfiguration;
import com.qgsg.constant.JwtClaimsConstant;
import com.qgsg.dto.TeacherDTO;
import com.qgsg.dto.TeacherLoginDTO;
import com.qgsg.entity.Teacher;
import com.qgsg.properties.JwtProperties;
import com.qgsg.result.Result;
import com.qgsg.service.TeacherService;
import com.qgsg.utils.JwtUtil;
import com.qgsg.vo.TeacherLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 教师管理
 */
@RestController
@RequestMapping("/admin/teacher")
@Api(tags = "管理员老师相接口")
@Slf4j
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 教师登录
     *
     * @param teacherLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<TeacherLoginVO> login(@RequestBody TeacherLoginDTO teacherLoginDTO) {

        log.info("教师登录：{}", teacherLoginDTO);

        Teacher teacher = teacherService.login(teacherLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, teacher.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        TeacherLoginVO teacherLoginVO = TeacherLoginVO.builder()
                .id(teacher.getId())
                .userName(teacher.getUsername())
                .name(teacher.getName())
                .token(token)
                .build();

        return Result.success(teacherLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 新增管理员
     * @param teacherDTO
     * @return
     */
    @PostMapping
    @ApiOperation("注册管理员")
    public Result save(@RequestBody TeacherDTO teacherDTO){
        log.info("注册管理员:{}",teacherDTO);
        teacherService.save(teacherDTO);
        return Result.success();
    }
}
