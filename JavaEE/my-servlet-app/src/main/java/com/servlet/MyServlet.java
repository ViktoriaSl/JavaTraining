package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet("mypage")
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("ThreadsList", Thread.getAllStackTraces());
        req.setAttribute("name", "MyServletApp");

        Map<String, String> headersList = new HashMap<>();
        Enumeration<String> headerNames = req.getHeaderNames();
        String headerName;
        while (headerNames.hasMoreElements()) {
            headerName = headerNames.nextElement();
            Enumeration<String> headers = req.getHeaders(headerName);
            while (headers.hasMoreElements()) {
                String headerValue = headers.nextElement();
                headersList.put(headerName, headerValue);
            }
            req.setAttribute("headerNames", headersList);
        }
        req.getRequestDispatcher("/mypage.jsp").forward(req, resp);
    }
}