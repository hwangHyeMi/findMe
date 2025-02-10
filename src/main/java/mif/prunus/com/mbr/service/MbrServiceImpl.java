package mif.prunus.com.mbr.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mif.prunus.com.mbr.mapper.MbrMapper;
import mif.prunus.com.mbr.vo.CustomUserDetails;
import mif.prunus.com.mbr.vo.MbrVO;

@Service
public class MbrServiceImpl implements MbrService {

	@Autowired
	private MbrMapper mbrMapper;

	@Override
	public Optional<CustomUserDetails> selectMbrChk(MbrVO vo) {
		return mbrMapper.selectMbrChk(vo);
	}
}
