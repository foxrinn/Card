package org.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dto.ResponseResult;
import org.model.Category;
import org.model.User;
import org.repository.CategoryRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category")
public class CategoryServlet extends HttpServlet {

    private CategoryRepository categoryRepository = new CategoryRepository();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        if (req.getParameter("idUser") != null){
            try {
                int idUser = Integer.parseInt(req.getParameter("idUser"));
                List<Category> categories = this.categoryRepository.getCategoriesByUserId(idUser);
                if (categories.size() != 0){
                    objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(true, categories));
                } else {
                    resp.setStatus(400);
                    objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "Categories not found"));
                }
            } catch (NumberFormatException e) {
                resp.setStatus(400);
                objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "Incorrect data"));
            }
        } else if (req.getParameter("id") != null) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                Category category = this.categoryRepository.getById(id);
                if (category == null) {
                    objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "Category not found"));
                }
                objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(true, category));
            } catch (NumberFormatException e) {
                resp.setStatus(400);
                objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "Incorrect data"));
            }
        } else {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), new ResponseResult<>(false, "User or Category id expected"));
        }
    }
}
