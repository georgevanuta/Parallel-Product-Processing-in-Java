public class Misc {
	public static void exitIf(
			final Boolean COND,
			final String MSG
	) {

		if (COND) {
			System.out.println(MSG);
			System.exit(-1);
		}
	}
}
