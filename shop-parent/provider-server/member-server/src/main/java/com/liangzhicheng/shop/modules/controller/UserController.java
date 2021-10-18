package com.liangzhicheng.shop.modules.controller;

import com.google.common.collect.Lists;
import com.liangzhicheng.shop.annotation.UserParam;
import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.utils.CookieUtil;
import com.liangzhicheng.shop.common.utils.DBUtil;
import com.liangzhicheng.shop.entity.User;
import com.liangzhicheng.shop.modules.service.IUserService;
import com.liangzhicheng.shop.modules.dto.LoginUserDTO;
import com.liangzhicheng.shop.utils.MD5Util;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping(value = "/login")
    public ResponseResult<String> login(@Valid LoginUserDTO loginUserDTO,
                                        HttpServletResponse response){
        String token  = userService.login(loginUserDTO);
        CookieUtil.setToken(token, response);
        return ResponseResult.success(token);
    }

    /**
     * @description 方式1.通过spring mvc提供@CookieValue注解，获取token，从redis中根据token获取用户并返回
     *              方式2.通过对象的注入方式直接获取用户并返回，只有贴上@UserParam注解才注入到bean，减少请求调用service层，
     *                    需配置UserArgumentResolver类解析器和MemberWebConfig配置类（将解析器注入到bean中），通过启动配置直接调用
     * @param token
     * @return ResponseResult<User>
     */
//    @GetMapping("/getByToken")
//    public ResponseResult<User> getUserByToken(
//            @CookieValue(name = Constants.USER_LOGIN_TOKEN) String token){
//        return ResponseResult.success(userService.getByToken(token));
//    }
    @GetMapping(value = "/getByToken")
    public ResponseResult<User> getUserByToken(@UserParam User user){
        return ResponseResult.success(user);
    }


    //--------------------------------------------------------------------------JMeter压测秒杀接口--------------------------------------------------------------------------

    @RequestMapping(value = "/initData")
    public ResponseResult<String> initData() throws Exception {
        List<User> users = this.initUser(500);
        insertToDb(users); //创建User对象并保存到数据库中
        createToken(users); //通过User对象，调用service方法，模拟登陆，创建token存在磁盘文件中.(jmeter)
        return ResponseResult.success();
    }

    /**
     * @description 创建用户对象
     * @param count
     * @return List<User>
     */
    private List<User> initUser(int count) {
        List<User> users = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(13000000000L + i);
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2c3d4e");
            user.setPassword(MD5Util.encryptAgain(
                    MD5Util.encrypt("111111"), user.getSalt()));
            users.add(user);
        }
        return users;
    }

    /**
     * @description 保存数据库操作
     * @param users
     * @throws Exception
     */
    private void insertToDb(List<User> users) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "insert into t_user (login_count, nickname, register_date, salt, password, id) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            statement.setInt(1, user.getLoginCount());
            statement.setString(2, user.getNickname());
            statement.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            statement.setString(4, user.getSalt());
            statement.setString(5, user.getPassword());
            statement.setLong(6, user.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
        conn.close();
    }

    /**
     * @description 创建token
     * @param users
     * @throws Exception
     */
    private void createToken(List<User> users) throws Exception {
        File file = new File("D:/tokens.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            LoginUserDTO loginUserDTO = new LoginUserDTO();
            loginUserDTO.setPassword(users.get(i).getId() + "");
            loginUserDTO.setPassword(MD5Util.encrypt("111111"));
            String token = userService.login(loginUserDTO);
            String row = users.get(i).getId() + "," + token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
        }
        raf.close();
    }

}
