package com.resuper.multidatasource.mapper;

import com.resuper.multidatasource.dsconfig.MultiDataSourceConfig;
import com.resuper.multidatasource.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@MultiDataSourceConfig.MutiDataSource2Dao
public interface MutiDadaSource2UserMapper {

    @Select("select * from user")
    public List<User> selMutiDadaSource2User();
}
