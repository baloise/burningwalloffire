package com.baloise.firewall;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
		public void testParseEnvs() throws Exception {
			Parser.parseEnvs("svx-ch-tcdvgadgt(#t/i/a/p)00(*11/22/33)");
			Parser.parseEnvs("svx-ch-tcdvgadgZ00(*1/2)");
			Parser.parseEnvs("svx-ch-tcdvgadgt(#t/i/a/p)009");
			Parser.parseEnvs("svx-ch-tcdvgadgt123");
		}

}
