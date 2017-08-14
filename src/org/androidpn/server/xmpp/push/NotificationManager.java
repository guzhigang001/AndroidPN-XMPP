/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.xmpp.push;

import java.util.List;
import java.util.Random;

import org.androidpn.server.model.Notification;
import org.androidpn.server.model.User;
import org.androidpn.server.service.NotificationServer;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

/** 
 * This class is to manage sending the notifcations to the users.  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationManager {

	private static final String NOTIFICATION_NAMESPACE = "androidpn:iq:notification";

	private final Log log = LogFactory.getLog(getClass());

	private SessionManager sessionManager;

	private NotificationServer notificationServer;

	private UserService userService;

	/**
	 * Constructor.
	 */
	public NotificationManager() {
		sessionManager = SessionManager.getInstance();
		notificationServer=ServiceLocator.getNotificationService();
		userService=ServiceLocator.getUserService();
	}

	/**
	 * Broadcasts a newly created notification message to all connected users.
	 * 
	 * @param apiKey the API key
	 * @param title the title
	 * @param message the message details
	 * @param uri the uri
	 */
	public void sendBroadcast(String apiKey, String title, String message,
			String uri) {
		log.debug("sendBroadcast()...");
		List<User> userList=userService.getUsers();
		for (User user : userList) {
			Random random = new Random();
			String uuid = Integer.toHexString(random.nextInt());
			IQ notificationIQ = createNotificationIQ(uuid,apiKey, title, message, uri);
			ClientSession session=sessionManager.getSession(user.getUsername());
			if (session!=null&&session.getPresence().isAvailable()) {
				notificationIQ.setTo(session.getAddress());
				session.deliver(notificationIQ);
			}
			saveNotifcation(uuid,apiKey, user.getUsername(), title, message, uri);
		}
	}

	/**
	 * Sends a newly created notification message to the specific user.
	 * 
	 * @param apiKey the API key
	 * @param title the title
	 * @param message the message details
	 * @param uri the uri
	 */
	public void sendNotifcationToUser(String apiKey, String username,
			String title, String message, String uri,boolean shouldSave) {
		log.debug("sendNotifcationToUser()...");
		Random random = new Random();
		String uuid = Integer.toHexString(random.nextInt());
		IQ notificationIQ = createNotificationIQ(uuid,apiKey, title, message, uri);
		ClientSession session = sessionManager.getSession(username);
		if (session != null) {
			if (session.getPresence().isAvailable()) {
				notificationIQ.setTo(session.getAddress());
				session.deliver(notificationIQ);
			}
			//            else {
			//				saveNotifcation(uuid,apiKey, username, title, message, uri);
			//			}
		}
		try {
			User user=userService.getUserByUsername(username);
			if (user!=null&&shouldSave) {
				saveNotifcation(uuid,apiKey, username, title, message, uri);
			}
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendNotificationByAlias(String apiKey, String alias,
			String title, String message, String uri,boolean shouldSave){
		String username=sessionManager.getUsernameByAlias(alias);
		if (username!=null) {
			sendNotifcationToUser(apiKey, username, title, message, uri, shouldSave);
		}
		
		
	}

	public void saveNotifcation(String uuid,String apiKey, String username,
			String title, String message, String uri){
		Notification notification=new Notification();
		notification.setApiKey(apiKey);
		notification.setMessage(message);
		notification.setTitle(title);
		notification.setUri(uri);
		notification.setUsername(username);
		notification.setUuid(uuid);
		notificationServer.saveNotification(notification);

	}

	/**
	 * Creates a new notification IQ and returns it.
	 */
	private IQ createNotificationIQ(String id,String apiKey, String title,
			String message, String uri) {

		// String id = String.valueOf(System.currentTimeMillis());

		Element notification = DocumentHelper.createElement(QName.get(
				"notification", NOTIFICATION_NAMESPACE));
		notification.addElement("id").setText(id);
		notification.addElement("apiKey").setText(apiKey);
		notification.addElement("title").setText(title);
		notification.addElement("message").setText(message);
		notification.addElement("uri").setText(uri);

		IQ iq = new IQ();
		iq.setType(IQ.Type.set);
		iq.setChildElement(notification);

		return iq;
	}
}
