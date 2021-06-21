package com.xadmin.NoteTaker.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xadmin.NoteTaker.bean.userbean;
import com.xadmin.NoteTaker.dao.userdao;

//This servlet acts as a page controller for the application, handling all
// requests from the user.
@WebServlet("/")//call all the time the same servlet
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private userdao userDAO;
	
	public void init() throws ServletException {
		userDAO=new userdao();
	}
      
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//we write all the implementations to handle all the actions
		
		String action=request.getServletPath();//taking action from req method by calling get servlet method from req object and storing it in an action variable
		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertUser(request, response);
				break;
			case "/delete":
				deleteUser(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateUser(request, response);
				break;
			default:
				listUser(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
		
	
}

	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<userbean> listUser = userDAO.selectAllUsers();//fetch useers
		request.setAttribute("listUser", listUser);//store it in listuser key
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");//show it in owr jsp page
		dispatcher.forward(request, response);
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));//taking name,email from jsp page
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");

		userbean book = new userbean(name, id, email, country);
		userDAO.updateUser(book);//calling updateuser method call to the dao class
		response.sendRedirect("list");//it will handle by default section of switch case
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		userbean existingUser = userDAO.selectUser(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");//to forward request to jsp page
		request.setAttribute("user", existingUser);//set existing user object to the user key
		dispatcher.forward(request, response);

	}


	private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));//taking id from get  parameter and convertin it to int
		userDAO.deleteUser(id);
		response.sendRedirect("list");

	}
	

	private void insertUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		userbean newUser = new userbean(name, email, country);//user bean class constructor
		
		userDAO.insertUser(newUser);
		response.sendRedirect("list");//SEnding lsit
	}
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);//forward req to userform.jsp
		
	}
}
