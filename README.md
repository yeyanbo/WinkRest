WinkRest
========


开发工具: Eclipse
应用服务器: Tomcat 6

新建项目,选择 Dynamic Web Project

选用基于JAX-RS标准的开源实现: Apache Wink 1.4

将如下jar包加入到Web项目中:


   * wink-1.4.jar

   * wink-common-1.4.jar

   * wink-server-1.4.jar

   * jsr311-api-1.1.1.jar
   * slf4j-api-1.6.1.jar
   * slf4j-simple-1.6.1.jar

如果需要支持JSON格式,请加入:


   * wink-json-provider-1.4.jar

   * json-20080701.jar


如果不加入这些jar包,请求JSON格式内容时会报错:
The resource identified by this request is only capable of generating responses with characteristics not acceptable according to the request "accept" headers ().
如果需要增加RequestHandler拦截器,请加入:


   * commons-lang-2.3.jar


如果不加入, 会报错:
java.lang.ClassNotFoundException: org.apache.commons.lang.ClassUtils

代码注解:

@Path("/SecurityEvent")
public class SecurityEventService {


class上一定要有个@Path, 指名在URL根地址之后的路径, 如果不需要路径,也要如下写:


@Path("/")
public class SecurityEventService {



如下定义一个简单的资源user, @GET代表HTTP动作, @Produces表示希望的输出格式:


这个输出为XML格式:
@GET
@Path("/user")
@Produces(MediaType.APPLICATION_XML)
public User getUserInXML() {
return getUser("unknown");
}



这个输出为JSON格式:

@GET
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public User getUserInJSON() {
     return getUser("unknown");
}



注: 他们调用的可是同一个getUser函数哦.


更复杂的在后面.




注意了，要想让 User 同时支持 XML 和 JSON 格式，User 类的定义也有讲究：

package com.xxxx.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class User {

private String name = null;
private int age = 20;

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public int getAge() {
return age;
}

public void setAge(int age) {
this.age = age;
}

}



看到了吗？User 类要用 JAXB 注解进行标记哟，不要忘记了！




先看看如何获取这个资源

我们假设URL根地址为: http://localhost:8080/SecurityEventService


我们需要先配置下web.xml文件:


<web-app>
  <display-name>SecurityEventService</display-name>
 
  <welcome-file-list>
    <welcome-file>index.html</welcome-file>
  </welcome-file-list>

  <servlet>
    <servlet-name > SecurityEventService</servlet-name > 
    <servlet-class > 
       org.apache.wink.server.internal.servlet.RestServlet     //这个Servlet是Wink提供的
    </servlet-class > 
    <init-param > 
       <param-name > applicationConfigLocation</param-name > 
       <param-value > /WEB-INF/application</param-value > 
    </init-param > 
  </servlet > 

  <servlet-mapping > 
    <servlet-name > SecurityEventService</servlet-name > 
    <url-pattern > /rest/*</url-pattern>                         //这个是Servlet对应的path 
  </servlet-mapping > 
</web-app > 


附属的还有的文件: /WEB-INF/application
指名拥有资源的类: 内容目前只有一行:
     com.wondersgroup.core.audit.SecurityEventService



OK, 可以运行了.


在浏览器中输入地址:http://localhost:8080/SecurityEventService/rest/user


返回如下:


Content-Length →132
Content-Type →application/xml
Date →Mon, 14 Jul 2014 11:40:01 GMT
Server →Apache-Coyote/1.1


<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<user pin="208">
    <password>23667</password>
    <username>unknown</username>
</user>

那么如何获得JSON格式的内容呢?

增加Http Request Header参数:

Accept: application/json


就可以得到JSON格式的内容了:

Content-Type →application/json
Date →Mon, 14 Jul 2014 11:11:01 GMT
Server →Apache-Coyote/1.1
Transfer-Encoding →chunked


{
    "user": {
        "password": "91790",
        "pin": "710",
        "username": "unknown"
    }
}