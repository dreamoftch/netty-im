# my-netty-im

### 配置postgre数据库

```
docker pull postgres:10

docker run --name my-postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:10

The default postgres user and database are created in the entrypoint with initdb.
```
然后修改Environment中的数据库连接信息即可。

### 生成mybatis mapper

`./gradlew server:mbGenerator`

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