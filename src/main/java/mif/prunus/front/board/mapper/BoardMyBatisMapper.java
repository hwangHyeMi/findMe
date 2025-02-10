package mif.prunus.front.board.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import mif.prunus.front.board.vo.SearchDataVO;

@Mapper
public interface BoardMyBatisMapper {

	@SuppressWarnings("rawtypes")
	List<HashMap> selectBoardList(SearchDataVO vo) throws Exception;

}
