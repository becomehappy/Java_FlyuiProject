package com.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Userinfo;
import com.service.UserInfoService;
import com.service.UserInfoServiceImpl;
import com.util.MD5;
import com.util.MailUtil;

/**
 * Servlet implementation class RegServlet
 */
@WebServlet("/reg")
public class RegServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//ͨ��ajax���������
		String email=request.getParameter("email");
		//System.out.println(email);
		UserInfoService us=new UserInfoServiceImpl();
		int num=us.checkEmail(email);
		response.getWriter().println(num);
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email=request.getParameter("email");
		
		String name=request.getParameter("nickname");
		String password =request.getParameter("password");
		//��ȡ���䣬������ʱ�������Ӧ����
		//��long��ת��ΪString
		String emailcode=String.valueOf(new Date().getTime());
	    try {
	        // date.getTime()+""����ʱ�����ΪУ���뷢�͸��Է�����
	        MailUtil.sendActiveMail(email, emailcode);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		//����һ���û�������
		Userinfo user=new Userinfo();
		user.setEmail(email);
		user.setNickname(name);
		user.setPassword(MD5.MD5(password));
		user.setActivecode(emailcode);
		System.out.println("������֤��:"+emailcode);
		//System.out.println(user.getNickname()+","+user.getEmail());
		
		UserInfoService us=new UserInfoServiceImpl();
		us.addUserInfo(user);
		response.sendRedirect(request.getContextPath()+"/jsp/loginpage.jsp");
	}
}
