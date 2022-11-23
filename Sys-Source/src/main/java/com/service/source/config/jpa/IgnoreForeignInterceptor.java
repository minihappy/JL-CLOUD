//package com.service.source.config.jpa;
//
//import org.hibernate.boot.Metadata;
//import org.hibernate.dialect.Dialect;
//import org.hibernate.mapping.ForeignKey;
//import org.hibernate.tool.schema.internal.StandardForeignKeyExporter;
//import org.hibernate.tool.schema.spi.Exporter;
//import org.springframework.cglib.proxy.MethodInterceptor;
//import org.springframework.cglib.proxy.MethodProxy;
//
//import java.lang.reflect.Method;
//
///**
// * @author hjl
// * @version 1.0
// * @description 排除外键生成拦截器
// * @date 2022/11/17 11:35
// */
//
//public class IgnoreForeignInterceptor implements MethodInterceptor {
//    @Override
//    public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//
//        //直接返回Exporter.NO_COMMANDS
//        if(method.getName().equals("getForeignKeyExporter")){
//            return new StandardForeignKeyExporter((Dialect) object){
//                @Override
//                public String[] getSqlCreateStrings(ForeignKey foreignKey, Metadata metadata) {
//                    return Exporter.NO_COMMANDS;
//                }
//
//                @Override
//                public String[] getSqlDropStrings(ForeignKey foreignKey, Metadata metadata) {
//                    return Exporter.NO_COMMANDS;
//                }
//            };
//        }
//        return methodProxy.invokeSuper(object, objects);
//    }
//}
