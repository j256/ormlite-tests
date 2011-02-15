package com.j256.ormlite.test.db;

import com.j256.ormlite.db.H2DatabaseTypeTest;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class H2ConnectTest extends H2DatabaseTypeTest {

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}
}
