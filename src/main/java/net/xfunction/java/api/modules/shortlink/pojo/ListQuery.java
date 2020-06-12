package net.xfunction.java.api.modules.shortlink.pojo;

import lombok.Getter;
import lombok.Setter;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.shortlink.model.xfunction.BizShortLink;
@Getter
@Setter
public class ListQuery extends BaseQuery<BizShortLink> {

	private Long userId;
	
}
