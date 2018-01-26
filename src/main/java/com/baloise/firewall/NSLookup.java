package com.baloise.firewall;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class NSLookup implements Function<String, NSLookup.LookupResult>{

	public static class LookupResult  implements Comparable<LookupResult>{
		private String stringRep;

		public LookupResult(String name, String address) {
			this.name = name;
			this.address = address;
			stringRep = "LookupResult [name=" + name + ", address=" + address + "]";
		}
		
		public final String name, address;

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
		public int compareTo(LookupResult o) {
			return stringRep.compareTo(o.stringRep);
		}

		
	}
	
	Map<String, LookupResult> cache = new HashMap<>();
	@Override
	public LookupResult apply(String query) {
		LookupResult res = cache.get(query);
		if(res == null) {
			try {
				res = nslookup(query);
				cache.put(query, res);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return res;
	}
	
	public static LookupResult nslookup(String query) throws IOException {
		Process proc =  new ProcessBuilder("nslookup", query).start();
		String address= "", name = "", lastToken = "";
		try( Scanner sc = new Scanner(proc.getInputStream())){
			while(sc.hasNext()) {
				String token = sc.next();
				if("Address:".equalsIgnoreCase(lastToken)) {
					address = token;
				} else if("Name:".equalsIgnoreCase(lastToken)) {
					name = token;
				}
			    lastToken = token;
			}
		}
		return new LookupResult(name, address );
	}

}
