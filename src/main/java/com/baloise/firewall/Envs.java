package com.baloise.firewall;

import static java.util.EnumSet.allOf;

import java.util.EnumMap;
import java.util.Set;
import java.util.TreeSet;

public class Envs {
	private EnumMap<Env, Set<String>> perEnv = new EnumMap<>(Env.class);
	public Set<String> of(Env env) {
		Set<String> ret = perEnv.get(env);
		if(ret == null) {
			ret = new TreeSet<>();
			perEnv.put(env, ret);
		}
		return ret;
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		allOf(Env.class).forEach(env -> {
			ret.append(env);
			ret.append('\n');
			ret.append(of(env));
			ret.append('\n');
		});
		return ret.toString();
	}
}
