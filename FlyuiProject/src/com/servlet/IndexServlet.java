package com.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Userinfo;
import com.service.ArticleService;
import com.service.ArticleServiceImpl;
import com.service.CommentService;
import com.service.CommentServiceImpl;
import com.service.UserInfoService;
import com.service.UserInfoServiceImpl;
import com.util.TimeUtil;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/IndexServlet")
public class IndexServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 ArticleService as=new ArticleServiceImpl();
		 List<Map<String,Object>> commentlist=as.getCommentAticle();
		 //��ʽʱ��
		 for(Map<String,Object> map:commentlist) {
			 String s=TimeUtil.calculateTime((Date)map.get("releasetime")); 
				map.put("releasetime",s);
			}
		 List<Map<String,Object>> goodlist=as.getGoodAticle();
		//��ʽʱ�� 
		 for(Map<String,Object> map:goodlist) {
			 String s=TimeUtil.calculateTime((Date)map.get("releasetime")); 
				map.put("releasetime",s);
			}
		 UserInfoService us=new UserInfoServiceImpl();
		 List<Map<String,Object>> userlist=us.getUserReplyNum();
		 
		 //������ͨ����
		 request.setAttribute("commentlist", commentlist);
		 //�����ö�����
		 request.setAttribute("goodlist", goodlist);
		 //���ػ����ܰ�
		 request.setAttribute("userlist", userlist);
		 //���ر�������
		 CommentService cm=new CommentServiceImpl();
		 List<Map<String,Object>> weeklist=cm.getThisWeekComment();
		 request.setAttribute("thisweek", weeklist);
		 request.getRequestDispatcher("/jsp/loginsuccess.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid=request.getParameter("userid");
		Userinfo user=(Userinfo)request.getSession().getAttribute("userinfo");
		
		user.setSignintime(new Date());
		UserInfoService us=new UserInfoServiceImpl();
		int num=us.updateSignIn(user);
		request.getSession().setAttribute("userinfo", user);
		response.getWriter().print(num);
	}
}
