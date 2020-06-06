package net.xfunction.java.api.modules.questionnaire.model.xfunction;

import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table(name = "xfu_questionnaire_form")
public class XfuQuestionnaireForm {
    @Id
    @Column(name = "xfu_questionnaire_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuQuestionnaireId;

    @Column(name = "xfu_user_id")
    private Long xfuUserId;
    
    @Column(name = "xfu_questionnaire_type")
    private Integer xfuQuestionnaireType;

    @Column(name = "xfu_questionnaire_name")
    private String xfuQuestionnaireName;

    @Column(name = "xfu_questionnaire_create")
    private Date xfuQuestionnaireCreate;

    @Column(name = "xfu_questionnaire_update")
    private Date xfuQuestionnaireUpdate;

    @Column(name = "xfu_questionnaire_pre")
    private String xfuQuestionnairePre;

    @Column(name = "xfu_questionnaire_after")
    private String xfuQuestionnaireAfter;

    /**
     * 收集到的总数
     */
    @Column(name = "xfu_questionnaire_count")
    private Integer xfuQuestionnaireCount;

    /**
     * 调整问卷结束时间
     */
    @Column(name = "xfu_questionnaire_expired")
    private Date xfuQuestionnaireExpired;

    /**
     * 配图
     */
    @Column(name = "xfu_questionnaire_pic")
    private String xfuQuestionnairePic;

    @Column(name = "xfu_questionnaire_json")
    private String xfuQuestionnaireJson;

    public static final String XFU_QUESTIONNAIRE_ID = "xfuQuestionnaireId";

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_QUESTIONNAIRE_NAME = "xfuQuestionnaireName";

    public static final String XFU_QUESTIONNAIRE_CREATE = "xfuQuestionnaireCreate";

    public static final String XFU_QUESTIONNAIRE_UPDATE = "xfuQuestionnaireUpdate";

    public static final String XFU_QUESTIONNAIRE_PRE = "xfuQuestionnairePre";

    public static final String XFU_QUESTIONNAIRE_AFTER = "xfuQuestionnaireAfter";

    public static final String XFU_QUESTIONNAIRE_COUNT = "xfuQuestionnaireCount";

    public static final String XFU_QUESTIONNAIRE_EXPIRED = "xfuQuestionnaireExpired";

    public static final String XFU_QUESTIONNAIRE_PIC = "xfuQuestionnairePic";

    public static final String XFU_QUESTIONNAIRE_JSON = "xfuQuestionnaireJson";
}