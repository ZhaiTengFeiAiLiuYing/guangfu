package com.pb.web;
import com.pb.enums.ExceptionEnum;
import com.pb.enums.SuccessEnum;
import com.pb.exception.GfException;
import com.pb.pojo.User;
import com.pb.service.UserService;
import com.pb.utils.CopyUtils;
import com.pb.vo.Result;
import com.pb.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2019/5/17.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/regist")
    public ResponseEntity<Result> regist(@RequestBody  User user){
        String userName=user.getUsername();
        String password=user.getPassword();
        if(StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            throw new GfException(ExceptionEnum.USERNAME_OR_PASSWORD_BE_NULL);
        }
        if(password.length()>10||password.length()<5){
            throw new GfException(ExceptionEnum.PASSWORD_LENGTH_ERROR);
        }
        int count=userService.regist(user);
        if(count<=0){
            throw new GfException(ExceptionEnum.INSERT_ERROR);
        }
        return ResponseEntity.status(SuccessEnum.SUCCESS_SAVE.getCode()).
                body(new Result(SuccessEnum.SUCCESS_SAVE));
    }
    /**
     * 用户登录认证
     */
    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody User user){
        String userName=user.getUsername();
        String password=user.getPassword();
        if(StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            throw new GfException(ExceptionEnum.USERNAME_OR_PASSWORD_BE_NULL);
        }
        User findUser=userService.login(userName,password);
        if(findUser==null){
            throw  new  GfException(ExceptionEnum.USERNAME_OR_PASSWORD_BE_ERROR);
        }
        UserVo userVo=new UserVo();
        CopyUtils.copyProperties(findUser,userVo);
        return ResponseEntity.status(SuccessEnum.SUCCESS_SELECT.getCode())
                .body(new Result(SuccessEnum.SUCCESS_SELECT,userVo));
    }


}
