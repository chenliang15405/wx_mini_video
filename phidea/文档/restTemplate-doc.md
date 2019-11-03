RestTemplate调用方法


```

//1.直接获取响内容
String object = restTemplate.getForObject("http://localhost:8080/getString?src=hello", String.class);

 //2.获取响应信息，包含响应状态、响应头、响应内容
ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/getString?src=hello", String.class);

// post请求
User user = restTemplate.postForObject("http://localhost:8080/getUser", postData, User.class);


// 设置请求头
HttpHeaders httpHeaders = new HttpHeaders();
httpHeaders.add("Content-Type", "application/json;charset=utf-8");

//设置请求参数
Map<String, Object> postData = new HashMap<>();
postData.put("id", 1L);
postData.put("name", "测试");
postData.put("age", 18);

//将请求头和请求参数设置到HttpEntity中
HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(postData, httpHeaders);

User user = restTemplate.postForObject("http://localhost:8080/getUser", httpEntity, User.class);


// 使用exchange()方法
exchange():在URL上执行特定的HTTP方法，返回包含对象的ResponseEntity，这个对象是从响应体中映射得到的
String strbody=restTemplate.exchange(uri, HttpMethod.GET, entity,String.class).getBody();
、WeatherResponse weatherResponse= JSONObject.parseObject(strbody,WeatherResponse.class);

```

delete():这个方法是在特定的URL上对资源执行HTTP DELETE操作

exchange():在URL上执行特定的HTTP方法，返回包含对象的ResponseEntity，这个对象是从响应体中映射得到的

execute() 在URL上执行特定的HTTP方法，返回一个从响应体映射得到的对象

getForEntity() 发送一个HTTP GET请求，返回的ResponseEntity包含了响应体所映射成的对象

getForObject() 发送一个HTTP GET请求，返回的请求体将映射为一个对象

postForEntity() ``POST 数据到一个URL，返回包含一个对象的ResponseEntity，这个对象是从响应体中映射得到的

postForObject() POST数据到一个URL，返回根据响应体匹配形成的对象

headForHeaders() 发送HTTP HEAD请求，返回包含特定资源URL的HTTP头

optionsForAllow() 发送HTTP OPTIONS请求，返回对特定URL的Allow头信息

postForLocation() POST 数据到一个URL，返回新创建资源的URL

put() PUT 资源到特定的URL


> 解决中文乱码：
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);