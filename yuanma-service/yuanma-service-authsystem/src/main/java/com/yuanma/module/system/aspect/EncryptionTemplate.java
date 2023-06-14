package com.yuanma.module.system.aspect;

import com.yuanma.module.system.aspect.strategy.AESEncryption;
import com.yuanma.module.system.aspect.strategy.BASE64Encryption;
import com.yuanma.module.system.aspect.strategy.SwxaJCEEncryption;
import com.yuanma.module.system.encrption.IEncrption;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EncryptionTemplate  implements IEncrption{

    @Autowired
    private final AuthSystemProperties properties ;

    private Map<String, IEncrption> strategys = new HashMap<String, IEncrption>();

    public EncryptionTemplate(AuthSystemProperties properties){
        this.properties = properties;
        List<IEncrption> encrptions = new ArrayList<>();
        encrptions.add(new BASE64Encryption());
        encrptions.add(new AESEncryption());
        encrptions.add(new SwxaJCEEncryption());
        for(IEncrption encrption:encrptions){
            strategys.put(encrption.strategy(),encrption);
        }
    }

    public String encode(String str){
        if(StringUtils.isEmpty(str))
            return str;
        if(properties.isEncryptionEnable()){
            return strategys.get(properties.getStrategy()).encode(str);
        }
        return str;
    }

    public String decode(String str){
        if(StringUtils.isEmpty(str))
            return str;
        if(properties.isEncryptionEnable()) {
            return strategys.get(properties.getStrategy()).decode(str);
        }
        return str;
    }

    @Override
    public String strategy() {
        return properties.getStrategy();
    }

}
