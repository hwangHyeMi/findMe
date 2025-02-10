package mif.prunus.com.mbr.service;

import java.util.Optional;

import mif.prunus.com.mbr.vo.CustomUserDetails;
import mif.prunus.com.mbr.vo.MbrVO;

public interface MbrService {

	Optional<CustomUserDetails> selectMbrChk(MbrVO vo);

}
