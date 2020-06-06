package net.xfunction.java.api.modules.activity.enums;

import lombok.Getter;

@Getter
public enum XfuActivityOrderDict {
	
	DOWN("私有", 0),
    UP("公开", 1),
    HEAD("置顶", 500);

    private String name;
    private Integer value;

    XfuActivityOrderDict(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
