package com.sign.service;

import com.alibaba.fastjson.JSONObject;
import com.sign.tool.Param;
import com.sign.tool.Request;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//@RestController
//@CrossOrigin
//@RequestMapping("/param")
public class ParamController {

//    //添加网站  用户等信息    我不用
//    @PostMapping("/createUrl")
//    public Object create(@RequestBody JSONObject json){
//        String url=json.getString("url");
//        String urlPath=json.getString("urlPath");
//        List<String> username=(List)json.get("username");
//        Integer comment_number=json.getInteger("comment_number");
//        String regex ="^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";
//        Pattern pattern = Pattern.compile(regex);
//        if(!pattern.matcher(url).matches()){
//            //输入的网址有问题
//        }else{
//            if(url.charAt(url.length()-1)!='/'){
//                //不是制定的/结尾
//            }
//        }
//        if(!pattern.matcher(urlPath).matches()){
//            //输入的截取网址路径有问题
//        }else{
//            if(urlPath.charAt(urlPath.length()-1)!='/'){
//                //不是制定的/结尾
//            }
//        }
//        int size=username.size();
//        if(size==0){
//            //没有账号
//        }
//        if(comment_number == null || comment_number<1){
//            //评论次数有问题
//        }
//        List<Integer> comment=new ArrayList<>();
//        for(int i=0;i<size;i++){
//            if(username.get(i).indexOf("username=")==-1 || username.get(i).indexOf("&password=")==-1){
//                //用户的账号有问题
//            }
//            comment.add(0);
//        }
//        int length=Param.url.length;
//        String[] urls=new String[length+1];
//        String[] urlPaths=new String[length+1];
//        Integer[] comment_number_days=new Integer[Param.comments_number_day.length+1];
//        for(int i=0;i<length;i++){
//            urls[i]=Param.url[i];
//            urlPaths[i]=Param.urlPath[i];
//            comment_number_days[i]=Param.comments_number_day[i];
//        }
//        urls[length]=url;
//        urlPaths[length]=urlPath;
//        comment_number_days[length]=comment_number;
//        Param.url=urls;
//        Param.urlPath=urlPaths;
//        Param.comments_number_day=comment_number_days;
//        Param.user.add(username);
//        Param.comments_number.add(comment);
//        for(int i=0;i<length+1;i++){
//            System.out.println(Param.url[i]);
//            System.out.println(Param.urlPath[i]);
//            System.out.println(Param.comments_number_day[i]);
//            System.out.println();
//        }
//        System.out.println("--------------------------------------------");
//        for (int i=0;i<Param.user.size();i++){
//            for(int j=0;j<Param.user.get(i).size();j++){
//                System.out.println(Param.user.get(i).get(j));
//                System.out.println(Param.comments_number.get(i).get(j));
//            }
//            System.out.println();
//        }
//        return pattern.matcher(url).matches();
//    }



//    @PostMapping("/getPost")
//    public void getPost(){
//        Request.getPost();
//    }

//    @PostMapping("/sign")
//    public void sign(){
//        Request.sign();
//    }


//    @PostMapping("/comment")
//    public void comment(){
//        Request.comments();
//    }



}
