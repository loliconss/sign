**_一个基于wordpress主题的签到脚本_**

**主题示例**：_https://acg222.cn/_

**配置:**
1. 项目配置了日志，因为我用的是宝塔和tomact。所以日志目录在项目同路径下新建一个log文件夹，在log下创建了sign，日志全保存在此路径，如果需要修改可用在配置文件中修改
2. 初始化网站配置等，所以是此类主题的签到，所以我用的全是数组和集合，通过循环进行全部签到，需要在tool文件夹下的Param配置要签到的信息，此文件下有标注参数的作用。此文件下的init方法进行初始化
   需要自己创建list，然后放入自己的用户名和密码，格式为（username=你的用户名&password=你的密码）,因为脚本支持多个一个网站多个账户，
   所以用户账号是list集合，网站是数组，添加账号的顺序下标要和网站数组的下标一致。评论同理。
3. 邮箱授权，我用的是QQ邮箱授权。用的话如果想修改可以直接自己去修改。
4. 评论，因为部分主题有评论任务，所以我写了评论，但是注意，有些网站是人工审核评论的，如果你启用的话容易被拉进黑名单。所以我将评论全部注释掉了。如果想用的话可以在TimsSign文件下打开。
   不建设打开
5. ParamController是测试时候用的，我注释掉了，所以不建设打开。
6. 注意token最开始设计的是每星期检测一次是否过期，后来发现他有效期应该在十天左右没有2星期，所以改为每天。


**注意：**

部分网站人工比较容易封号，所以我在签到的时候设置了随机定时任务。每天签到时间随机，但是我还是遇到一个网站几次封我号，感觉应该是检查了ip，所以此脚本不一定适应于所有此主题网站。不过因为老封我号，所以我直接写了油猴脚本，脚本代码在下面_




    function() {
    console.log("开始运行")
    //你的用户名
    var username = "";
    //你的密码
    var password = "";
    //网站路径  结尾已/结尾
    var path = "";
    var hejiba = new Date(localStorage.getItem("time"));
    let token = localStorage.getItem("timeToken")

      let time = hejiba.getFullYear() + "-" + (hejiba.getMonth() + 1) + "-" + hejiba.getDate();
      var day = new Date();
      let date = day.getFullYear() + "-" + (day.getMonth() + 1) + "-" + day.getDate();
      if (time != date) {
        if (!token) {
          //如果没有token  相当于第一次初始化
          const http = new XMLHttpRequest();
          const url = path + "wp-json/jwt-auth/v1/token";
          http.open("POST", url);
          var fd = new FormData();
          fd.append("username", username);
          fd.append("password", password);
          http.send(fd);
          http.onload = (e) => {
            if (!http.responseText) {
              return;
            }
            let data = JSON.parse(http.responseText)
            if (data.token) {
              token = "Bearer " + data.token;
              localStorage.setItem("timeToken", token)
            } else {
              console.log("登录失败")
              return;
            }
          }
        }
        const http = new XMLHttpRequest();
        const url = path + "wp-json/b2/v1/getUserInfo";
        http.open("POST", url);
        http.setRequestHeader("authorization", token);
        http.send();
        http.onload = (e) => {
          if (!http.responseText) {
            return;
          }
          let data = JSON.parse(http.responseText);
          if (data.data) {
            //token过期的话  就重新登录
            const http = new XMLHttpRequest();
            const url = path + "wp-json/jwt-auth/v1/token";
            http.open("POST", url);
            var fd = new FormData();
            fd.append("username", username);
            fd.append("password", password);
            http.send(fd);
            http.onload = (e) => {
              if (!http.responseText) {
                return;
              }
              let data = JSON.parse(http.responseText)
              if (data.token) {
                token = "Bearer " + data.token;
                localStorage.setItem("hjbToken", token)
              } else {
                console.log("登录失败")
                return;
              }
            }

          }


          //开始激活签到
          const getUserMission = new XMLHttpRequest();
          const url = path + "wp-json/b2/v1/getUserMission";
          getUserMission.open("POST", url);
          getUserMission.setRequestHeader("authorization", token);
          getUserMission.send();
          getUserMission.onload = (e) => {
            if (!getUserMission.responseText) {
              return;
            }
            let data = JSON.parse(getUserMission.responseText)
            if (!data.mission.date) {
              //没有签到  开始进行签到
              const userMission = new XMLHttpRequest();
              const url = path + "wp-json/b2/v1/userMission";
              userMission.open("POST", url);
              userMission.setRequestHeader("authorization", token);
              userMission.send();
              userMission.onload = (e) => {
                //如果签到过，就会直接返回你签的奖励数量  所以不用判断是否成功，这接口不可能失败的
                let data = JSON.parse(userMission.responseText);
                //签到了就将时间设置为当天时间
                localStorage.setItem("time", new Date());
              }
            } else {
              //签到了就将时间设置为当天时间
              localStorage.setItem("time", new Date());
            }

          }
        }
      } else{
        console.log("今天已经签到")
      }


**此油猴脚本只能支持单网站单用户签到，如果你想多网站的话可以自己修改**

