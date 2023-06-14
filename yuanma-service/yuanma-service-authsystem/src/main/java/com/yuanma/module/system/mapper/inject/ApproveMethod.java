//package com.yuanma.module.system.mapper.inject;
//
//import com.baomidou.mybatisplus.core.enums.SqlMethod;
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.metadata.TableInfo;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlSource;
//
//public class ApproveMethod extends AbstractMethod {
//    @Override
//    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
//        String sql = String.format("<script>\nUPDATE %s set approve_flag = #{approveFlag} WHERE %s = #{id} \n</script>"
//                , tableInfo.getTableName()
//                , tableInfo.getKeyColumn());
//        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, modelClass);
//        return this.addUpdateMappedStatement(mapperClass, modelClass, "updateApproveById", sqlSource);
//    }
//}
