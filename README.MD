## 1 踩坑记录

1. 生成Q类

   ```xml
   <plugin>
       <groupId>com.mysema.maven</groupId>
       <artifactId>apt-maven-plugin</artifactId>
       <version>1.1.3</version>
       <executions>
           <execution>
               <phase>generate-sources</phase>
               <goals>
                   <goal>process</goal>
               </goals>
               <configuration>
                   <outputDirectory>target/generated-sources/java</outputDirectory>
                   <processor>com.infobip.spring.data.jdbc.annotation.processor.SpringDataJdbcAnnotationProcessor</processor> <!-- 使用JDBC和R2DBC时需要使用该类生成Q类-->
               </configuration>
           </execution>
       </executions>
       <dependencies>
           <dependency>
               <groupId>com.querydsl</groupId>
               <artifactId>querydsl-apt</artifactId>
               <version>${querydsl.version}</version>
           </dependency>
       </dependencies>
   </plugin>
   ```

2. 不使用flyway时需要配置`SQLTemplates`

   ```java
   @Configuration
   public class SQLTemplatesConfig {
   
       @Bean
       public SQLTemplates sqlTemplates() {
           return new MySQLTemplates(); // 如果是其他数据库修改成对应的Templates
       }
   }
   ```

3. SpringBoot 不会发布`ContextStartedEvent`

   ```java
   public static void main(String[] args) {   					SpringApplication.run(WebfluxR2dbcQuerydslApplication.class, args).start(); // 调用start方法，有副作用，见https://stackoverflow.com/questions/38206676/contextstartedevent-not-broadcasted-on-spring-boot
   }
   ```

   SpringBoot使用run启动只会触发`ContextRefreshedEvent`,如果需要`ContextStartedEvent`可以调用`start`方法

## 2 Webflux

1. 全局异常处理

   创建处理类并继承`AbstractErrorWebExceptionHandler`, 参考`com.anii.querydsl.exception.ExceptionHandler`

2. 参数校验器

   ```xml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
   	<artifactId>spring-boot-starter-validation</artifactId>
   </dependency>
   ```

   参数校验可以参考` [RequestUtils.java](src\main\java\com\anii\querydsl\common\RequestUtils.java) `

3. 加入Security做用户授权

   添加配置类`com/anii/querydsl/config/SecurityConfig`

4. 添加jwt做用户认证

   配置类放在`com/anii/querydsl/config/jwt`

5. 加入List\<String\> 转为 pg 中的JSON数组

   对应的转换器分别为 `com/anii/querydsl/convert/r2dbc/JsonToListConverter.java`和`com/anii/querydsl/convert/r2dbc/ListToJsonConverter.java`

6. 枚举和字符串互转
   SpringDataR2DBC 自带了枚举到字符串的转换，具体的转换逻辑可以参考`org.springframework.data.r2dbc.convert.MappingR2dbcConverter#getPotentiallyConvertedSimpleRead`方法
   
   ```java
		if (Enum.class.isAssignableFrom(target)) {
			return Enum.valueOf((Class<Enum>) target, value.toString());
		}
   ```

7. LocalDateTime日期转换
   在application.yaml中配置的日期格式只对Date有效，LocalDateTime需要额外配置
   ```yaml
   spring:
     webflux:
       format:
         date: yyyy-MM-dd  # 只对Date类型有效
         date-time: yyyy-MM-dd HH:mm:ss
        time: HH:mm:ss
   ```
   
   配置类在`com.anii.querydsl.convert.web.StringToLocalDateConverter`和`com.anii.querydsl.convert.web.StringToLocalDateTimeConverter`
   
## 3 R2DBC

1. 有多个流需要使用SpringReactiveSecurity的上下文信息

例如`com.anii.querydsl.service.impl.ChatServiceImpl#chatStream`多个实体的保存需要用到审计功能，在审计功能中需要读取当前登录的用户信息，所以需要将该流返回给springwebflux，由webflux消费该流才能用到用户信息的上下文。

> 获得当前登录用户信息的三种方式

   三种方式都是通过`ReactiveSecurityContextHolder`获得，只是使用的地方不同

1. 使用的**流**和存放的用户信息在同一个上下文

   因为是在同一个上下文可以直接通过`ReactiveSecurityContextHolder`获得用户信息并使用

   tips: 如果是使用Spring R2DBC 提供的审计功能，需要确保保存的流和用户信息在同一上下文。

   ```java
   @Override
   public Mono<List<ChatRole>> findAll() {
       return UserContextHolder.getUsername()
               .zipWith(Mono.just(ChatRoleTypeEnum.SYSTEM), repository::findAllByUsernameOrType)
               .flatMapMany(Function.identity())
               .collectList();
   }
   ```

2. **流**和用户信息不在同一个上下文

   不在同一个上下文时无法直接通过`ReactiveSecurityContextHolder`获得，此时需要将用户信息作为Service方法的参数传递过来，如下示例：

   ```java
   // Controller
   private Mono<ServerResponse> createImagePrompt(ServerRequest serverRequest) {
       Flux<String> stringFlux = serverRequest.bodyToMono(ChatImageCreateRequest.class)
               .zipWith(UserContextHolder.getUsername(), chatImageService::createImage)
               .flatMapMany(Function.identity());
   
       return CommonResult.ok(stringFlux);
   }
   
   // Service
   Flux<String> createImage(ChatImageCreateRequest request, String username);
   ```

3. 需要返回流同时要将信息保存到数据库（需要审计用户信息）

   因为需要保存审计信息时需要和用户信息在同一个上下文，而有需要向前端返回信息，所以无法直接保证保存数据库的操作和用户信息在同一上下文，所以需要将存数据库的操作合并到返回给前端的流中。

   ```java
   @Override
   public Flux<String> chatStream(Long id, ChatMessageRequest req) {
       // 查找chat 配置
       Mono<Chat> chat = UserContextHolder.getUsername()
               .flatMap(username -> repository.findByIdAndUsername(id, username))
               .cache(); // 添加缓存防止多次查数据库
   
       Flux<Message> commandMessage = chat.map(Chat::getCommand).map(Message::ofSystemContent).flux();
       Flux<Message> chatMessages = chat.flatMapMany(c -> messageRepository.findAllByChatIdLimit(c.getId(), c.getContextNum()))
               .map(MessageMapper.MAPPER::toMessage);
       Flux<Message> newMessage = Mono.just(req.content()).map(Message::ofUserContent).flux();
       Flux<String> replays = Flux.concat(commandMessage, chatMessages, newMessage)
               .collectList()
               .zipWith(chat, (messages, c) ->
                       MessageMapper.MAPPER.toCompletion(c, messages)
               )
               .flatMapMany(client::chatStream)
               .onErrorMap(e -> e instanceof GPTException, e -> new BusinessException(e.getMessage(), ((GPTException) e).getCode()))
               .cache(); // 必须使用cache转化为热源，否则在保存和返回前端时发起多次请求
   
       Mono<String> saveUserMessage = Mono.just(req.content())
               .map(content ->
                       ChatMessage.builder()
                               .chatId(id)
                               .type(MessageTypeEnum.USER)
                               .content(content)
                               .build())
               .flatMap(messageRepository::save) // 如果直接subscribe则无法获得用户信息所以需要确保 和用户信息在同一个上下文中
               .then(Mono.empty());
       Mono<String> saveReplayMessage = replays.collect(Collectors.joining(""))
               .map(c ->
                       ChatMessage.builder()
                               .chatId(id)
                               .content(c)
                               .type(MessageTypeEnum.ASSISTANT)
                               .build())
               .flatMap(messageRepository::save)
               .then(Mono.empty()); // 采用一个空返回，并连接到返回值，可以保证被spring订阅，从而使用spring security写入的上下文
   
       // 会出现的情况：如果流的传输被中断则不会触发保存数据库的操作。
       return Flux.concat(replays, saveUserMessage, saveReplayMessage);
   }
   
   ```

   **需要注意的问题：replays的传输被主动或者异常情况中断，则不会触发存数据库的操作，上面的业务需要这样的逻辑，如果有其他业务需求这种方法可能不适用**

   

   

   