package net.xfunction.java.api.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.xfunction.java.api.core.model.xfunction.SysDictData;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDictDto {


	
	private String dictLabel;
	
	private String dictValue;
	
	private Boolean isDefault;

	private String cssClass;

	private String listClass;
	
	public DataDictDto(SysDictData sysDictData) {
		
		this.dictLabel = sysDictData.getDictLabel();
		this.dictValue = sysDictData.getDictValue();
		this.isDefault = sysDictData.getIsDefault();

		this.cssClass = sysDictData.getCssClass();
		this.listClass = sysDictData.getListClass();
	}
	
}
