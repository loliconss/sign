package com.sign.tool;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {

    static Logger log= LoggerFactory.getLogger(com.sign.tool.Request.class);

    //检查token是否过期
    public static void resetToken(){
        int size=Param.url.length;
        Integer[] dataInt=new Integer[2];
        for(int i=0;i<size;i++){
            if(Param.user.size()>0){
                int length=Param.user.get(i).size();
                for(int j=0;j<length;j++){
                    dataInt[0]=i;
                    dataInt[1]=j;
                    if(Param.token.get(i).get(j)==null || Param.token.get(i).get(j).equals("")){
                        Param.token.get(i).set(j,"Bearer "+JSONObject.parseObject(Tool.post(Param.user.get(i).get(j), Param.url[i]+Param.tokenUrl, false, null, dataInt)).getString("token"));
                        log.info(Param.url[i]+"   网站的token获取成功；当前时间"+Tool.getTime());
                    }else{
                        //判断token是否过期，当token过期的时候因为接口返回的状态码是403，所以直接就在catch中进行重新获取赋值
                        String userInfo=Tool.post(null, Param.url[i]+Param.getUserInfo, true, Param.token.get(i).get(j), dataInt);
                        if(userInfo==null) {
                            log.info(Param.url[i]+"  网站token过期，token获取成功");
                            return;
                        }
                    }
                }
            }
        }
    }


    //签到接口
    public static void sign(){
        //上来先判断各个网站的token是否过期
        int size=Param.url.length;
        Integer[] dataInt=new Integer[2];
        for(int i=0;i<size;i++){
            dataInt[0]=i;
            int length=Param.token.get(i).size();
            for(int j=0;j<length;j++){
                dataInt[1]=j;
                //激活签到接口
                Tool.post(null,Param.url[i]+Param.getUserMission, true, Param.token.get(i).get(j), dataInt);
                //开始签到
                String information=Tool.post(null,Param.url[i]+Param.userMission, true, Param.token.get(i).get(j), dataInt);

                if(information == null || information.length()<10){
                    //应该是没有签到成功，也有可能是签到过了或者是激活出现问题
                    log.info("当前网站："+Param.url[i]+"可能是签到过了或者是激活出现问题");
//                    Email.Send("网站签到失败", Param.url[i]);
                }else{
                    log.info("当前网站："+Param.url[i]+" 签到成功");
                }
            }
        }
    }




    //获取帖子首页帖子id
    public static void getPost(){
        int size=Param.url.length;
        Integer[] dataInt=new Integer[2];
        for(int i=0;i<size;i++){
            dataInt[0]=i;
            String post=Tool.post(null, Param.url[i], false, null, dataInt);
            //当page大于100时代表获取到了当前首页的信息   注意这个100是我自己加的，可能会返回别的错误什么的，所以我就用100
            if(post == null || post.length()<100){
                //再重新获取一次
                post= Tool.post(null, Param.url[i], false, null, dataInt);
            }
            if(post == null || post.length()<100){
                //还小于100就算了，防止一直循环下去。可能网站没了什么的。
                log.error("获取首页信息失败");
                Email.Send("网站获取首页失败", Param.url[i]);
                return;
            }
            //通过正则匹配首页上的href=你要的网址+帖子id的页面；因为有的网站虽然主题一样但是有多层路径，首页设置了个urlPath
            String regex ="href=\""+Param.urlPath[i]+"\\d{3,7}.html";
            duplicate(regex, Param.urlPath[i],post,i);
            if(i==3){
                regex="href=\""+Param.url[i]+"ent/as/\\d{3,7}.html";
                duplicate(regex,Param.url[i]+"ent/as/",post,i);
            }
        }
    }



    //开始评论
    public static void comments(){
        int size=Param.url.length;
        Integer[] dataInt=new Integer[2];
        for(int i=0;i<size;i++){
            dataInt[0]=i;
            List<String> list=Param.list.get(i);
            int tag=Param.tag.get(i);
            int length= list.size();
            if(length==0) {
                Request.getPost();
                list=Param.list.get(i);
                length= list.size();
            }
            //如果文章id用完了只能从头再来
            if(length<= tag) {
                Param.tag.set(i,0);
            }
            //首先获取帖子id，因为是按顺序排列，所以我们按顺序来取帖子id
            String postId= list.get(tag);
            //获取到此网站下的账号数量，对多个账号进行评论
            int number= Param.user.get(i).size();
            for (int j=0;j<number;j++){
                dataInt[1]=j;
                commentsReturn(Param.url[i]+Param.commentUrl, Param.list.get(i).get(Param.tag.get(i)),Param.token.get(i).get(j), dataInt);
            }
            Param.tag.set(i,tag++);
            Param.number.set(i,0);
        }
    }



    //账号进行评论
    public static void commentsReturn(String url, String postId, String token, Integer[] dataInt){
        if(dataInt[0]==2){
            //网站https://mcy.ink/没有评论权限，所以我就直接不评论了
            return;
        }
        if(Param.comments_number_day[dataInt[0]]<=Param.comments_number.get(dataInt[0]).get(dataInt[1])){
            //当天此网站此账号的评论任务完成了，没必要再评论
            log.info(Param.url[dataInt[0]]+"   over");
            return;
        }
        int lenght= Param.comments.length;
        String data="comment_post_ID="+postId+"&comment="+ Param.comments[(int)(Math.random()*lenght)];
        String result=Tool.post(data, url,true, token, dataInt);
        //如果http是403就重新调起
        if(result==null) {
            return;
        }else {
            Param.comments_number.get(dataInt[0]).set(dataInt[1],Param.comments_number.get(dataInt[0]).get(dataInt[1])+1);
            log.info("网站："+url+"   评论成功");
        }
    }

    public static void duplicate(String regex,String urlPath,String post, int i){
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(post);
        List<Result> list= Param.listPage.get(i);
        int number=list.size();
        while (m.find()) {
            list.add(new Result(number+1,m.group().replace("href=\""+urlPath,"").replace(".html","")));
        }
        list= new ArrayList<>(new HashSet<>(list));
        Result[] temp = new Result[list.size()];
        list.toArray(temp);
        Arrays.sort(temp, new Comparator<Result>() {
            @Override
            public int compare(Result result1, Result result2) {
                return result1.getTempId() - result2.getTempId();
            }
        });
        list=Arrays.asList(temp);
        int length=list.size();
        if(length<=0) {
            //报异常
            log.error("获取帖子id失败");
            Email.Send("网站获取帖子id失败", Param.url[i]);
        }

        List<String> box=new ArrayList<>();
        for(int j=0;j<length;j++) {
            list.get(j).setTempId(j+1);
            box.add(list.get(j).getPost_id());
        }
        Param.list.set(i,box);
        log.info("当前网站："+Param.url[i]+"于"+Tool.getTime()+"  更新了首页，获取到了文章id");
    }





}
