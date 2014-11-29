package spring05.user.service;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import spring05.user.dao.UserDao;
import spring05.user.vo.Levels;
import spring05.user.vo.User;

public class UserService {
	private static final Logger logger = LoggerFactory
			.getLogger(UserService.class);

	@Autowired
	SimpleDriverDataSource dataSource;

	@Autowired
	UserDao userDao;

	protected static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	protected static final int MIN_RECOMMEND_FOR_GOLD = 30;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setDataSource(SimpleDriverDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
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
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

		//트랜잭션 시작
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
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
		} finally {
			DataSourceUtils.releaseConnection(c, dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
	}

	public void add(User user) {
		if (user.getLevels() == null)
			user.setLevels(Levels.BASIC);
		userDao.add(user);
	}
}
