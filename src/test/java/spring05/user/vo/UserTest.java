package spring05.user.vo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/context/applicationContext.xml")
public class UserTest {

	User user;

	@Before
	public void setUp() {
		user = new User();
	}

	@Test
	public void upgradeLevel() {
		Levels[] levels = Levels.values();
		for (Levels level : levels) {
			if (level.nextLevel() == null)
				continue;
			user.setLevels(level);
			user.upgradeLevel();
			assertThat(user.getLevels(), is(level.nextLevel()));
		}
		
		System.out.println();
		System.out.println();
	}

	@Test(expected = IllegalStateException.class)
	public void cannotUpgradeLevel() {
		Levels[] levels = Levels.values();
		for (Levels level : levels) {
			if (level.nextLevel() != null)
				continue;
			user.setLevels(level);
			user.upgradeLevel();
		}
	
		System.out.println();
		System.out.println();
	}

}
