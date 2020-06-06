package net.xfunction.java.api.modules.user.mapper.xfunction;

import net.xfunction.java.api.core.utils.XfunMapper;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;

public interface XfuUserMapper extends XfunMapper<XfuUser> {
	String selectForUserAvatar(Long xfuUserId);
	
	XfuUser selectForUser(Long xfuUserId);
}