layui.use('layim', function (layim) {
    //基础配置
    layim.config({

        init: {
            url: 'findFriends' //接口地址（返回的数据格式见下文）
            , type: 'post' //默认get，一般可不填
            , data: {userCode: $("#user_code").val()} //额外参数
        }

        ,brief: false //是否简约模式（若开启则不显示主面板）
        ,title: $("#user_name").val() //自定义主面板最小化时的标题
        //,right: '100px' //主面板相对浏览器右侧距离
        //,minRight: '90px' //聊天面板最小化时相对浏览器右侧距离
        , initSkin: '3.jpg' //1-5 设置初始背景
        //,skin: ['aaa.jpg'] //新增皮肤
        ,isfriend: true //是否开启好友
        ,isgroup: true //是否开启群组
        //,min: true //是否始终最小化主面板，默认false
        , notice: true //是否开启桌面消息提醒，默认false
        ,voice: true //声音提醒，默认开启，声音文件为：default.wav

        //获取群员接口（返回的数据格式见下文）
        , members: {
            url: '' //接口地址（返回的数据格式见下文）
            , type: 'get' //默认get，一般可不填
            , data: {} //额外参数
        }

        //上传图片接口（返回的数据格式见下文），若不开启图片上传，剔除该项即可
        , uploadImage: {
            url: '' //接口地址
            , type: 'post' //默认post
        }

        //上传文件接口（返回的数据格式见下文），若不开启文件上传，剔除该项即可
        , uploadFile: {
            url: '' //接口地址
            , type: 'post' //默认post
        }
        //扩展工具栏，下文会做进一步介绍（如果无需扩展，剔除该项即可）
        , tool: [{
            alias: 'code' //工具别名
            , title: '代码' //工具名称
            , icon: '&#xe64e;' //工具图标，参考图标文档
        }]

        , msgbox: layui.cache.dir + 'css/modules/layim/html/msgbox.html' //消息盒子页面地址，若不开启，剔除该项即可
        , find: layui.cache.dir + 'css/modules/layim/html/find.html' //发现页面地址，若不开启，剔除该项即可
        , chatLog: layui.cache.dir + 'css/modules/layim/html/chatlog.html' //聊天记录页面地址，若不开启，剔除该项即可
    });

    //建立WebSocket通讯
    //注意：如果你要兼容ie8+，建议你采用 socket.io 的版本。下面是以原生WS为例
    var webSocket = null;
    if ('WebSocket' in window) {
        webSocket = new WebSocket('ws://172.16.9.209:8081/websocket/' + $("#user_id").val());
    } else {
        alert('当前浏览器 不支持 websocket')
    }

    webSocket.onerror = function () {
        console.log("WebSocket连接发生错误");
    }

    //连接成功时触发
    webSocket.onopen = function () {
        console.log('WebSocket连接成功');
    };


    //连接关闭的回调方法
    webSocket.onclose = function () {
        console.log("WebSocket正在关闭链接...");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        webSocket.close();
        console.log("WebSocket连接关闭");
    }

    layim.on('online', function(status){
        console.log("在线状态是========"+status); //获得online或者hide

        //此时，你就可以通过Ajax将这个状态值记录到数据库中了。
        //服务端接口需自写。
    });


    //监听发送消息
    layim.on('sendMessage', function (data) {
        webSocket.send(JSON.stringify(data));
        console.log(data);
    });

    //监听收到的消息
    webSocket.onmessage = function (event) {

        var data = JSON.parse(event.data);
        if(data.to.status == "0" || data.to.status == "1"){
            if(data.to.status == "0"){
                layim.setFriendStatus(data.mine.id, 'online'); //设置指定好友在线，即头像取消置灰
            }
            if(data.to.status == "1"){
                layim.setFriendStatus(data.mine.id, 'offline'); //设置指定好友不在线，即头像置灰
            }
        }else{
            console.log(data);
            var obj = {
                username: data.mine.username
                , avatar: data.mine.avatar
                , id: data.mine.id
                , type: data.to.type
                , content: data.mine.content
            }
            console.log(obj)
            layim.getMessage(obj);
        }
    }


});