package com.sign.service;

import com.sign.tool.Request;
import com.sign.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Timer;
import java.util.TimerTask;


@Component
public class TimeSign {

    Logger log= LoggerFactory.getLogger(TimeSign.class);

    //每天凌晨1点签到
    @Scheduled(cron="00 00 1 ? * *")
    public void sign() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Request.sign();
                timer.cancel();
            }
        }, Tool.getIntTime());
    }


    //每天01:20:30进行一波评论  多次隔开评论是有的网站不让快速评论  时间间隔长是因为怕重复评论后要重新调起评论，间隔短容易被和延时后的评论重叠被cf抓到
//    @Scheduled(cron="30 20 1 ? * *")
//    public void comments() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Request.comments();
//                timer.cancel();
//            }
//        }, Tool.getIntTime());
//    }


    //每天01:40:30进行一波评论
//    @Scheduled(cron="30 40 1 ? * *")
//    public void comments2() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Request.comments();
//                timer.cancel();
//            }
//        }, Tool.getIntTime());
//    }

    //每天02:06:30进行一波评论
//    @Scheduled(cron="30 06 2 ? * *")
//    public void comments3() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Request.comments();
//                timer.cancel();
//            }
//        }, Tool.getIntTime());
//    }

    //每天02:28:30进行一波评论
//    @Scheduled(cron="30 28 2 ? * *")
//    public void comments4() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Request.comments();
//                timer.cancel();
//            }
//        }, Tool.getIntTime());
//    }

    //每天02:38:30进行一波评论
//    @Scheduled(cron="30 38 2 ? * *")
//    public void comments5() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Request.comments();
//                timer.cancel();
//            }
//        }, Tool.getIntTime());
//    }

    //每天20:10:30清空评论的次数记录  因为加入了随机时间定时任务  所以是8点以后到10点多这段时间
//    @Scheduled(cron="30 10 20 ? * *")
//    public void empty() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                int size= Param.comments_number.size();
//                for(int i=0;i<size;i++){
//                    int length=Param.comments_number.get(i).size();
//                    for(int j=0;j<length;j++){
//                        Param.comments_number.get(i).set(j,0);
//                    }
//                }
//                timer.cancel();
//                log.info("Empty the complete");
//            }
//        }, Tool.getIntTime());
//    }



    //每天凌晨00:10:00分开始准备进行首页获取更新帖子id   （随机时间在10分钟到2小时10分钟之间）
//    @Scheduled(cron="00 10 0 * * ?")
//    public void updatePost() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Request.getPost();
//                timer.cancel();
//            }
//        }, Tool.getIntTime());
//    }


    //每周五晚上23:00:00分再判断一次token是否过期，这样的话防止在评论的时候token过期
//    @Scheduled(cron="0 0 23 ? * FRI")
    @Scheduled(cron="00 30 19 ? * *")
    public void resetToken() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Request.resetToken();
                timer.cancel();
            }
        }, Tool.getIntTime());
    }


}
