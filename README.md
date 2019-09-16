# my-netty-im

### 启动server

`./gradlew server:run`

### 启动netty-client

`./gradlew client:run --args='userId'`

userId表示用户名

### 启动websocket-client

直接使用浏览器访问client模块下resources/websocket/websocket-test.html即可

然后就可以聊天了

### IM消息格式

- 第一部分:消息的length
- 第二部分:消息内容(对应`IMMessage`对象, json格式传输)

响应的编解码器分别为`TcpMessageEncoder`和`TcpMessageDecoder`