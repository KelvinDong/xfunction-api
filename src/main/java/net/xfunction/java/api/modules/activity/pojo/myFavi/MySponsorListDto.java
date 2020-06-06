package net.xfunction.java.api.modules.activity.pojo.myFavi;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivitySponsor;

@Data
public class MySponsorListDto {

	private Long sponsorId;
	private String sponsorName;
	private String sponsorLogo;
	private String sponsorIntro;
	
	
	public MySponsorListDto(XfuActivitySponsor sponsor) {
		
		if(BaseUtils.isNotNull(sponsor)) {
			this.sponsorId = sponsor.getXfuUserId();
			this.sponsorIntro = sponsor.getXfuSponsorIntro();
			this.sponsorName = sponsor.getXfuSponsorName();
			this.sponsorLogo = sponsor.getXfuSponsorLogo();
		}
		
	}
	
	public MySponsorListDto() {	}
}
