package com.pb.service;
import com.pb.enums.ExceptionEnum;
import com.pb.exception.GfException;
import com.pb.mapper.StationMapper;
import com.pb.mapper.UserMapper;
import com.pb.mapper.UserStationMapper;
import com.pb.pojo.Station;
import com.pb.pojo.User;
import com.pb.pojo.UserStation;
import com.pb.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


/**
 * Created by Administrator on 2019/5/17.
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserStationMapper userStationMapper;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public User findUserByName(String userName){
        Example example=new Example(User.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("username",userName);
        //criteria.andEqualTo("password",password);
        return userMapper.selectOneByExample(example);
    }
    public int regist(User user){
        User findUser=findUserByName(user.getUsername());
        if(findUser!=null){
            throw new GfException(ExceptionEnum.USER_BEREGSTRY);
        }
        String id=idWorker.nextId()+"";
        user.setId(id);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userMapper.insert(user);
    }
    public User login(String userName,String password){
        User user=findUserByName(userName);
        if(user!=null&&bCryptPasswordEncoder.matches(password,user.getPassword())){
            Example example=new Example(UserStation.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("userId",user.getId());
            List<UserStation> userStations=userStationMapper.selectByExample(example);
            List<String> stationIds=new LinkedList<>();
            for(UserStation userStation:userStations){
                stationIds.add(userStation.getStationId());
            }
            Example example12=new Example(Station.class);
            Example.Criteria criteria12=example12.createCriteria();
            criteria12.andIn("zhanDianXuLieHao",stationIds);
            List<Station> stations=stationMapper.selectByExample(example12);
            user.setStations(new HashSet<>(stations));
            return  user;
        }

        return  null;
    }
}
