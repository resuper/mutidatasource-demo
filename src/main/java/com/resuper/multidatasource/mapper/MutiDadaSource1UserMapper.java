package com.resuper.multidatasource.mapper;

import com.resuper.multidatasource.dsconfig.MultiDataSourceConfig;
import com.resuper.multidatasource.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@MultiDataSourceConfig.MutiDataSource1Dao
public interface MutiDadaSource1UserMapper {

    @Select("select * from user")
    public List<User> selMutiDadaSource1User();
}
