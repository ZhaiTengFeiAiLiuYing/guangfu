package com.pb.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by Administrator on 2019/5/17.
 */
public interface MyMapper<T> extends Mapper<T> ,MySqlMapper<T> {
}
