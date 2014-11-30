package spring05.user.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MockMailSender implements MailSender {

	private static final Logger logger = LoggerFactory
			.getLogger(MockMailSender.class);

	private List<String> requests = new ArrayList<String>();

	public List<String> getRequests() {
		return requests;
	}

	@Override
	public void send(SimpleMailMessage mailMessage) throws MailException {
		// 전송 요청을 받은 이메일주소 저장.
		requests.add(mailMessage.getTo()[0]);
		logger.debug("메일이 성공적으로 발송 되었습니다.");
	}

	@Override
	public void send(SimpleMailMessage[] arg0) throws MailException {
		logger.debug("메일이 성공적으로 발송 되었습니다.");
	}

}
