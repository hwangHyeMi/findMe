package mif.prunus.front.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * -Request- 검색 요청에 대한 정보 DTO, 검색 조건이 늘어날 수 있어 객체로 만듬
 */

@Getter
@Setter
public class SearchDataVO {

	String title;
	String content;
	String writerName;

	int first;
	int rowCnt;

	@Builder
	public SearchDataVO(String title, String content, String writerName) {
		this.title = title;
		this.content = content;
		this.writerName = writerName;
	}

	public static SearchDataVO createdSearchData(String title, String content, String writerName) {
		return SearchDataVO.builder().title(title).content(content).writerName(writerName).build();
	}
}
