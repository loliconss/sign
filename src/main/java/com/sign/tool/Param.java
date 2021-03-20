package com.sign.tool;

import java.util.ArrayList;
import java.util.List;

public class Param {
    //参数配置，
    // 需要在url中写要签到的网站
    // urlpath中写首页正则截取路径
    //comments_number_day对应的每个网站评论次数的任务
    //在init方法中初始化参数
    //user为账号的信息，要和url的下标匹配，可以多个账号
    //TimeSign为定时任务类，因为最高4个评论，所以我就写了4个评论的定时任务


    //要签到的主题网站
    public static  String[] url={};

    //每个网站评论的次数
    public static Integer[] comments_number_day={};

    //获取帖子是需要截取掉的路径   后面的html我就直接在方法里写了   当然如果有一个网站有多层路径的话，
    // 你可以用list装，在正则上进行字符串拼接之内的，毕竟只有获取首页帖子的接口才用到此数据
    public static  String[] urlPath={};

    //主题token接口
    static final String tokenUrl="wp-json/jwt-auth/v1/token";
    //主题检查token是否过期接口
    static final String getUserInfo="wp-json/b2/v1/getUserInfo";
    //每天激活签到接口
    static final String getUserMission="wp-json/b2/v1/getUserMission";
    //签到接口
    static final String userMission="wp-json/b2/v1/userMission";
    //评论的接口
    static final String commentUrl="wp-json/b2/v1/commentSubmit";

    //各个网站的token   第一层list保存各个网站的token  第二层保存每个网站不同账号的  （我有些网站是2个或多个账号）
    static List<List<String>> token=new ArrayList<>();

    //各个网站的评论帖子的id的下标记录
    static List<Integer> tag=new ArrayList<>();

    //每天获取的帖子因为有可能和以前帖子重复，所以用set去重，但是要考虑到排序问题，所以用Result类
    static ArrayList<List<Result>> listPage=new ArrayList<List<Result>>();

    //每天排序完成后并合并去重的帖子
    static ArrayList<List<String>> list=new ArrayList<List<String>>();

    //字符数组，评论时按顺序随机选择评论
    static String[] comments= {};

    //发送邮件的邮箱号
    static String sendEmail="";
    //发送邮件的邮箱授权号
    static String authorization="";
    //接受邮箱的邮箱号
    static String acceptEmail="";

    //账号和密码     现在暂时用数组，（我有些网站是2个或多个账号）   后期准备做list套list在启动的时候初始化
    public static List<List<String>> user=new ArrayList<>();

    //当天每个网站评论的次数
    public static List<List<Integer>> comments_number=new ArrayList<>();



    //当评论重复后会进行下一个帖子评论，防止全部都重复评论，所以这个值当标记用，
    //标记递归的次数，当递归次数等于帖子数量的时候，很有可能就是每个帖子都评论过了，
    //所以项目的评论功能已经没有意义了
    static List<Integer> number=new ArrayList<>();

    //初始化账号密码，要和url中的网址相互对应  同时要设置token，token值为空
    //同时对listPage和list还有tag进行扩容，要不然在获取帖子id的时候因为默认生产的list为null会异常
    public static void init(){
        //可以设置多个账号   自己新建账号集合加入user集合中


        //初始化每个账号评论的数量  要和账号匹配     Param.comments_number   不过部分网站可能人工审核



        //账号密码添加完后直接进行token扩容为和user一个  如果你的user比url的长度长的话，多余的因为匹配不上网站所以是没用的
        //所以我直接就用url的长度了，毕竟user长出来的就算token匹配上了也没用到，
        int size=Param.url.length;
        for(int i=0;i<size;i++){
            //将token扩容到和url一样
            List<String> token=new ArrayList<>();
            int length=Param.user.get(i).size();
            for(int j=0;j<length;j++){
                token.add("");
            }
            Param.token.add(token);
            List<Result> res=new ArrayList<>();
            Param.listPage.add(res);
            List<String> li=new ArrayList<>();
            Param.list.add(li);
            Param.tag.add(0);
            Param.number.add(0);
        }

    }
}
