package com.resuper.multidatasource.dsconfig;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Configuration
public class MultiDataSourceConfig {


    // ==================================配置数据源===============================================

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "jdbc.mutidatasource1")
    public DataSource mutidatasource1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "jdbc.mutidatasource2")
    public DataSource mutidatasource2DataSource() {
        return DataSourceBuilder.create().build();
    }


    // ==================================配置sqlsessionFactory 设置datasource==============================================
    // 每一个SqlSessionFactory都设置了一个DataSource属性 , 所以他们产生的SqlSession就是不同数据库的SqlSession ;
    // TypeAliasesPackage属性表示扫描该属性下的全部java文件作为别名 ,
    // 即mapper.xml文件中 select 标签 resultType属性的值 ,如果不设置则要输入对应实体的全限定名称 ,设置之后只需输入对应实体的类名即可;
    // MapperLocations 属性用于扫描指定文件下的哪些文件作为MyBatis的xml映射文件;
    @Bean
    @Primary
    public SqlSessionFactory mutidatasource1SqlSessionFactory(@Qualifier("mutidatasource1DataSource") DataSource mutidatasource1DataSource) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(mutidatasource1DataSource);
        // 扫描该路径下的所有java文件作为别名
        fb.setTypeAliasesPackage("com.resuper.multidatasource");
        fb.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/mutidatasource1/**/*.xml")
        );
        return fb.getObject();
    }

    @Bean
    public SqlSessionFactory mutidatasource2SqlSessionFactory(@Qualifier("mutidatasource2DataSource") DataSource mutidatasource2DataSource) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(mutidatasource2DataSource);
        fb.setTypeAliasesPackage("com.resuper.multidatasource");
        fb.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/mutidatasource2/**/*.xml")
        );
        return fb.getObject();
    }

    // ==================================配置MapperScannerConfigurer==============================================
    //    MapperScannerConfigurer用于配置MyBatis Mapper接口的扫描 ;
    //    该对象可以设置BasePackage 和 AnnotationClass属性,
    //    分别表示 扫描哪个包下的文件且标识了指定注解的接口 作为MyBatis 的 Mapper , 同时还需设置一个SqlSessionFactory; 

    @Bean(name = "mutidatasource1MapperScannerConfigurer")
    public MapperScannerConfigurer mutidatasource1MapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("mutidatasource1SqlSessionFactory");
        configurer.setBasePackage("com.resuper.multidatasource");
        configurer.setAnnotationClass(MutiDataSource1Dao.class);
        return configurer;
    }

    @Bean(name = "mutidatasource2MapperScannerConfigurer")
    public MapperScannerConfigurer mutidatasource2MapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("mutidatasource2SqlSessionFactory");
        configurer.setBasePackage("com.resuper.multidatasource");
        configurer.setAnnotationClass(MutiDataSource2Dao.class);
        return configurer;
    }


    //====================================被引用的注解==============================================================
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Component
    public @interface MutiDataSource1Dao {
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Component
    public @interface MutiDataSource2Dao {
        String value() default "";
    }

    //===============================================================================================================
    // 事务管理器只需要指定对应的DataSource即可
    @Bean(name = "mutidatasource1TransactionManager")
    @Primary
    public DataSourceTransactionManager activitiTransactionManager(@Qualifier("mutidatasource1DataSource") DataSource mutidatasource1DataSource) throws Exception {
        return new DataSourceTransactionManager(mutidatasource1DataSource);
    }

    @Bean(name = "mutidatasource2TransactionManager")
    public DataSourceTransactionManager sbusedbTransactionManager(@Qualifier("mutidatasource2DataSource") DataSource mutidatasource2DataSource) throws Exception {
        return new DataSourceTransactionManager(mutidatasource2DataSource);
    }
}