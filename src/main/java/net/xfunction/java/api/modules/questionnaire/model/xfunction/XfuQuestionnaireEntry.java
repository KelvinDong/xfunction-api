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
@Table(name = "xfu_questionnaire_entry")
public class XfuQuestionnaireEntry {
    @Id
    @Column(name = "xfu_entry_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long xfuEntryId;

    @Column(name = "xfu_questionnaire_id")
    private Long xfuQuestionnaireId;

    @Column(name = "xfu_user_id")
    private Long xfuUserId;

    @Column(name = "xfu_entry_create")
    private Date xfuEntryCreate;

    @Column(name = "xfu_entry_content")
    private String xfuEntryContent;

    public static final String XFU_ENTRY_ID = "xfuEntryId";

    public static final String XFU_QUESTIONNAIRE_ID = "xfuQuestionnaireId";

    public static final String XFU_USER_ID = "xfuUserId";

    public static final String XFU_ENTRY_CREATE = "xfuEntryCreate";

    public static final String XFU_ENTRY_CONTENT = "xfuEntryContent";
}