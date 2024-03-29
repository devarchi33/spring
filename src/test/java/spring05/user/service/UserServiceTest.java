package spring05.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static spring05.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static spring05.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import spring05.user.dao.UserDao;
import spring05.user.util.TestUtil;
import spring05.user.vo.Levels;
import spring05.user.vo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/context/applicationContext.xml")
public class UserServiceTest {
	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceTest.class);

	@Autowired
	UserDao userDao;

	@Autowired
	UserService userService;

	@Autowired
	TestUtil testUtil;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	MailSender mailSender;

	List<User> users;
	User[] userArray = {
			new User("skyfly33", "이동훈", "fighting!", Levels.BASIC, 1, 0,
					"imfly7@naver.com"),
			new User("imfly7", "이현규", "fighting!@", Levels.BASIC,
					MIN_LOGCOUNT_FOR_SILVER, 10, "imfly8@naver.com"),
			new User("iruentech", "이루엔", "fighting!@#", Levels.SILVER, 100,
					MIN_RECOMMEND_FOR_GOLD, "imfly9@naver.com"),
			new User("toby", "이일민", "fighting!@#$", Levels.SILVER, 200,
					MIN_RECOMMEND_FOR_GOLD + 20, "imfly10@naver.com"),
			new User("spring", "스프링", "fighting!@#$%", Levels.BASIC, 2, 0,
					"imfl11@naver.com") };

	@Before
	public void setUp() {
		testUtil.init();

		users = new ArrayList<User>();

		// 배열을 리스트에 등록
		for (User user : userArray) {
			users.add(user);
		}

		logger.debug("******* 샘플이 테스트 리스트에 등록 되었습니다. *******");

	}

	@Test
	@DirtiesContext
	public void upgradeLevel() throws Exception {
		logger.debug("======= 업그레이드 레벨 테스트 =======");
		for (User user : users) {
			userDao.add(user);
		}

		// 메일발송 결과를 테스트할 수 있도록 목 오브젝트를 만들어 UserService의 의존 객체로 주입.
		MockMailSender mockMailSender = new MockMailSender();
		userService.setMailSender(mockMailSender);

		userService.upgradeLevels();

		testUtil.checkLevelUpgraded(users.get(0), false);
		testUtil.checkLevelUpgraded(users.get(1), true);
		testUtil.checkLevelUpgraded(users.get(2), true);
		testUtil.checkLevelUpgraded(users.get(3), true);
		testUtil.checkLevelUpgraded(users.get(4), false);

		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(3));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(2).getEmail()));
		assertThat(request.get(2), is(users.get(3).getEmail()));

		/*
		 * users.get(0).setLogin(MIN_LOGCOUNT_FOR_SILVER);
		 * userDao.update(users.get(0)); userService.upgradeLevels();
		 * testUtil.checkLevelUpgraded(users.get(0), true);
		 * 
		 * users.get(1).setRecommend(MIN_RECOMMEND_FOR_GOLD);
		 * userDao.update(users.get(1)); userService.upgradeLevels();
		 * testUtil.checkLevelUpgraded(users.get(1), true);
		 */

		System.out.println();
		System.out.println();
	}

	@Test
	public void add() {
		logger.debug("======= 초기레벨 설정 테스트 =======");

		User userWithLevel = users.get(3);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevels(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithouUserRead = userDao.get(userWithoutLevel.getId());

		assertThat(userWithLevelRead.getLevels(), is(userWithLevel.getLevels()));
		assertThat(userWithouUserRead.getLevels(), is(Levels.BASIC));
		logger.debug("******* 초기 레벨 설정 테스트가 성공하였습니다. *******");

		System.out.println();
		System.out.println();
	}

	static class TestUserService extends UserService {
		private String id;

		private TestUserService(String id) {
			this.id = id;
		}

		public void upgradeLevel(User user) {
			if (user.getId().equals(this.id))
				throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}

	static class TestUserServiceException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8290726284653179608L;

	}

	@Test
	public void AllOrNothing() throws Exception {
		logger.debug("=======  트랜잭션 처리 테스트 =======");
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setTransactionManager(this.transactionManager);
		testUserService.setMailSender(this.mailSender);

		// userDao.deleteAll();

		for (User user : users) {
			userDao.add(user);
		}

		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {

		}

		testUtil.checkLevelUpgraded(users.get(1), false);

		System.out.println();
		System.out.println();
	}
}
