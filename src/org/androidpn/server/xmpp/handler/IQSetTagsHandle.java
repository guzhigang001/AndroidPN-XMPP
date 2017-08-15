package org.androidpn.server.xmpp.handler;

import java.util.List;

import org.androidpn.server.service.NotificationServer;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.handler.IQHandler;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.dom4j.Element;

import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

public class IQSetTagsHandle extends IQHandler {

	private static final String NAMESPACE = "androidpn:iq:settags";
	
	private SessionManager sessionManager;
	public IQSetTagsHandle(){
		sessionManager=SessionManager.getInstance();
	}
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ reply = null;

        ClientSession session = sessionManager.getSession(packet.getFrom());
        if (session == null) {
            log.error("Session not found for key " + packet.getFrom());
            reply = IQ.createResultIQ(packet);
            reply.setChildElement(packet.getChildElement().createCopy());
            reply.setError(PacketError.Condition.internal_server_error);
            return reply;
        }
        if (session.getStatus() == Session.STATUS_AUTHENTICATED) {//STATUS_AUTHENTICATED身份验证 只有登录成功的会话发的XMPP才会被接收 防止伪造请求
            if (IQ.Type.set.equals(packet.getType())) {
            	  Element element = packet.getChildElement();
            	  String username=element.elementText("username");
            	  String tagStr=element.elementText("tags");
            	  String[] tagsArray=tagStr.split(",");
            	 if (tagsArray!=null&&tagsArray.length>0) {
            		 for (String tag : tagsArray) {
            			 sessionManager.setUserTag(username, tag);
					}
            		 System.out.println("set username tags successfully");
				}
			}
        }
        
		return null;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}


	
}
