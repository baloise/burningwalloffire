package com.baloise.firewall;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.stream;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

public class BurningWallOfFire {
	
	static Env env = Env.PRD;
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		Parser parser = new Parser(new NSLookup());
		File file = new File("con-"+currentTimeMillis()+".html");
		PrintStream out = new PrintStream(new FileOutputStream(file));
		if(args.length == 0) {
			parser = new Parser(s -> new NSLookup.LookupResult(s, ""+currentTimeMillis()));
			args = new String [] {
					"servers(#t/i/a/p)00(*1/2) -> protocol HTTP ->  123(#5/7/9/3) -> (#test-/int-/acc-/)target, (#test-/int-/acc-/)some(*1/2)-machine.com\n"
							+"moreservers(#t/i/a/p)00(*1/2) -> protocol UDP -> 111222 ->  some(*22/11).at.com"
			};
			System.out.println(args[0]);
		} 
		out.println("<html><head><title>Firewall Rules</title><style type=\"text/css\">\r\n" + 
				"table, th, td {\r\n" + 
				"	vertical-align: top;\r\n" + 
				"	padding: 5px;\r\n" + 
				"	border: 1px solid black;\r\n" + 
				"	border-collapse: collapse;\r\n" + 
				"}\r\n" + 
				"</style></head><body>");
		out.println("<table>\r\n" + 
				"		<thead>\r\n" + 
				"			<tr>\r\n" + 
				"				<th rowspan=\"2\">Environment</th>\r\n" + 
				"				<th colspan=\"2\">Source</th>\r\n" + 
				"				<th rowspan=\"2\">Protocol</th>\r\n" + 
				"				<th colspan=\"3\">Target</th>\r\n" + 
				"			</tr>\r\n" + 
				"			<tr>\r\n" + 
				"				<th>Name</th>\r\n" + 
				"				<th>IP</th>\r\n" + 
				"				<th>Name</th>\r\n" + 
				"				<th>IP</th>\r\n" + 
				"				<th>Port</th>\r\n" + 
				"			</tr>\r\n" + 
				"		</thead>\r\n" + 
				"		<tbody>");
		stream(args[0].split("\\n")).map(parser::parse).flatMap(s -> s.stream()).
		sorted().
		forEach(rule -> {
			if(!rule.env.equals(env)) {
				env = rule.env;
			}
			out.println(toTr(rule));
		});
		out.println("	</tbody>\r\n" + 
				"	</table>");
		out.println("</body></html>");
		out.close();
		if (Desktop.isDesktopSupported()) {
		    Desktop.getDesktop().browse(file.toURI());
		}
	}

	private static String toTr(Rule rule) {
		return format("<tr>\r\n" + 
				"				<td>%s</td>\r\n" + 
				"				<td>%s</td>\r\n" + 
				"				<td>%s</td>\r\n" + 
				"				<td>%s</td>\r\n" + 
				"				<td>%s</td>\r\n" + 
				"				<td>%s</td>\r\n" + 
				"				<td>%s</td>\r\n" + 
				"			</tr>", 
				rule.env, 
				rule.source.name,
				rule.source.address,
				rule.protocol,
				rule.target.name,
				rule.target.address,
				rule.port
				);
	}

}