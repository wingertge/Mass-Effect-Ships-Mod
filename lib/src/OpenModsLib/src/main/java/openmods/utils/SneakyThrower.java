package openmods.utils;

public class SneakyThrower {

	public static class DummyException extends RuntimeException {
		private static final long serialVersionUID = 2594806051360685738L;
	}

	public static class Thrower<T extends Throwable> {
		@SuppressWarnings("unchecked")
		public T sneakyThrow(Throwable exception) throws T {
			throw (T)exception;
		}
	}

	private static final Thrower<DummyException> HELPER = new Thrower<DummyException>();

	public static DummyException sneakyThrow(Throwable exception) {
		HELPER.sneakyThrow(exception);
		return null;
	}

}
