package com.springredis.redispractice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.springredis.redispractice.Pojos.Receiver;
import com.springredis.redispractice.config.AppConfig;

/*This is a simple practice to know how to connect redis to Spring boot and
 * practice the knowledge in future practices.
 * 
 * Practice's being made from spring tutorials: https://spring.io/guides/gs/messaging-redis/
 * 
 * It's for sending queue messages thorugh redis. I'll comment every section of the code
 * so I can understand later what I did.
 */
@SpringBootApplication
public class RedispracticeApplication{
	private static final Logger logger = LoggerFactory.getLogger(RedispracticeApplication.class);
	public static void main(String[] args) throws InterruptedException {
		/*First of all, because is a web app, the context of the application
		 * must be built from the SpringApplication.run() instance, many tutorials
		 * dont clarify this very specific. When I tried to achieve 
		 * configuring the configuration file to the main class,
		 * I didn't have success until doing a new  project for some reason.
		 * 
		 * So you adquire the context of the project and you retrieve the beans. 
		 * Remember that beans are singleton instances of a class, in java they are specified
		 * as classes without contructor, and with a serializable interface implemented
		 * for saving objects in disk. 
		 */

		//we get the config instance
		ConfigurableApplicationContext c1= SpringApplication.run(RedispracticeApplication.class, args);

		//We get the bean for the redis sending template.
		StringRedisTemplate redisTemplate=c1.getBean(StringRedisTemplate.class);

		//we get out pojo listener.
		Receiver c2=c1.getBean(Receiver.class);

		//test for checking if bean is read.
		logger.info(c2.getPruebaBean());
		while(c2.getCount()==0){
			logger.info("Sending message...");
			
			//we send the message
			redisTemplate.convertAndSend("chat", "Hello from Redis!");
			Thread.sleep(500L);
		}
		System.exit(0);

	}



}
