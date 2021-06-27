package com.gacnik.diplomska.naloga;

import com.gacnik.diplomska.naloga.model.Address;
import com.gacnik.diplomska.naloga.model.Employee;
import com.gacnik.diplomska.naloga.model.Sex;
import com.gacnik.diplomska.naloga.repo.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Log4j2
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Bean
//	CommandLineRunner runner(EmployeeRepository employeeRepository) {
//		return args -> {
//			Employee employee = new Employee(
//				"Jan Alojz",
//					"Strah",
//					"mail1@gmail.com",
//					"+3687089629001",
//					new Address("Dr pot 12", "2000", "Maribor", "Slovenia"),
//					Sex.MALE,
//					List.of("idPhone11", "idPC11")
//			);
//				employeeRepository.insert(employee);
//		};
//	}
}
