package spring05.user.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import spring05.user.dao.UserDao;
import spring05.user.vo.User;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(TestUtil.class);

	@Autowired
	UserDao userDao;

	public void init() {
		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));
		logger.debug("******* 테스트 초기화 성공 *******");
	}

	public void checkCount(int count) {
		assertThat(userDao.getCount(), is(count));
		logger.debug("******* 조회수가 일치 합니다. *******");
	}

	public void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevels(), is(user2.getLevels()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
		logger.debug("******* 두 유저가 일치 합니다. *******");
	}

	public void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdate.getLevels(), is(user.getLevels().nextLevel()));
			logger.debug("******* 레벨이 " + user.getLevels() + "에서 "
					+ userUpdate.getLevels() + " 로 업그레이드 되었습니다. *******");
		} else {
			assertThat(userUpdate.getLevels(), is(user.getLevels()));
			logger.debug("******* 레벨이" + userUpdate.getLevels()
					+ " 상태를 유지하였습니다. *******");
		}
	}

	public void printUserInfo(User user) {
		logger.info("####### " + user.getName() + ", 유저에 대한 정보 입니다. #######");
		logger.info("User Id : " + user.getId());
		logger.info("User Name : " + user.getName());
		logger.info("User Password : " + user.getPassword());
		logger.info("User Levels : " + user.getLevels());
		logger.info("User Login : " + user.getLogin());
		logger.info("User recommend : " + user.getRecommend());
		System.out.println();
	}
}
