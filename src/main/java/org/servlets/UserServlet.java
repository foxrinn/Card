package org.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dto.ResponseResult;
import org.model.User;
import org.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserRepository userRepository = new UserRepository();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        if (req.getParameter("id") != null) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                User user = this.userRepository.getById(id);
                if (user == null) {
                    objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "User not found"));
                }
                objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(true, user));
            } catch (NumberFormatException e) {
                resp.setStatus(400);
                objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "Incorrect data"));
            }
        } else {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "User id expected"));
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        if (req.getParameter("id") != null) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                User deleted = this.userRepository.getById(id);
                if (deleted == null){
                    resp.setStatus(400);
                    objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "User not found"));
                    return;
                }
                if (!this.userRepository.delete(deleted)) {
                    resp.setStatus(400);
                    objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "User not found"));
                } else {
                    objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(true, deleted));
                }
            } catch (Exception e) {
                resp.setStatus(400);
                objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "Incorrect data"));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        try(BufferedReader bufferedReader = req.getReader()) {
            User user = objectMapper.readValue(bufferedReader, User.class);
            int id = user.getId();
            User userDB = this.userRepository.getById(id);
            //TODO сделать проверку не на объект а только на аргументы логина и пароля
            if (user.getLogin().equals(userDB.getLogin()) && user.getPassword().equals(userDB.getPassword())) {
                objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(true, user));
            } else {
                objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "Incorrect login/password"));
            }
        } catch (Exception e) {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "Incorrect data"));
        }
    }
}
