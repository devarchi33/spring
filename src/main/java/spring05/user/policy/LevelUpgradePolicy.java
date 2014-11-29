package spring05.user.policy;

import spring05.user.vo.User;

public interface LevelUpgradePolicy {

	public boolean canUpgradeLevel(User user);
	public void upgradeLevel(User user);
}
