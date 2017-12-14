package tMethods;

/**
 * Like 2 useful methods to go with Enumerations
 *
 * @author Tallon Hodge
 * @see Enum
 */
public class TEnums {
	/**
	 * @return the enum value following e </br>
	 *         returns the first enum if e is the last
	 */
	public static Enum<?> nextEnum(final Enum<?> e) {
		return e.getDeclaringClass()
				.getEnumConstants()[e.ordinal() + 1 < e.getDeclaringClass().getEnumConstants().length ? e.ordinal() + 1
						: 0];
	}

	/**
	 * @return the enum value before e </br>
	 *         returns the last enum if e is the first
	 */
	public static Enum<?> prevEnum(final Enum<?> e) {
		return e.getDeclaringClass().getEnumConstants()[e.ordinal() > 0 ? e.ordinal() - 1
				: e.getDeclaringClass().getEnumConstants().length - 1];
	}

	/**
	 * @return a random enum from an array of enums
	 */
	public static Enum<?> randEnum(final Enum<?>[] e) {
		return e[(int) (Math.random() * e.length)];
	}
}
