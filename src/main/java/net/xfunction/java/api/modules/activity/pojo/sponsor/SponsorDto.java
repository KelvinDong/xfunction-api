package net.xfunction.java.api.modules.activity.pojo.sponsor;

import lombok.Data;
import net.xfunction.java.api.core.utils.BaseUtils;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivitySponsor;

@Data
public class SponsorDto {

	Long sponsorId;
	String sponsorName;
	String sponsorLogo;
	String sponsorIntro;
	
	public SponsorDto(XfuActivitySponsor sponsor) {
		if(BaseUtils.isNotNull(sponsor)) {
			this.sponsorId = sponsor.getXfuUserId();
			this.sponsorName = sponsor.getXfuSponsorName();
			this.sponsorIntro = sponsor.getXfuSponsorIntro();
			this.sponsorLogo = sponsor.getXfuSponsorLogo();
		}
	}
	public SponsorDto() {}
}
