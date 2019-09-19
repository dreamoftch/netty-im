var ws = new WebSocket("ws://localhost:18888/ws")
var alreadyLogin = false
var imMessageType = {
    "LOGIN": "LOGIN", // 登陆消息
    "LOGOUT": "LOGOUT", // 登出消息
    "ACK": "ACK", // ACK消息
    "HEARTBEAT": "HEARTBEAT", // 心跳消息
    "CHAT": "CHAT", // 聊天消息
    "MSG_SEND_ACK": "MSG_SEND_ACK", // 消息成功发送到服务器的ACK
    "MSG_READ": "MSG_READ", // 消息已读
}

ws.onmessage = function(e) {
    var showArea = document.getElementById("responset_msg")
    var msgObj = JSON.parse(e.data)
    switch (msgObj.messageType) {
        case imMessageType.CHAT: // 聊天消息
            handleChatMsg(msgObj)
            break
        case imMessageType.MSG_SEND_ACK: // 消息发送到服务器的ack
            handleAck(msgObj)
            break
        case imMessageType.MSG_READ: // 消息发送到服务器的ack
            handleMsgRead(msgObj)
            break
         default:
            console.log('收到其他消息:' + e.data)
    }
}

// 收到聊天消息
function handleChatMsg(msgObj) {
    appendMsg(msgObj)
    // 给服务器发送ack说明我已经收到消息了
    sendAck(msgObj)
    // 对于别人发送给我的消息,这里简单处理,接收到消息即认为消息已读
    sendMsgRead(msgObj)
}

// 消息发送到服务器的ack
function handleAck(msgObj) {
    var statusId = msgObj.requestId + "_status"
    document.getElementById(statusId).innerHTML = "(未读)"
}

// 我发送的消息-对方已读
function handleMsgRead(msgObj) {
    var statusId = msgObj.requestId + "_status"
    document.getElementById(statusId).innerHTML = "(已读)"
}

ws.onopen = function(e) {
    var showArea = document.getElementById("responset_msg")
    showArea.innerHTML = "连接建立"
}
ws.onclose = function(e) {
    var showArea = document.getElementById("responset_msg")
    showArea.innerHTML = showArea.innerHTML + "<br/>连接关闭"
}

function appendMsg(msgObj) {
    if (isSendByMySelf(msgObj)) {
        // 我发送的聊天消息
        buildMyMsg(msgObj)
    } else {
        // 他人发送的聊天消息
        buildOthersMsg(msgObj)
    }
}

function formatDate(msgObj) {
    return dateFtt("yyyy-MM-dd hh:mm:ss", new Date(msgObj.createdAt))
}

// 判断是否是我自己发送的消息
function isSendByMySelf(msgObj) {
    return msgObj.sourceUserId == getUserId()
}

function buildMyMsg(msgObj) {
    var createdAt = dateFtt("yyyy-MM-dd hh:mm:ss", new Date(msgObj.createdAt))
    var showArea = document.getElementById("responset_msg")
    showArea.innerHTML = showArea.innerHTML +
            "<br/><span id="  + msgObj.requestId + ">" +
            "(" + createdAt + ") 我说:" + msgObj.body + "<span id=" +  msgObj.requestId + "_status>" + "(发送中)</span>" +
            "</span>"
}

function buildOthersMsg(msgObj, createdAt) {
    var userId = msgObj.sourceUserId
    if (userId == "0") {
        userId = "系统"
    }
    var createdAt = dateFtt("yyyy-MM-dd hh:mm:ss", new Date(msgObj.createdAt))
    var showArea = document.getElementById("responset_msg")
    showArea.innerHTML = showArea.innerHTML +
            "<br/><span id="  + msgObj.requestId + ">" +
            "(" + createdAt + ") " + userId + "说:" + msgObj.body +
            "</span>"
}

function sendWsMsg() {
    if (ws.readyStatus = WebSocket.OPEN) {
        var inputMsg = document.getElementById("user_input_msg").value
        doSendMsg(inputMsg)
    } else {
        alert("连接已断开！");
    }
}

function doSendMsg(content) {
    var msgObj = {
        "requestId": uuid(),
        "messageType": imMessageType.CHAT,
        "sourceUserId": getUserId(),
        "body": content,
        "createdAt": new Date()
    }
    var messageType = document.getElementById("messageType").value
    if (messageType == "private") {
        // 私聊
        var targetUserId = document.getElementById("targetUserId").value
        if (!targetUserId) {
            alert("请输入目标用户id");
            return
        }
        msgObj.targetUserId = [targetUserId]
    }
    buildMyMsg(msgObj)
    var json = JSON.stringify(msgObj)
    ws.send(json)
}

function login(content) {
    if (alreadyLogin) {
        alert("您当前已经处于登录状态, 请先登出");
        return
    }
    var tokenObj = {
        "token": "a745a0c2-309c-4b08-8ac3-fc91d49179c3"
    }
    var obj = {
        "requestId": uuid(),
        "messageType": imMessageType.LOGIN,
        "sourceUserId": getUserId(),
        "body": JSON.stringify(tokenObj)
    }
    var json = JSON.stringify(obj)
    ws.send(json)
    var showArea = document.getElementById("responset_msg")
    showArea.value = "登陆成功，开始发消息吧"
    alreadyLogin = true
    // 启动后台心跳
    sendHeartBeatBackend()
}

function sendAck(msgObj) {
    var requestId = msgObj.requestId
    if (!requestId) {
        return
    }
    var obj = {
        "requestId": requestId,
        "messageType": imMessageType.ACK
    }
    var json = JSON.stringify(obj)
    ws.send(json)
}

// 告诉服务器消息已读
function sendMsgRead(msgObj) {
    if (isSendByMySelf(msgObj)) {
        // 如果是我自己发送的消息则不需要发送已读通知
        return
    }
    var requestId = msgObj.requestId
    if (!requestId) {
        return
    }
    var obj = {
        "requestId": requestId,
        "messageType": imMessageType.MSG_READ
    }
    var json = JSON.stringify(obj)
    ws.send(json)
}

function getUserId() {
    var userId = document.getElementById("userId").value
    if (!userId) {
        userId = "defaultUser"
    }
    return userId
}

function switchMessageType() {
    var messageType = document.getElementById("messageType").value
    if (messageType == "open") {
        document.getElementById("userList").innerHTML = ''
    } else {
        // 私聊
        document.getElementById("userList").innerHTML = '<input type="text" id="targetUserId" placeholder="请输入目标用户id">'
    }
}

/**
 * 生成uuid
 */
function uuid() {
  return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
    (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
  )
}

/**
 * 发送心跳
 */
function sendHeartBeat() {
    var obj = {
        "requestId": uuid(),
        "messageType": imMessageType.HEARTBEAT,
        "sourceUserId": getUserId()
    }
    var json = JSON.stringify(obj)
    ws.send(json)
    // 继续设置定时心跳
    sendHeartBeatBackend()
}

/**
 * 后台持续发心跳
 */
function sendHeartBeatBackend() {
    setTimeout("sendHeartBeat()", 10000);
}

function dateFtt(fmt,date) { //author: meizz
    var o = {
        "M+" : date.getMonth()+1,     //月份
        "d+" : date.getDate(),     //日
        "h+" : date.getHours(),     //小时
        "m+" : date.getMinutes(),     //分
        "s+" : date.getSeconds(),     //秒
        "q+" : Math.floor((date.getMonth()+3)/3), //季度
        "S" : date.getMilliseconds()    //毫秒
    };
    if(/(y+)/.test(fmt))
    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
    if(new RegExp("("+ k +")").test(fmt))
    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}



