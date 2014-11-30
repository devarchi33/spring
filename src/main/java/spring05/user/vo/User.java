package spring05.user.vo;

public class User {

	private String id;
	private String name;
	private String password;
	private Levels levels;
	private int login;
	private int recommend;
	private String email;

	public User() {
	}

	public User(String id, String name, String password, Levels levels,
			int login, int recommend, String email) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.levels = levels;
		this.login = login;
		this.recommend = recommend;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Levels getLevels() {
		return levels;
	}

	public void setLevels(Levels levels) {
		this.levels = levels;
	}

	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void upgradeLevel() {
		Levels nextLevel = this.levels.nextLevel();
		if (nextLevel == null) {
			throw new IllegalStateException(this.levels + " can't upgrade.");
		}
		this.levels = nextLevel;
	}
}
