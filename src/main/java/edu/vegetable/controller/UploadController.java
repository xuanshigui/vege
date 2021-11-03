package edu.vegetable.controller;

import com.alibaba.fastjson.JSONObject;
import edu.vegetable.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/")
public class UploadController extends BaseController{

    private FileService fileService;

    @Autowired
    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping("/uploadimg")
    public Map upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

        if (file.isEmpty()) {
            throw new Exception("上传文件不能为空");
        }
        String fileNames = file.getOriginalFilename();

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String format=sdf.format(new Date());
        String basePath = this.getClass().getClassLoader().getResource("static").getFile();

        String absolutePath = basePath + "/uploadfile/image/" + format +"/";
        //String basePath = request.getServletContext().getRealPath("resources/static/uploadfile");

        File dir = new File(absolutePath);
        if (!dir.isDirectory()) {//文件目录不存在，就创建一个
            dir.mkdirs();
        }
        //上传文件
        String filePath = fileService.uploadFile(file, fileNames, absolutePath);
        JSONObject data = new JSONObject();
        data.put("path", filePath);
        return buildResponse(data);
    }

    @RequestMapping("/upload_img")
    public Map upload_textarea(@RequestParam("file") MultipartFile file , HttpServletRequest request) throws Exception {

        if (file.isEmpty()) {
            throw new Exception("上传文件不能为空");
        }
        String fileNames = file.getOriginalFilename();
        //上传文件
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String format=sdf.format(new Date());
        String basePath = this.getClass().getClassLoader().getResource("static").getFile();

        String absolutePath = basePath + "/uploadfile/image/" + format;
        File dir = new File(absolutePath);
        if (!dir.isDirectory()) {//文件目录不存在，就创建一个
            dir.mkdirs();
        }
        String filePath = fileService.uploadFile(file, fileNames,absolutePath);
        Map<String, String> map = new HashMap<>();
        String newFileName = filePath.substring(filePath.lastIndexOf("/"),filePath.length());
        String newFilePath = "http://127.0.0.1:8080/show_img?imgPath="+absolutePath + newFileName;
        System.out.println(newFilePath);
        map.put("location", newFilePath);
        //"location", downloadUrl + dir + "/" + name

        return map;
    }

    @RequestMapping("/show_img")
    public byte[] showImg(@RequestParam("imgPath") String imgPath) throws Exception {

        File file = new File(imgPath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());

        return bytes;
    }
}
