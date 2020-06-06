package net.xfunction.java.api.modules.activity.enums;

import lombok.Getter;

@Getter
public enum XfuActivityApplyDict {

	EDIT("编辑中", "0"),
    ING("审核中", "1"),
    FAILD("审核失败", "2"),
    SUCCESS("审核成功", "3");

    private String name;
    private String value;

    XfuActivityApplyDict(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
}
