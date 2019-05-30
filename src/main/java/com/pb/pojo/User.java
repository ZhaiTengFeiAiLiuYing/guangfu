package com.pb.pojo;

import lombok.Data;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by Administrator on 2019/5/17.
 */
@Table(name = "用户表")
@Data
public class User {
    private String id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String weixin;
    private Set<Station> stations;
}
