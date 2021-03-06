/*
 * The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package io.github.benas.todolist.web.servlet.todo;

import io.github.todolist.core.domain.Todo;
import io.github.todolist.core.service.api.TodoService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import static io.github.benas.todolist.web.util.Views.ERROR_PAGE;

/**
 * Servlet that controls todo deletion.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */

@WebServlet(name = "DeleteTodoServlet", urlPatterns = "/todos/delete.do")
public class DeleteTodoServlet extends HttpServlet {

    private TodoService todoService;

    private ResourceBundle resourceBundle;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
        todoService = applicationContext.getBean(TodoService.class);
        resourceBundle = ResourceBundle.getBundle("todolist");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String id = request.getParameter("todoId");
        try {
            long todoId = Long.parseLong(id);
            Todo todo = todoService.getTodoById(todoId);
            if (todo != null) {
                todoService.remove(todo);
                request.getRequestDispatcher("/todos").forward(request, response);
            } else {
                redirectToErrorPage(request, response, id);
            }
        } catch (NumberFormatException e) {
            redirectToErrorPage(request, response, id);
        }
    }

    private void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response, String todoId) throws ServletException, IOException {
        request.setAttribute("error", MessageFormat.format(resourceBundle.getString("no.such.todo"), todoId));
        request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
    }

}
