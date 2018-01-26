package com.baloise.firewall;

import static java.lang.Integer.parseInt;
import static java.util.EnumSet.allOf;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baloise.firewall.NSLookup.LookupResult;

public class Parser {
	
	Function<String, NSLookup.LookupResult> lookup;

	public Parser(Function<String, LookupResult> lookup) {
		this.lookup = lookup;
	}
	public Set<Rule> parse(String input) {
		return parse(input, lookup);
	}
	
	public static Set<Rule> parse(String input, Function<String, NSLookup.LookupResult> lookup) {
		String[] tokens = input.split("\\s*->\\s*");
		Envs sources = parseEnvs(tokens[0].trim());
		String protocol = tokens[1].trim();
		Envs ports = parseEnvs(tokens[2].trim());
		Envs targets = parseEnvs(tokens[3].trim());
		Set<Rule> rules = new TreeSet<>();
		allOf(Env.class).forEach(env -> {
			sources.of(env).forEach(source -> {
				targets.of(env).forEach(target -> {
					ports.of(env).forEach(port -> {
						rules.add(new Rule(env, lookup.apply(source), protocol, lookup.apply(target), parseInt(port)));
					});
				});
			});
		});
		
		return rules ;
	}

	private static Pattern envs = Pattern.compile("\\(#([^/]*?)/([^/]*?)/([^/]*?)/([^/]*?)\\)");
	private static Pattern multis = Pattern.compile("\\(\\*([^/]*?)(/([^/]*?))+\\)");
	
	public static Envs parseEnvs(final String inputs) {
		Envs ret = new Envs();
		for (String input : inputs.split("\\s*,\\s*")) {
			Matcher envsMatcher = envs.matcher(input);
			if(envsMatcher.find()) {
				allOf(Env.class).forEach(env -> {
					ret.of(env).add(input.replace(envsMatcher.group(), envsMatcher.group(env.ordinal()+1)));
				});
			} else {
				allOf(Env.class).forEach(env -> {
					ret.of(env).add(input);
				});
			}
		}
		
		allOf(Env.class).forEach(env -> {
			parseEnvs(ret.of(env));
		});
		return ret;
	}
	
	private static void parseEnvs(final Set<String> inputs) {
		Set<String> toRemove = new HashSet<>();
		Set<String> toAdd = new HashSet<>();
		inputs.forEach(input -> {
			Matcher multiMatcher = multis.matcher(input);
			if(multiMatcher.find()) {
				toRemove.add(input);
				String group = multiMatcher.group(0);
				for ( String multi : group.substring(2, group.length()-1).split("\\s*/\\s*")) {
					toAdd.add(input.replace(multiMatcher.group(), multi));
				}
			}
		});
		inputs.removeAll(toRemove);
		inputs.addAll(toAdd);
	}
}

