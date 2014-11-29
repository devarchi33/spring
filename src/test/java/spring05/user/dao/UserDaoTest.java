package spring05.user.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring05.user.util.TestUtil;
import spring05.user.vo.Levels;
import spring05.user.vo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/context/applicationContext.xml")
public class UserDaoTest {

	private static final Logger logger = LoggerFactory
			.getLogger(UserDaoTest.class);

	@Autowired
	ApplicationContext ctx;

	@Autowired
	UserDao userDao;

	@Autowired
	TestUtil testUtil;

	List<User> users;
	User[] userArray = {
			new User("skyfly33", "이동훈", "fighting!", Levels.BASIC, 1, 0),
			new User("imfly7", "이현규", "fighting!@", Levels.SILVER, 50, 10),
			new User("iruentech", "이루엔", "fighting!@#", Levels.SILVER, 100, 30),
			new User("toby", "이일민", "fighting!@#$", Levels.GOLD, 200, 50) };

	@Before
	public void setUp() {
		ctx = new GenericXmlApplicationContext("context/applicationContext.xml");

		testUtil.init();

		users = new ArrayList<User>();

		// 배열을 리스트에 등록
		for (User user : userArray) {
			users.add(user);
		}

		logger.debug("******* 샘플이 테스트 리스트에 등록 되었습니다. *******");

	}

	@Test
	public void addAndGet() {
		logger.debug("======= 등록, 조회 테스트 =======");

		User user1 = users.get(0);
		userDao.add(user1);
		User userGet1 = userDao.get(user1.getId());

		testUtil.checkSameUser(user1, userGet1);
		testUtil.printUserInfo(user1);
		testUtil.printUserInfo(userGet1);
		logger.debug("******* 등록, 조회 테스트 성공 *******");
		
		System.out.println();
		System.out.println();
	}

	@Test
	public void getAll() {
		logger.debug("======= 전체조회 테스트 =======");

		for (User user : users) {
			userDao.add(user);
		}

		User[] userGetArray = { new User(), new User(), new User(), new User() };

		for (int i = 0; i < userGetArray.length; i++) {
			userGetArray[i] = userDao.get(users.get(i).getId());
			testUtil.checkSameUser(userGetArray[i], users.get(i));
			testUtil.printUserInfo(userGetArray[i]);
			testUtil.printUserInfo(users.get(i));
		}
	
		System.out.println();
		System.out.println();
	}

}
