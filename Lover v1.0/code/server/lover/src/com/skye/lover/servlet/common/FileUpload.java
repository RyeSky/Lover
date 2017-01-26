package com.skye.lover.servlet.common;

import com.google.gson.annotations.Expose;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.Const;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传 <br> 具体步骤： <br>1）获得磁盘文件条目工厂 DiskFileItemFactory 要导包 <br> 2） 利用
 * request 获取 真实路径 ，供临时文件存储，和 最终文件存储 ，这两个存储位置可不同，也可相同 <br>3）对
 * DiskFileItemFactory 对象设置一些 属性 <br> 4）高水平的API文件上传处理 ServletFileUpload upload
 * = new ServletFileUpload(factory); 目的是调用 parseRequest（request）方法 获得 FileItem
 * 集合list <br>5）在 FileItem 对象中 获取信息， 遍历， 判断 表单提交过来的信息 是否是 普通文本信息 另做处理 <br> 6）
 * 第一种. 用第三方 提供的 item.write( new File(path,filename) ); 直接写到磁盘上 第二种. 手动处理
 */
public class FileUpload extends HttpServlet {

    private class Return {
        @Expose
        String filePaths;//文件路径，多个路径用英文逗号分隔

        Return(String filePaths) {
            this.filePaths = filePaths;
        }

        @Override
        public String toString() {
            return "Return [filePaths=" + filePaths + "]";
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (Const.SUPPORT_GET)
            doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BaseResponse br = new BaseResponse();
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            final String path = request.getSession().getServletContext().getRealPath("/upload");
            final String webPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath() + "/upload";
            File file = new File(path);
            if (!file.exists())
                file.mkdirs();
            factory.setRepository(file);
            factory.setSizeThreshold(1024 * 1024);
            ServletFileUpload upload = new ServletFileUpload(factory);
            StringBuilder filePaths = new StringBuilder();
            String fileName;
            List<FileItem> list = upload.parseRequest(request);
            for (FileItem item : list) {
                if (!item.isFormField()) {
                    try {
                        fileName = UUID.randomUUID().toString() + item.getName().substring(item.getName().lastIndexOf("."));
                    } catch (Exception e) {
                        e.printStackTrace();
                        fileName = UUID.randomUUID().toString();
                    }
                    item.write(new File(path, fileName));
                    if (filePaths.length() > 0)
                        filePaths.append(",");
                    filePaths.append(webPath);
                    filePaths.append("/");
                    filePaths.append(fileName);
                }
            }
            br.result = new Return(filePaths.toString());
        } catch (Exception e) {
            e.printStackTrace();
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(br.toString());
        out.flush();
        out.close();
        CommonUtil.log(getClass().getSimpleName() + "\n" + br.toString());
    }

}
