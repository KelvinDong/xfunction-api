package net.xfunction.java.api.modules.activity.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import net.xfunction.java.api.core.utils.PageResultSet;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivity;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityComment;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityEntry;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivityTemp;
import net.xfunction.java.api.modules.activity.pojo.PicFileQuery;
import net.xfunction.java.api.modules.activity.pojo.activityComment.ActivityCommentQuery;
import net.xfunction.java.api.modules.activity.pojo.activityComment.ActivityCommentVo;
import net.xfunction.java.api.modules.activity.pojo.activityEntry.ActivityEntryQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityList.MyActivityListQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.LotteryQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.LotteryResultDto;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.LotteryResultVo;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.MyActivityLotteryDto;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.MyActivityLotteryQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityLottery.MyActivityLotteryVo;
import net.xfunction.java.api.modules.activity.pojo.myActivityTemp.MyActivityTempPojo;
import net.xfunction.java.api.modules.activity.pojo.myActivityTemp.MyActivityTempQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityWall.MyActivityWallQuery;
import net.xfunction.java.api.modules.activity.pojo.myActivityWall.MyActivityWallVo;
import net.xfunction.java.api.modules.activity.pojo.myComment.MyCommentsQuery;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryQuery;
import net.xfunction.java.api.modules.activity.pojo.myEntry.MyEntryVo;

public interface MainService {

	Result createUpdateActivity(XfuActivityTemp temp) ;

	String savePic(PicFileQuery form) throws IllegalStateException, IOException;

	PageResultSet getMyActivityList(MyActivityListQuery query);


	Boolean updateActivityByUp(MyActivityListQuery query);

	Result updateActivityByDown(MyActivityListQuery query);

	Result addEntry(XfuActivityEntry entry);


	Result getMyActivityEntries(ActivityEntryQuery query);

	void dlMyActivityEntries(ActivityEntryQuery query, HttpServletResponse response);


	Result signByEntryCheckinCode(ActivityEntryQuery query);


	Result signBySponsor(ActivityEntryQuery query);

	MyActivityTempPojo selectActivityTemp(MyActivityTempQuery query);

	PageResultSet getMyEntries(MyEntryQuery query);

	Result cancelEntry(MyEntryQuery query);

	Result singByActivityToken(MyEntryQuery query);

	PageResultSet getActivityComments(ActivityCommentQuery query);

	PageResultSet getMyComments(MyCommentsQuery query);

	Result createComment(XfuActivityComment comment);

	Result replayComment(ActivityCommentVo vo);

	Result delComment(MyCommentsQuery query);

	void dlMyActivityEntriesTemple(ActivityEntryQuery query, HttpServletResponse response);

	Result importActivityEntries(MyEntryVo query) throws IllegalStateException, IOException;

	Result cancelBySponsor(ActivityEntryQuery query);

	Result saveActivityLotterySettings(MyActivityLotteryVo vo);


	MyActivityLotteryDto getActivityLotterySettings(MyActivityLotteryQuery query);

	Result getMyActivityAllEntries(ActivityEntryQuery query);

	Result addActivityLotteryResult(LotteryResultVo vo);

	List<LotteryResultDto> getActivityLotteryResults(LotteryQuery query);

	void dlActivityLotteryResults(LotteryQuery query, HttpServletResponse httpServletResponse);

	Result getActivityWallSettingsAndTotal(MyActivityWallQuery query);

	Result saveWallSettings(MyActivityWallVo vo);


}
