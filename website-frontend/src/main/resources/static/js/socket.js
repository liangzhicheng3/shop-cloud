function createSocket(uuid){
    var socket;
    if(typeof(WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    }else{
        //实现化WebSocket对象，指定要连接的服务器地址与端口，建立连接
        socket = new WebSocket("ws://localhost:8095/" + uuid);
        //打开事件
        socket.onopen = function() {
            console.log("Socket 已连接...");
            //socket.send("这是来自客户端的消息" + location.href + new Date());
        };
        //获得消息事件
        socket.onmessage = function(event) {
            var result = $.parseJSON(event.data);
            if(result.code === 10000){
                layer.confirm("恭喜你，秒杀成功！查看订单？",{btn:["确定","取消"]},
                    function(){
                        window.location.href = "/order_detail.html?orderNo=" + result.data.orderNo;
                    },
                    function(){
                        layer.closeAll();
                    });
            }else{
                layer.msg(result.message);
            }
        };
        //关闭事件
        socket.onclose = function() {
            console.log("Socket 已断开...");
        };
        //发生了错误事件
        socket.onerror = function() {
            console.log("Socket 连接异常...");
        }
        //窗口关闭
        $(window).unload(function(event){
            socket.close();
        });
    }
    return socket;
}
