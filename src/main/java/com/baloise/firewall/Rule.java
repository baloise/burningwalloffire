package com.baloise.firewall;

import com.baloise.firewall.NSLookup.LookupResult;

/**
 * @author b028178
 *
 */
public class Rule implements Comparable<Rule>{
	final Env env;
	final LookupResult source;
	final String protocol;
	final LookupResult target;
	final int port;
	private final String stringRep;
	private final String compareRep;
	public Rule(Env env, LookupResult source, String protocol, LookupResult target, int port) {
		this.env = env;
		this.source = source;
		this.protocol = protocol;
		this.target = target;
		this.port = port;
		stringRep = "Rule [env=" + env + ", source=" + source + ", protocol=" + protocol + ", target=" + target + ", port="
				+ port + "]";
		compareRep = "Rule [env=" + env.ordinal() + ", source=" + source + ", protocol=" + protocol + ", target=" + target + ", port="
				+ port + "]";
	}
	
	@Override
	public String toString() {
		return stringRep;
	}

	@Override
	public int hashCode() {
		return stringRep.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return stringRep.equals(""+obj);
	}

	@Override
	public int compareTo(Rule o) {
		return compareRep.compareTo(o.compareRep);
	}
	
	
}
