package com.poly.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.constant.SessionAttr;
import com.poly.dto.UserDto;
import com.poly.dto.VideoLikedInfo;
import com.poly.entity.User;
import com.poly.service.impl.StatsServiceImpl;
import com.poly.service.impl.UserServiceImpl;

@WebServlet(urlPatterns = { "/admin", "/admin/favourites" }, name = "HomeControllerAdmin")
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private com.poly.service.UserService userService = new UserServiceImpl();
	
	private com.poly.service.StatsService statsService = new StatsServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		System.out.println("currentUser" + currentUser);
		if (currentUser != null && currentUser.getIsAdmin() == true) {
			String path = request.getServletPath();
			switch (path) {
			case "/admin":
				doGetHome(request, response);
				break;
			case "/admin/favourites":
				doGetFavourites(request, response);
				break;
			}
		} else {
			response.sendRedirect("index");
		}

	}

	protected void doGetHome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<VideoLikedInfo> videos = statsService.findVideoLikedInfo();
		request.setAttribute("videos", videos);
		request.getRequestDispatcher("/views/admin/home.jsp").forward(request, response);
	}

	protected void doGetFavourites(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("applycation/json");
		String videoHref = request.getParameter("href");
		List<UserDto> users = userService.findUsersLikedVideoByVideoHref(videoHref);
		if(users.isEmpty()) {
			response.setStatus(400);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			String dataResponse = mapper.writeValueAsString(users);
			response.setStatus(200);
			out.print(dataResponse);
			out.flush();
		}
	}
}
