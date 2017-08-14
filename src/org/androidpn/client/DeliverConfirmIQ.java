package org.androidpn.client;

import org.jivesoftware.smack.packet.IQ;
/**
 * 用于向服务器发送确认消息的IQ
 * @author gzg
 *
 */
public class DeliverConfirmIQ extends IQ {

	 private String uuid;
	
	
	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	@Override
	public String getChildElementXML() {
		 StringBuilder buf = new StringBuilder();
	        buf.append("<").append("deliverconfirm").append(" xmlns=\"").append(
	                "androidpn:iq:deliverconfirm").append("\">");
	        if (uuid != null) {
	            buf.append("<uuid>").append(uuid).append("</uuid>");
	        }
	        buf.append("</").append("deliverconfirm").append("> ");
	        return buf.toString();
	}

}
