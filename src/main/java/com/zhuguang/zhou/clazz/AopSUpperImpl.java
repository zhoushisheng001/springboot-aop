package com.zhuguang.zhou.clazz;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public class AopSUpperImpl extends AopSupper {

    private boolean isParent;


    public void update () {
        System.out.println("..... update .....");
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }
}
