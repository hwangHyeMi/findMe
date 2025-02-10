package mif.prunus.front.board.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mif.prunus.front.board.service.BoardMyBatisService;
import mif.prunus.front.board.vo.SearchDataVO;

@RestController
@RequestMapping(value = { "/user/board", "/front/board" })
@Slf4j
public class BoardController {

	@Autowired
	private BoardMyBatisService boardMyBatisService;

	// 페이징 목록
	@GetMapping("/list2")
	public ResponseEntity<List<HashMap>> boardList2(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, SearchDataVO vo) {
		log.info("PageSize : {}", pageable.getPageSize());
		log.info("PageNumber : {}", pageable.getPageNumber());
		// log.info("Sort : {}", pageable.getSort().);
		// log.info("SortOr : {}", pageable.getSortOr(pageable.getSort()));
		vo.setFirst(pageable.getPageNumber());
		vo.setRowCnt(pageable.getPageSize());
		try {
			List<HashMap> listDTO = boardMyBatisService.selectBoardList(vo);
			return ResponseEntity.status(HttpStatus.OK).body(listDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

}
