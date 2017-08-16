package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.Notification;

public interface NotificationServer {
	
    void saveNotification(Notification notification);
	
	List<Notification> findNotificationsByUserName(String username);
	
	void deleteNotification(Notification notification);
	
	void deleteNotificationByUUID(String uuid);
	
//	String findUUIDByUsername(String username);
}
