package com.resuper.multidatasource;

import com.resuper.multidatasource.entity.User;
import com.resuper.multidatasource.mapper.MutiDadaSource1UserMapper;
import com.resuper.multidatasource.mapper.MutiDadaSource2UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
class MultiDatasourceApplicationTests {

    @Resource
    private MutiDadaSource1UserMapper mutiDadaSource1UserMapper;
    @Resource
    private MutiDadaSource2UserMapper mutiDadaSource2UserMapper;


    /**
     *
     * 实现多数据源
     * 1.将url、驱动、用户名、密码 等jdbc基本信息 写入配置文件application.properties
     * 2. 编写配置类，在配置类中对每个数据源配置一套
     * ==========DataSource , SqlSessionFactory , MapperScannerConfigurer , DataSourceTransactionManager  , Annotation
     * 3. 哪个mapper使用哪个数据源 由第二步中指定的注解决定
     * =================在Mapper接口名上添加注解======================
     * 调用mapper的方法时就会选择到相应的数据源
     *
     *
     */
    @Test
    void contextLoads() {
        List<User> users1 = mutiDadaSource1UserMapper.selMutiDadaSource1User();
        log.info(users1.toString());

        List<User> users2 = mutiDadaSource2UserMapper.selMutiDadaSource2User();
        log.info(users2.toString());
    }

}
