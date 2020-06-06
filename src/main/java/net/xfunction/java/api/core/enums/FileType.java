package net.xfunction.java.api.core.enums;

import lombok.Getter;

@Getter
public enum FileType {
	
	ACTIVITYPIC("活动图片标题和内容", 1),
    SPONSORLOGO("主办方logo", 2),
    LOTTERYPIC("活动抽奖底图", 3),
    QUESTIONAIREPIC("调查问卷头图", 4),
    CHECKINPIC("活动签到墙图",5),
    USERAVATAR("用户头像",6),
	USERTEMP("用户临时上传", 99);

    private String name;
    private Integer value;

    FileType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
