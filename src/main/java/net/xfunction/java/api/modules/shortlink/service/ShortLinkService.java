package net.xfunction.java.api.modules.shortlink.service;

import net.xfunction.java.api.modules.shortlink.model.xfunction.BizShortLink;

public interface ShortLinkService {



	BizShortLink getBizShortLinkByLinkId(Long bizShortId);

	BizShortLink getBizShortLinkByLinkUrl(String bizLinkUrl);

	BizShortLink getBizShortLink(String bizLinkUrl);

}
