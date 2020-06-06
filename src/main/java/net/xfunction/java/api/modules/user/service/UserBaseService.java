package net.xfunction.java.api.modules.user.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.user.model.xfunction.XfuUser;
import net.xfunction.java.api.modules.user.query.UserQuery;

public interface UserBaseService {

	// Result createUser(XfuUser user);

	// Result findUserByAuth(XfuUser user);

	// void updateUserByAuth(XfuUser user);

	Result getUser(XfuUser user);

	Result updateUser(XfuUser user);

	// XfuUser isMobleExist(String userMoble);

	// XfuUser getXfuUser(XfuUser user);

	String saveUserPic(XfuUser user, MultipartFile file) throws IllegalStateException, IOException;

	/*
	Result bindResult(UserQuery userQuery);

	Result resetFromMail(UserQuery userQuery);
	
	Result sendPasswordMail(XfuUser user);

	Result sendBindMail(XfuUser user);
	*/
	
}
