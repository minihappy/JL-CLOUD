//package com.service.source.config.jpa;
//
//import org.hibernate.dialect.Database;
//import org.hibernate.dialect.Dialect;
//import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
//import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
//import org.springframework.cglib.proxy.Enhancer;
//import org.springframework.context.annotation.Configuration;
//
//
///**
// * @author hjl
// * @version 1.0
// * @description 自定义外键解析器
// * @date 2022/11/17 11:38
// */
//@Configuration
//public class IgnoreForeignDialectResolver implements DialectResolver {
//    @Override
//    public Dialect resolveDialect(DialectResolutionInfo info) {
//        for (Database database : Database.values()) {
//            Dialect dialect = database.resolveDialect(info);
//            if (dialect != null) {
//                IgnoreForeignInterceptor myInterceptor = new IgnoreForeignInterceptor();
//                Enhancer enhancer = new Enhancer();
//                enhancer.setSuperclass(dialect.getClass());  // 设置超类，cglib是通过继承来实现的
//                enhancer.setCallback(myInterceptor);
//                return (Dialect) enhancer.create();
//            }
//        }
//
//        return null;
//    }
//}
