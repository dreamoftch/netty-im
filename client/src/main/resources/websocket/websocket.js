var ws = new WebSocket("ws://localhost:18888/ws")

ws.onmessage = function(e) {
    var showArea = document.getElementById("responset_msg")
    var msgObj = JSON.parse(e.data)
    if (msgObj.messageType != 'CHAT') {
        return
    }
    var userId = msgObj.sourceUserId
    if (userId == "0") {
        userId = "系统"
    } else if (userId == getUserId()) {
        userId = "我"
    }
    showArea.value = showArea.value + "\n" + userId + "说:" + msgObj.body
    // 给服务器发送ack
    sendAck(msgObj)
}
ws.onopen = function(e) {
    var showArea = document.getElementById("responset_msg")
    showArea.value = "连接建立"
}
ws.onclose = function(e) {
    var showArea = document.getElementById("responset_msg")
    showArea.value = "连接关闭"
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
        "messageType": "CHAT",
        "sourceUserId": getUserId(),
        "body": content
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
    var json = JSON.stringify(msgObj)
    ws.send(json)
}

function login(content) {
    var tokenObj = {
        "token": "a745a0c2-309c-4b08-8ac3-fc91d49179c3"
    }
    var obj = {
        "requestId": uuid(),
        "messageType": "LOGIN",
        "sourceUserId": getUserId(),
        "body": JSON.stringify(tokenObj)
    }
    var json = JSON.stringify(obj)
    ws.send(json)
    var showArea = document.getElementById("responset_msg")
    showArea.value = "登陆成功，开始发消息吧"
}

function sendAck(msg) {
    var requestId = msg.requestId
    if (!requestId) {
        return
    }
    var obj = {
        "requestId": requestId,
        "messageType": "ACK"
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

function uuid() {
  return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
    (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
  )
}


