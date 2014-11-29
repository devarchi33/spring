package spring05.user.policy;

import static spring05.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static spring05.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

import org.springframework.beans.factory.annotation.Autowired;

import spring05.user.dao.UserDao;
import spring05.user.vo.Levels;
import spring05.user.vo.User;

public class LevelUpgradePolicyImpl implements LevelUpgradePolicy {
	
	@Autowired
	UserDao userDao;

	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
	@Override
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

	@Override
	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}

}
