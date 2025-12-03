package edu.tecnm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // 👈 Importamos

@SpringBootApplication
@EnableScheduling // 👈 Habilitamos las tareas programadas
public class Proyecto1Application {

	public static void main(String[] args) {
		SpringApplication.run(Proyecto1Application.class, args);
	}

}