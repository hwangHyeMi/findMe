package mif.prunus.com.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import mif.prunus.front.board.web.BoardController;

@Slf4j
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

	private String secretKey;

	// 사용자가 전달하는 추가적인 파라미터들을 저장하는 클래스
	public FormWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		secretKey = request.getParameter("secret_key");
		log.info("FormWebAuthenticationDetails secretKey: : {}", secretKey);
	}

	public String getSecretKey() {
		return secretKey;
	}
}