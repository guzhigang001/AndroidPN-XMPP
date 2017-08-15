package org.androidpn.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jivesoftware.smack.packet.IQ;
/**
 * 用于向服务器发送别名的IQ
 * @author gzg
 *
 */
public class SetTagsIQ extends IQ {

	private String username;

	private List<String> tagsList=new ArrayList<String>();

	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getTagsList() {
		return tagsList;
	}



	public void setTagsList(List<String> tagsList) {
		this.tagsList = tagsList;
	}
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<").append("settags").append(" xmlns=\"").append(
				"androidpn:iq:settags").append("\">");
		if (username != null) {
			buf.append("<username>").append(username).append("</username>");
		}
		if (tagsList!=null&&!tagsList.isEmpty()) {
			buf.append("<tags>");
			boolean needSepterate=false;
			for(String tag:tagsList){
				if (needSepterate) {
					buf.append(",");
				}
				buf.append(tag);
				needSepterate=true;
			}
			buf.append("</tags>");
		}
		buf.append("</").append("settags").append("> ");
		return buf.toString();
	}

}
