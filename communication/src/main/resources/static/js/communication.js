$(function () {
    //取得当前登录成功的用户名
    var uname = '${sessionScope.user}';
    // 定义一个变量，用于存储建立好的连接
    var ws=null;
    //path即为连接地址
    var path="ws://localhost:8100/chat?name="+uname;

    $(function(){
        if ('WebSocket' in window) {
            ws = new WebSocket(path);
        } else if ('MozWebSocket' in window) {
            ws = new MozWebSocket(path);
        } else {
            alert("当前浏览器，不支持websocket");
            return;
        }
        //建立连接以后，会调用的回调函数
        ws.onopen = function () {
            //alert("连接管道已建立 ");
        };
        //当服务器端返回消息时，该函数会指定,event.data即可以得到返回来的内容
        ws.onmessage = function (event) {
            eval("var t="+event.data);


            //取得欢迎信息
            var welcome = t.msg;
            //判断信息是否为空
            if(welcome!=undefined&&welcome!=""){
                $("#msgTd").append(welcome+"<Br>");
            }

            //得以用户列表
            var names = t.names;
            if(names!=undefined&&names!=""){
                $("#userTd").html("");//清空原有用户名
                $.each(names,function(index,k){
                    $("#userTd").append(k+"<Br>");
                })
            }
            //得到消息
            var content = t.content;
            if(content!=undefined&&content!=""){
                $("#msgTd").append(content+"<Br>");
            }
        };

        //发送消息
        $("#sendBtn").click(function(){
            var msg = $("[name=msg]").val();
            ws.send(msg);
            $("[name=msg]").val("");

        });

    });

    //关闭浏览器之前，先关闭管道
    window.onbeforeunload = onbeforeunload_handler;

    function onbeforeunload_handler() {

        ws.close();//关闭通道
    }
})