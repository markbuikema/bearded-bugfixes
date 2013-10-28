/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.model;

/**
 * 
 * @author Christian, Mark
 */
public class SaxRequest {
	private final String method;
	private final long date;
	private final String url;
	private final String content;

	public SaxRequest(String method, String url, String content, long date) {
		this.method = method;
		this.url = url;
		this.content = content;
		this.date = date;
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return method + "," + url + "," + content;
	}

}
