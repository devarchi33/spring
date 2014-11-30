package spring05.user.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import spring05.user.dao.UserDao;
import spring05.user.vo.Levels;
import spring05.user.vo.User;

public class UserService {
	private static final Logger logger = LoggerFactory
			.getLogger(UserService.class);

	@Autowired
	UserDao userDao;

	@Autowired
	PlatformTransactionManager transactionManager;

	protected static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	protected static final int MIN_RECOMMEND_FOR_GOLD = 30;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEmail(user);
	}

	public boolean canUpgradeLevel(User user) {
		Levels currentLevel = user.getLevels();
		switch (currentLevel) {
		case BASIC:
			return (user.getLevels() == Levels.BASIC && user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
		case SILVER:
			return (user.getLevels() == Levels.SILVER && user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
		case GOLD:
			return false;
		default:
			throw new IllegalStateException("Unknown Level : " + currentLevel);
		}
	}

	public void upgradeLevels() throws Exception {
		// 트랜잭션 시작
		TransactionStatus status = transactionManager
				.getTransaction(new DefaultTransactionDefinition());

		try {
			List<User> users = userDao.getAll();
			for (User user : users) {
				if (canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			transactionManager.commit(status);
			logger.debug("******* 작업이 성공적으로 DB에 반영되었습니다. *******");
		} catch (Exception e) {
			transactionManager.rollback(status);
			logger.debug("******* 작업 도중 문제가 생겨서 취소 되었습니다. *******");
			throw e;
		}
	}

	public void add(User user) {
		if (user.getLevels() == null)
			user.setLevels(Levels.BASIC);
		userDao.add(user);
	}

	private void sendUpgradeEmail(User user) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.ksug.org");
		Session s = Session.getInstance(props, null);

		MimeMessage message = new MimeMessage(s);
		try {
			message.setFrom(new InternetAddress("useradmin@ksug.org"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					user.getEmail()));
			message.setSubject("Upgrade 안내");
			message.setText("사용자 등급이 " + user.getLevels().name()
					+ "로 업그레이드 되었습니다.");

			Transport.send(message);
		} catch (AddressException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}/*
		 * catch(UnsupportedEncodingException e){ throw new RuntimeException(e);
		 * }
		 */
	}
}
