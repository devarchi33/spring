package spring05.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {

	private static final Logger logger = LoggerFactory
			.getLogger(DummyMailSender.class);

	@Override
	public void send(SimpleMailMessage arg0) throws MailException {
		logger.debug("메일이 성공적으로 발송 되었습니다.");
	}

	@Override
	public void send(SimpleMailMessage[] arg0) throws MailException {
		logger.debug("메일이 성공적으로 발송 되었습니다.");
	}

}
