package mif.prunus.com.mbr.vo;

import lombok.Data;

@Data
public class MbrDto {

	private String mbrSeq;
	private String mbrId;
	private String mbrPw;
	private String mbrNm;
	private String mbrPon;
	private String mbrLastLoginDt;
	private String mbrLastLoginIp;
	private String mbrLastLoginMac;
	private int mbrLoginFailCnt;
	private String mbrJoinDt;

	private String mbrLoginYn;
}
