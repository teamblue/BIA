package logic;

enum HTTPType {
	GET, POST, UNKNOWN
}

/**
 * Extract the header of an HTTP stream.
 */
public class HTTPHeader {
	private String host;

	public String getHost() {
		return host;
	}

	public HTTPHeader(String message) {
		String line[] = message.split("\n");
		String split[];

		for (int i = 0; i < line.length; i++) {
			split = line[i].split(" ");
			if (split.length > 1) {
				if (split[0].equals("Host:")) {
					host = split[1].trim();
				}
			}
		}
	}
}