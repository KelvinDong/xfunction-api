package net.xfunction.java.api.modules.activity.pojo.publicActivityList;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseQuery;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;

@Data
public class PublicActivityListQuery extends BaseQuery<XfuActivity>{   
	
	private Long sponsorId;

}
