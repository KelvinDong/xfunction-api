package net.xfunction.java.api.modules.activity.pojo.sponsor;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import net.xfunction.java.api.modules.activity.model.xfunction.XfuActivitySponsor;

@Data
public class SponsorVo {
	
	private Long sponsorId;
	private String sponsorName;
	private String sponsorLogo;
	private String sponsorIntro;
	
	private MultipartFile file;
	
	public XfuActivitySponsor convertToXfuActivitySponsor() {
		XfuActivitySponsor xfuActivitySponsor = new XfuActivitySponsor();
		xfuActivitySponsor.setXfuUserId(this.sponsorId);
		xfuActivitySponsor.setXfuSponsorName(this.sponsorName);
		xfuActivitySponsor.setXfuSponsorLogo(this.sponsorLogo);
		xfuActivitySponsor.setXfuSponsorIntro(this.sponsorIntro);
		return xfuActivitySponsor;
	}
	
}
