package net.xfunction.java.api.modules.shortlink.service;

import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.shortlink.model.xfunction.BizShortLink;
import net.xfunction.java.api.modules.shortlink.pojo.ListQuery;

public interface ShortLinkService {



	BizShortLink getBizShortLinkByLinkId(Long bizShortId);

	BizShortLink getBizShortLinkByLinkUrl(String bizLinkUrl,Long xfuUserId);

	BizShortLink getBizShortLink(String bizLinkUrl,Long xfuUserId);

	PageResultSet getMyUrls(ListQuery query);

	Result replaceShortLink(BizShortLink link, Long userId);

	void updateShortLinkRemark(BizShortLink in, Long userId);

}
