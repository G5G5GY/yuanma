package com.yuanma.module.utils;

/**
 * @Author zsheng
 * @Date 2022/5/3 13:10
 * @Version 1.0
 * @Describe:
 */
public enum DataScopeType {
    ALL("全部",1),USER("本级",2),OTHER("自定义",3);

    private String name;
    private Integer level;
    DataScopeType(String name, Integer level){
        this.name = name;
        this.level = level;
    }

    public static Integer getLevelByName(String name){
        for(DataScopeType dataScopeType:DataScopeType.values()){
            if(dataScopeType.name.equals(name)){
                return dataScopeType.level;
            }
        }
        return -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
