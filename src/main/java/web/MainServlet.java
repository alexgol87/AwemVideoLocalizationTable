package web;

import util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MainServlet extends HttpServlet {

    private static Runnable task = null;
    private static Thread thread = null;
    private static String lastUpdateTimeVideo;
    private static String lastUpdateTimeBanner;
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ((thread.getState() == Thread.State.NEW || thread.getState() == Thread.State.TERMINATED) && req.getParameter("runUpdate").equals("yes")) {
            if (req.getParameter("creativeType").equals("video")) task = GoogleDriveSpider::new;
            else task = GoogleDriveBannerSpider::new;
            if (thread.getState() == Thread.State.TERMINATED) thread = new Thread(task);
            if (req.getParameter("updatePreview") != null) {
                if (req.getParameter("creativeType").equals("video")) DropboxApiUtil.startUpdateVideoPreview();
                else DropboxApiUtil.startUpdateBannerPreview();
            } else {
                DropboxApiUtil.stopUpdateVideoPreview();
                DropboxApiUtil.stopUpdateBannerPreview();
            }
            thread.start();
            req.setAttribute("lockUpdate", TRUE);
            req.setAttribute("tableReady", FALSE);
        } else if ((thread.getState() == Thread.State.NEW || thread.getState() == Thread.State.TERMINATED)) {
            req.setAttribute("lockUpdate", FALSE);
            req.setAttribute("tableReady", TRUE);
            req.setAttribute("execTime", GeneralUtil.execTime);
            req.setAttribute("videoErrors", GoogleDriveSpider.videoErrors.size());
            lastUpdateTimeVideo = GoogleDriveApiUtil.getModifiedTime(GoogleDriveApiUtil.buildSheetsApiClientService(), "1SC92tKYXQDqujUcvZVYMmmNiJp35Q1b22fKg2C7zeQI", "v");
            lastUpdateTimeBanner = GoogleDriveApiUtil.getModifiedTime(GoogleDriveApiUtil.buildSheetsApiClientService(), "1SC92tKYXQDqujUcvZVYMmmNiJp35Q1b22fKg2C7zeQI", "b");
        } else if ((thread.getState() == Thread.State.RUNNABLE)) {
            req.setAttribute("lockUpdate", TRUE);
            req.setAttribute("tableReady", FALSE);
        }
        req.setAttribute("lastUpdateTimeVideo", lastUpdateTimeVideo);
        req.setAttribute("lastUpdateTimeBanner", lastUpdateTimeBanner);
        req.getRequestDispatcher("main.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //if (req.getParameter("creativeType").equals("banner")) task = GoogleDriveBannerSpider::new;
        //else task = GoogleDriveSpider::new;
        if (thread == null) thread = new Thread(task);
        else if ((thread.getState() == Thread.State.RUNNABLE)) {
            req.setAttribute("lockUpdate", TRUE);
            req.setAttribute("tableReady", FALSE);
        } else {
            req.setAttribute("lockUpdate", FALSE);
        }
        lastUpdateTimeVideo = GoogleDriveApiUtil.getModifiedTime(GoogleDriveApiUtil.buildSheetsApiClientService(), "1SC92tKYXQDqujUcvZVYMmmNiJp35Q1b22fKg2C7zeQI", "v");
        lastUpdateTimeBanner = GoogleDriveApiUtil.getModifiedTime(GoogleDriveApiUtil.buildSheetsApiClientService(), "1SC92tKYXQDqujUcvZVYMmmNiJp35Q1b22fKg2C7zeQI", "b");
        req.setAttribute("lastUpdateTimeVideo", lastUpdateTimeVideo);
        req.setAttribute("lastUpdateTimeBanner", lastUpdateTimeBanner);
        //TODO Google authorization https://coderoad.ru/15938514/Java-%D0%B8-Google-Spreadsheets-API-%D0%B0%D0%B2%D1%82%D0%BE%D1%80%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F-%D1%81-OAuth-2-0
        req.getRequestDispatcher("main.jsp").forward(req, resp);
    }
}
