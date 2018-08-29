package com.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Commentinfo;
import com.bean.Userinfo;
import com.service.ArticleService;
import com.service.ArticleServiceImpl;
import com.service.CommentService;
import com.service.CommentServiceImpl;
import com.util.TimeUtil;

/**
 * Servlet implementation class ArticleInfoServlet
 */
@WebServlet("/articleinfo")
public class ArticleInfoServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//����
		String artid=request.getParameter("artid");
		int id=Integer.parseInt(artid);
		ArticleService as=new ArticleServiceImpl();
		Map<String,Object> map=as.getArticleById(id);
		//ת��һ��ʱ��
		map.put("releasetime", TimeUtil.calculateTime((Date)map.get("releasetime")));
		request.setAttribute("detail_map", map);
		//��ȡ������ӵ�ȫ������
		CommentService comment=new CommentServiceImpl();
		List<Map<String,Object>> list=comment.getCommentById(id);
		for(Map<String,Object> listmap:list) {
			listmap.put("remarktime", TimeUtil.calculateTime((Date)listmap.get("remarktime")));
		}
		request.setAttribute("remarkinfo", list);
		 //���ر�������
		 CommentService cm=new CommentServiceImpl();
		 List<Map<String,Object>> weeklist=cm.getThisWeekComment();
		 request.setAttribute("thisweek", weeklist);
		request.getRequestDispatcher("/jsp/replypage.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//�ظ����ӣ��ж��Ƿ��¼״̬
		Userinfo user =(Userinfo)request.getSession().getAttribute("userinfo");
		if(user==null) {
			response.sendRedirect(request.getContextPath()+"/jsp/loginpage.jsp");
		}
		String userid=request.getParameter("userid");
		String artid=request.getParameter("artid");
		String replycontent=request.getParameter("replycontent");
		Commentinfo comminfo=new Commentinfo();
		comminfo.setArtorcommid(Integer.parseInt(artid));
		comminfo.setUserid(Integer.valueOf(userid));
		comminfo.setContent(replycontent);
		
		CommentService cs=new CommentServiceImpl();
		int num=cs.addNewReply(comminfo);
		response.getWriter().println(num);
	}

}
