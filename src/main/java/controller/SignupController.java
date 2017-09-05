package controller;

import dao.UserDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignupController extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req.getParameter("login"), req.getParameter("password"),
                                                req.getParameter("email"));

        boolean signedUp = userDao.persist(user);
        resp.getWriter().write(String.valueOf(signedUp));
    }
}
