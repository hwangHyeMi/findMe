package mif.prunus.front.board.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mif.prunus.front.board.mapper.BoardMyBatisMapper;
import mif.prunus.front.board.vo.SearchDataVO;

@Service
public class BoardMyBatisServiceImpl implements BoardMyBatisService {

	@Autowired
	private BoardMyBatisMapper mapper;

	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> selectBoardList (SearchDataVO vo) throws Exception {
		return mapper.selectBoardList(vo);
	}
}
