package spring05.user.vo;

public enum Levels {

	GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

	private int value;

	private Levels nextLevel;

	Levels(int value, Levels nextLevel) {
		this.value = value;
		this.nextLevel = nextLevel;
	}

	public int intValue() {
		return this.value;
	}

	public Levels nextLevel() {
		return this.nextLevel;
	}

	public static Levels valueOf(int value) {
		switch (value) {
		case 1:
			return BASIC;
		case 2:
			return SILVER;
		case 3:
			return GOLD;
		default:
			throw new AssertionError("Unknown Level : " + value);
		}
	}
}
