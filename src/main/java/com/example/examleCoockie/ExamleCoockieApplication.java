package com.example.examleCoockie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class ExamleCoockieApplication {

    static RestTemplate template = new RestTemplate();
    final static String myURL = "http://91.241.64.178:7081/api/users";

    public static void main(String[] args) {
        SpringApplication.run(ExamleCoockieApplication.class, args);

        //ДОСТАЕМ КУКИ//

        // забираем всю инфу у http ответа (заголовки, тело, код состояния)
        ResponseEntity<String> forEntity = template.getForEntity(myURL, String.class);
        //вытаскиваем куки
        List<String> cookiesList = forEntity.getHeaders().get("Set-Cookie");
        //печатаем их в консоль
        forEntity.getHeaders().get("Set-Cookie").stream().forEach(System.out::println);
        System.err.println("Это был GET^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        //СОСТАВЛЯЕМ ЗАГОЛОВОК//

        HttpHeaders headers = new HttpHeaders();
        //назначаем формат
        headers.setContentType(MediaType.APPLICATION_JSON);
        //помещаем куки id
        headers.set("Cookie", cookiesList.stream().collect(Collectors.joining(";")));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        //СОЗДАЕМ ЮЗЕРОВ И БЕЖИМ ПО ВЫЗОВАМ МЕТОДОВ//

        //1
        User user = new User(3L, "James", "Brown", (byte) 10);
        HttpEntity<User> userHttpRequestEntity = new HttpEntity<>(user, headers);

        //2
        User user1 = new User(3L, "Thomas", "Shelby", (byte) 10);
        HttpEntity<User> userHttpRequestEntity1 = new HttpEntity<>(user1, headers);

        //ВЫЗОВЫ МЕТОДОВ
        saveUser(userHttpRequestEntity);
        updateUser(userHttpRequestEntity1);
        deleteUser(userHttpRequestEntity1);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    //МЕТОДЫ ЗАПРОСОВ//

    public static void saveUser(HttpEntity<User> userHttpRequestEntity) {
        //получаем http ответ, сделав post запрос
        ResponseEntity<String> responseEntity = template.exchange(myURL,
                HttpMethod.POST,
                userHttpRequestEntity,
                String.class);

        //получаем данные из запроса
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());
        System.out.println(responseEntity.getHeaders());
        System.err.println("Это был POST^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

    }

    public static void updateUser(HttpEntity<User> userHttpRequestEntity1) {
        ResponseEntity<String> responseEntity = template.exchange(myURL,
                HttpMethod.PUT,
                userHttpRequestEntity1,
                String.class);
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());
        System.out.println(responseEntity.getHeaders());
        System.err.println("Это был PUT^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

    }

    public static void deleteUser(HttpEntity<User> userHttpRequestEntity1) {
        ResponseEntity<String> responseEntity = template.exchange(myURL + "/3",
                HttpMethod.DELETE,
                userHttpRequestEntity1,
                String.class);
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());
        System.out.println(responseEntity.getHeaders());
        System.err.println("Это был DELETE^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }
}

