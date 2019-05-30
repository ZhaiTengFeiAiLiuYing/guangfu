package com.pb.pojo;

import lombok.Data;

import javax.persistence.Table;

/**
 * Created by Administrator on 2019/5/17.
 */
@Data
@Table(name = "用户站点")
public class UserStation {
    private String id;
    private String userId;
    private String stationId;
}
