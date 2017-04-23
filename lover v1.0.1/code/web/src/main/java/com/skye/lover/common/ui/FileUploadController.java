package com.skye.lover.common.ui;

import com.skye.lover.common.model.resp.BaseResponse;
import com.skye.lover.common.model.resp.FileUploadResponse;
import com.skye.lover.util.ConstantUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Controller
@Scope("prototype")
@RequestMapping("mobile/common/")
public class FileUploadController {
    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    /**
     * 文件上传 <p> 具体步骤： <br>1）获得磁盘文件条目工厂 DiskFileItemFactory 要导包 <br> 2） 利用
     * request 获取 真实路径 ，供临时文件存储，和 最终文件存储 ，这两个存储位置可不同，也可相同 <br>3）对
     * DiskFileItemFactory 对象设置一些 属性 <br> 4）高水平的API文件上传处理 ServletFileUpload upload
     * = new ServletFileUpload(factory); 目的是调用 parseRequest（request）方法 获得 FileItem
     * 集合list <br>5）在 FileItem 对象中 获取信息， 遍历， 判断 表单提交过来的信息 是否是 普通文本信息 另做处理 <br> 6）
     * 第一种. 用第三方 提供的 item.write( new File(path,filename) ); 直接写到磁盘上 第二种. 手动处理</p>
     *
     * @param request  请求
     * @param response 响应
     * @return 响应实体
     */
    @ResponseBody
    @RequestMapping(value = "FileUpload", method = {RequestMethod.POST, RequestMethod.GET})
    public Object fileUpload(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse br = new BaseResponse();
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            final String path = request.getSession().getServletContext().getRealPath("/upload");
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
                    filePaths.append(ConstantUtil.WEB_UPLOAD_FILE_PATH);
                    filePaths.append("/");
                    filePaths.append(fileName);
                }
            }
            br.result = new FileUploadResponse(filePaths.toString());
        } catch (Exception e) {
            e.printStackTrace();
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
        log.info("fileUpload\n\t" + br.toString());
        return br.toString();
    }
}
