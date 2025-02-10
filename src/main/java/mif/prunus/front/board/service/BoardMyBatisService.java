package mif.prunus.front.board.service;

import java.util.HashMap;
import java.util.List;

import mif.prunus.front.board.vo.SearchDataVO;

public interface BoardMyBatisService {

	@SuppressWarnings("rawtypes")
	List<HashMap> selectBoardList(SearchDataVO vo) throws Exception;

}
