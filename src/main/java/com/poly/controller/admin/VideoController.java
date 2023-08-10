package com.poly.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poly.constant.SessionAttr;
import com.poly.entity.User;
import com.poly.entity.Video;
import com.poly.service.VideoService;
import com.poly.service.impl.VideoServiceImpl;

@WebServlet(urlPatterns = {"/admin/video"}, name="VideoControllerOfAdmin")
public class VideoController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private VideoService videoService = new VideoServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		
		if(currentUser != null && currentUser.getIsAdmin() == true) {
			String action = request.getParameter("action");
			switch (action) {
			case "view": 
				doGetOverView(request, response);
				break;
			case "delete": 
				doGetDelete(request, response);
				break;
			case "add": 
				request.setAttribute("isEdit", false);
				doGetAdd(request, response);
				break;
			case "edit":
				request.setAttribute("isEdit", true);
				doGetEdit(request, response);
				break;
			}
		} else {
			response.sendRedirect("index");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		
		if(currentUser != null && currentUser.getIsAdmin() == true) {
			String action = request.getParameter("action");
			switch (action) {
			case "add": 
				doPostAdd(request, response);
				break;
			case "edit": 
				doPostEdit(request, response);
				break;
			}
			
		} else {
			response.sendRedirect("index");
		}
	}
	
	//localhost:8080/ASM-Java4/admin/video?action=view
	protected void doGetOverView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Video> videos = videoService.findAll();
		req.setAttribute("videos", videos);
		req.getRequestDispatcher("/views/admin/video-overview.jsp").forward(req, resp);
	}
	//localhost:8080/ASM-Java4/admin/video?action=edit&edit={href}
	protected void doGetEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String href = req.getParameter("href");
		Video video = videoService.findByHref(href);
		req.setAttribute("video", video);
		req.getRequestDispatcher("/views/admin/video-edit.jsp").forward(req, resp);
	}
	
	protected void doPostEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		String href = req.getParameter("newHref");
		String poster = req.getParameter("poster");
		String hrefOrigin = req.getParameter("hrefOrigin");
		
		Video video = videoService.findByHref(hrefOrigin);
		video.setTitle(title);
		video.setDescription(description);
		video.setHref(href);
		video.setPoster(poster);
		
		Video videoReturn = videoService.update(video);
		System.out.println("Video return: " + videoReturn);
		if(videoReturn != null) {
			resp.setStatus(204);
		} else { 
			resp.setStatus(400);
		}
	}
	//localhost:8080/ASM-Java4/admin/video?action=add
	protected void doGetAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/admin/video-edit.jsp").forward(req, resp);
	}
	
	protected void doPostAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		String href = req.getParameter("href");
		String poster = req.getParameter("poster");
		
		Video video = new Video();
		video.setTitle(title);
		video.setDescription(description);
		video.setHref(href);
		video.setPoster(poster);
		
		Video videoReturn = videoService.create(video);
		if(videoReturn != null) {
			resp.setStatus(204);
		} else { 
			resp.setStatus(400);
		}
	}
	//localhost:8080/ASM-Java4/admin/video?action=delete&href={href}
	protected void doGetDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String href = req.getParameter("href");
		Video videoDeleted = videoService.delete(href);
		if(videoDeleted != null) {
			resp.setStatus(204);
		} else { 
			resp.setStatus(400);
		}
		List<Video> videos = videoService.findAll();
		req.setAttribute("videos", videos);
		req.getRequestDispatcher("/views/admin/video-overview.jsp").forward(req, resp);
	}
}
