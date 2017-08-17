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
package org.androidpn.server.console.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


/** 
 * A controller class to process the notification related requests.  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationController extends MultiActionController {

    private NotificationManager notificationManager;

    public NotificationController() {
        notificationManager = new NotificationManager();
    }

    public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        // mav.addObject("list", null);
        mav.setViewName("notification/form");
        return mav;
    }

    public ModelAndView send(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String broadcast = null;
        String username = null;
        String alias = null;
        String tag = null;
        String title = null;
        String message = null;
        String uri = null;
        String imageUrl=null;

        String apiKey = Config.getString("apiKey", "");
        logger.debug("apiKey=" + apiKey);

        DiskFileItemFactory factory=new DiskFileItemFactory();
        ServletFileUpload fileUpload=new ServletFileUpload(factory);
        List<FileItem> fileItems=fileUpload.parseRequest(request);
        
        for (FileItem fileItem : fileItems) {
			if ("broadcast".equals(fileItem.getFieldName())) {
				broadcast=fileItem.getString("utf-8");
			}else if("username".equals(fileItem.getFieldName())){
				username=fileItem.getString("utf-8");
			}else if("alias".equals(fileItem.getFieldName())){
				alias=fileItem.getString("utf-8");
			}else if("tag".equals(fileItem.getFieldName())){
				tag=fileItem.getString("utf-8");
			}else if("message".equals(fileItem.getFieldName())){
				message=fileItem.getString("utf-8");
			}else if("title".equals(fileItem.getFieldName())){
				title=fileItem.getString("utf-8");
			}else if("uri".equals(fileItem.getFieldName())){
				uri=fileItem.getString("utf-8");
			}else if("image".equals(fileItem.getFieldName())){
				imageUrl=uploadImage(request, fileItem);
			}
		}
        if (broadcast.equals("0")) {
            notificationManager.sendBroadcast(apiKey, title, message, uri,imageUrl,false);
        } else if(broadcast.equals("1")){
            notificationManager.sendNotifcationToUser(apiKey, username, title,message, uri,imageUrl,true);
        }else if(broadcast.equals("2")){
			notificationManager.sendNotificationByAlias(apiKey, alias, title, message, uri,imageUrl, false);
		}else if(broadcast.equals("3")){
			notificationManager.sendNotificationByTag(apiKey, tag, title, message, uri,imageUrl, false);
		}

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:notification.do");
        return mav;
    }

    public String uploadImage(HttpServletRequest request,FileItem fileItem) throws Exception{
    	String uploadPath=getServletContext().getRealPath("/upload");
    	File file=new File(uploadPath);
    	if (!file.exists()) {
			file.mkdirs();
		}
    	
    	if (fileItem !=null&&fileItem.getContentType().startsWith("image")) {
			String suffix=fileItem.getName().substring(fileItem.getName().indexOf("."));
			String fileName=System.currentTimeMillis()+suffix;
			InputStream in=fileItem.getInputStream();
			FileOutputStream fos=new FileOutputStream(uploadPath+"/"+fileName);
			byte[] bytes=new byte[1024];
			int len = 0;
			while((len=in.read(bytes))>0){
				fos.write(bytes,0,len);
				fos.flush();//清空缓存区，这样使得读取的数据不会覆盖而是会接着上一次的读取
			}
			fos.close();
			in.close();
			
//			String serverName=request.getServerName();
			int serverPort=request.getServerPort();
			
			String imageUrl="http://192.168.0.103:"+serverPort+"/upload/"+fileName;
			System.out.println("imageUrl--------->"+imageUrl);
			return imageUrl;
		}
    	
    	return "";
    }
}
