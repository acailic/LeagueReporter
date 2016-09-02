package pma.leaguereporter.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import pma.leaguereporter.util.L;

public class Connection {
	private boolean mCanPost;
	private int mTimeout;
	private HashMap<String, String> map = null;

	protected Connection() {}

	/**
	 * Return string result
	 * @param request request string
	 * @return string result
	 */
	public String request(String request) {
		if (request != null && !request.isEmpty()) return response(request);
		else return null;
	}

	private String response(String request) {
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(request);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(mCanPost ? "POST" : "GET");
			connection.setConnectTimeout(mTimeout > 0 ? mTimeout : 5000);
			if (map != null && !map.isEmpty()) {
				for (Map.Entry<String, String> header : map.entrySet()) {
					connection.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String response = buffer.toString();
			L.i(Connection.class, "(Success)request->" + request);
			L.i(Connection.class, "(Success)response->" + response);
			return response;
		} catch (IOException e) {
			L.e(Connection.class, e.toString());
		} finally {
			if (connection != null) connection.disconnect();
			try {
				if (reader != null) reader.close();
			} catch (IOException e) {
				L.e(Connection.class, e.toString());
			}
		}
		L.e(Connection.class, "(Error)request->" + request);
		return null;
	}

	protected void setPost(boolean canPost) {
		this.mCanPost = canPost;
	}

	protected void setTimeout(int timeout) {
		this.mTimeout = timeout;
	}

	protected void putHeader(String key, String value) {
		if (map == null) map = new HashMap<>();
		if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) map.put(key, value);
	}

	public static class Creator {

		private Connection connection = new Connection();

		/**
		 * Switch method for request, default <b>GET</b>
		 * @param canPost if must <b>POST</b> set true
		 */
		public Creator setPost(boolean canPost) {
			connection.setPost(canPost);
			return this;
		}

		/**
		 * Set timeout in milliseconds
		 * @param timeout timeout
		 */
		public Creator setTimeout(int timeout) {
			connection.setTimeout(timeout);
			return this;
		}

		/**
		 * Put headers to request
		 * @param key key
		 * @param value value
		 */
		public Creator putHeader(String key, String value) {
			connection.putHeader(key, value);
			return this;
		}

		/**
		 * Create object witch params
		 * @return object
		 */
		public Connection create() {
			return connection;
		}
	}

}
