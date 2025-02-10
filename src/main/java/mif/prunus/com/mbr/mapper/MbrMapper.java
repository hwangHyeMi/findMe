package mif.prunus.com.mbr.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import mif.prunus.com.mbr.vo.CustomUserDetails;
import mif.prunus.com.mbr.vo.MbrVO;

@Mapper
public interface MbrMapper {

	Optional<CustomUserDetails> selectMbrChk(MbrVO vo);

}
