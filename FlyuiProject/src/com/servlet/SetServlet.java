package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.bean.Userinfo;
import com.service.UserInfoService;
import com.service.UserInfoServiceImpl;

/**
 * Servlet implementation class SetServlet
 */
@WebServlet("/setservlet")
@MultipartConfig
public class SetServlet extends HttpServlet {
	
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//��ȡ�洢·��
		String savepath = request.getServletContext().getRealPath("/upload");
		// ��Ϊ���ǵķ������ϲ�ȷ���Ƿ���upload������
		File file = new File(savepath);
		if (!file.exists()) {
			file.mkdirs();
		}
		// ��ȡ�ϴ��ļ�����
		Collection<Part> parts = request.getParts();
		String filepath="";
		if (file != null) {	
			for (Part part : parts) {
				System.out.println(part);
				if(part.getContentType()!=null) {
					// ��Ϊ����part��ȡ�İ���username��password���������ǵ�����Ϊ�գ�������Ҫ�ų�����
					if (part.getContentType() != null) {
					// Servlet3.0��multipart/form-data��post�������װ��Part��ͨ��Part���ļ��ϴ�������
					// Servlet3û���ṩֱ�ӻ�ȡ�ļ����ķ�������Ҫ������ͷ�н�������
					// ��ȡ����ͷ������ͷ�ĸ�ʽ��form-data;name="file";filenme="*******.zip"
					String header = part.getHeader("content-disposition");
					
					// ������ͷ���ȡ�ļ���
					String filename = getFileName(header);
					
					// ���ļ�д��ָ��·��
					// File.separatorϵͳ����д��/��\\
					filepath=(new Date().getTime()+filename);
					part.write(savepath + File.separator + filepath );
				}
			 }
		}
			//����session
			Userinfo user=(Userinfo)request.getSession().getAttribute("userinfo");
			user.setHead_url(filepath);
			request.getSession().setAttribute("userinfo", user);
			//�������ݿ�
			UserInfoService us=new UserInfoServiceImpl();
			us.updateUserinfo(user);
			PrintWriter out = response.getWriter();
			out.println(request.getContextPath()+"/upload/"+filepath);
			out.flush();
			out.close();
		}
	}
	private String getFileName(String header) {
				/**
				 * String[] tempArr1 = header.split(";");����ִ����֮���ڲ�ͬ��������£�tempArr1���������������������
				 * �������google������£�tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
				 * IE������£�tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
				 */
				String[] tempArr1=header.split(";");
				for(String str:tempArr1) {
					System.out.println(str);
				}
				/**
				 * �������google������£�tempArr2={filename,"snmp4j--api.zip"}
				 * IE������£�tempArr2={filename,"E:\snmp4j--api.zip"}
				 */
				String[] tempArr2=tempArr1[2].split("=");
				// ��ȡ�ļ��������ݸ����������д��
				String filename=tempArr2[1].substring(tempArr2[1].lastIndexOf("\\")+1).replaceAll("\"","");
				return filename;
			}
}
