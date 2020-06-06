package net.xfunction.java.api.core.model.xfunction;

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
@Table(name = "sys_dict_data")
public class SysDictData {
    /**
     * 字典编码
     */
    @Id
    @Column(name = "dict_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dictCode;

    /**
     * 字典排序
     */
    @Column(name = "dict_sort")
    private Integer dictSort;

    /**
     * 字典标签
     */
    @Column(name = "dict_label")
    private String dictLabel;

    /**
     * 字典键值
     */
    @Column(name = "dict_value")
    private String dictValue;

    /**
     * 字典类型
     */
    @Column(name = "dict_type")
    private String dictType;

    /**
     * 字典类型名称，仅用于后以管理使用
     */
    @Column(name = "dict_type_name")
    private String dictTypeName;

    /**
     * 样式属性（其他样式扩展）
     */
    @Column(name = "css_class")
    private String cssClass;

    /**
     * 表格回显样式
     */
    @Column(name = "list_class")
    private String listClass;

    /**
     * 是否默认（1是 0否）
     */
    @Column(name = "is_default")
    private Boolean isDefault;

    /**
     * 状态（1正常 0停用）
     */
    private Boolean status;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    public static final String DICT_CODE = "dictCode";

    public static final String DICT_SORT = "dictSort";

    public static final String DICT_LABEL = "dictLabel";

    public static final String DICT_VALUE = "dictValue";

    public static final String DICT_TYPE = "dictType";

    public static final String DICT_TYPE_NAME = "dictTypeName";

    public static final String CSS_CLASS = "cssClass";

    public static final String LIST_CLASS = "listClass";

    public static final String IS_DEFAULT = "isDefault";

    public static final String STATUS = "status";

    public static final String CREATE_BY = "createBy";

    public static final String CREATE_TIME = "createTime";

    public static final String UPDATE_BY = "updateBy";

    public static final String UPDATE_TIME = "updateTime";

    public static final String REMARK = "remark";
}