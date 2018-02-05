package com.xiaobiao.chatclient.controller;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.xiaobiao.chatclient.model.Music;
import com.xiaobiao.chatclient.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wuxiaobiao
 * @create 2017-10-02 15:18
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Controller
@RequestMapping(value = "music")
public class MusicController {

    @Autowired
    private MusicService musicService;


    /**
     * 上传音乐跳转的页面
     */
    @RequestMapping(value = "upload")
    public String musicUpload(Model model) throws Exception {
        return "upload/upload";
    }


    /**
     * 上传音乐跳转的页面
     */
//    @RequestMapping(value = "uploadTool")
//    @ResponseBody
//    public String musicUploadTool(HttpServletRequest request) {
//        Gson json = new Gson();
//        Map<String, Object> map = Maps.newHashMap();
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        MultipartFile file = multipartRequest.getFile("file");//file是页面input的name名  
//        String basePath = "D:/upload_test";//文件路径
////        String basePath = "D:/my_demo/my-project/my-test - 副本/my-web/src/main/webapp/static/music/music";//文件路径
//        try {
//            MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//            if (resolver.isMultipart(request)) {
//                String fileStoredPath = "D:/upload_test";//文件夹路径
//                //随机生成文件名  
////                String randomName = "laji";
//                String uploadFileName = file.getOriginalFilename();
//                if (StringUtils.isNotBlank(uploadFileName)) {
//                    //截取文件格式名  
////                    String suffix = uploadFileName.substring(uploadFileName.indexOf("."));
//                    //重新拼装文件名  
////                    String newFileName = randomName + suffix;
//                    String newFileName = uploadFileName;
//                    String savePath = basePath + "/" + newFileName;
//                    File saveFile = new File(savePath);
//                    File parentFile = saveFile.getParentFile();
//                    if (saveFile.exists()) {
//                        saveFile.delete();
//                    } else {
//                        if (!parentFile.exists()) {
//                            parentFile.mkdirs();
//                        }
//                    }
//                    //复制文件到指定路径  
//                    FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
//                    //上传文件到服务器  
////                    FTPClientUtil.upload(saveFile, fileStoredPath);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return json.toJson(map);
//    }

    /**
     * 上传
     */
    @RequestMapping(value = "uploadTool")
    @ResponseBody
    public String handleFormUpload(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Gson json = new Gson();
        Map<String, Object> map = Maps.newHashMap();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");//file是页面input的name名  
        try {
            if (!file.isEmpty()) {
                //以下的代码是将文件file重新命名并存入Tomcat的webapps目录下项目的下级目录fileDir  
                String fileRealName = file.getOriginalFilename();//获得原始文件名;  
                int pointIndex = fileRealName.indexOf(".");//点号的位置       
                String fileSuffix = fileRealName.substring(0, pointIndex);//截取文件后缀  
                String savedDir = request.getSession().getServletContext().getRealPath("/static/uploadFileDir");//获取服务器指定文件存取路径my-web/target/my-web/static   
                File savedFile = new File(savedDir, fileRealName);
                boolean isCreateSuccess = savedFile.createNewFile();
                if (isCreateSuccess) {
                    file.transferTo(savedFile);//转存文件
                    Music music = new Music();
                    music.setUserCode((String) session.getAttribute("user"));
                    music.setMusicName(fileSuffix);
                    music.setDeleteFlag("0");
                    music.setCreateTime(new Date());
                    music.setUrl("/static/uploadFileDir/" + fileRealName);
                    musicService.insertMusic(music);
                    map.put("errorMsg", "上传成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toJson(map);
    }

    @RequestMapping(value = "queryMusic", method = RequestMethod.POST)
    @ResponseBody
    public String querMusicList(Model model, HttpServletRequest request) {
        Gson json = new Gson();
        Map<String, Object> map = Maps.newHashMap();
        try {
            Music music = new Music();
            List<Music> musics = musicService.queryMusic(music);
            map.put("result", musics);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toJson(map);
    }
}
