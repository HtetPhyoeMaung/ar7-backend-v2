package com.security.spring;

import com.security.spring.user.repository.ComponentRepository;
import com.security.spring.user.role.Component;
import com.security.spring.user.role.Role;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "com.security.spring")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	public Application(ComponentRepository componentRepository){
//		Map<String, String> adminPermissionCode = new HashMap<>();
//		Map<String, String> downLinePermissionCode = new HashMap<>();
//
//		// Admin permissions
//		adminPermissionCode.put("01000", "Dashboard");
//		adminPermissionCode.put("02000", "Profile");
//		adminPermissionCode.put("03000", "DownLineUserList");
//		adminPermissionCode.put("04000", "UserManagement");
//		adminPermissionCode.put("05000", "Admin Unit History");
//		adminPermissionCode.put("06000", "Unit Transaction History");
//		adminPermissionCode.put("07000", "GameType");
//		adminPermissionCode.put("08000", "Game Provider");
//		adminPermissionCode.put("09000", "Bank Type");
//		adminPermissionCode.put("10000", "Bank Name");
//		adminPermissionCode.put("11000", "Bank Account");
//		adminPermissionCode.put("12000", "Bank Type Auth");
//		adminPermissionCode.put("13000", "Bank Name Auth");
//		adminPermissionCode.put("14000", "User Report");
//		adminPermissionCode.put("15000", "Deposit");
//		adminPermissionCode.put("16000", "Withdraw");
//		adminPermissionCode.put("17000", "Calculate Commission");
//		adminPermissionCode.put("18000", "Setting");
//		adminPermissionCode.put("19000", "Term and Condition");
//
//		downLinePermissionCode.put("01000", "Dashboard");
//		downLinePermissionCode.put("02000", "Profile");
//		downLinePermissionCode.put("03000", "DownLineUserList");
//		downLinePermissionCode.put("06000", "Unit Transaction History");
//		downLinePermissionCode.put("11000", "Bank Account");
//		downLinePermissionCode.put("12000", "Bank Type Auth");
//		downLinePermissionCode.put("13000", "Bank Name Auth");
//		downLinePermissionCode.put("14000", "User Report");
//		downLinePermissionCode.put("15000", "Deposit");
//		downLinePermissionCode.put("16000", "Withdraw");
//		downLinePermissionCode.put("18000", "Setting");
//		downLinePermissionCode.put("19000", "Term and Condition");
//
//		Component adminComponent = Component.builder()
//				.role(Role.ADMIN)
//				.permissionCode(adminPermissionCode)
//				.build();
//
//		Component seniorMasterComponent = Component.builder()
//				.role(Role.SENIORMASTER)
//				.permissionCode(downLinePermissionCode)
//				.build();
//
//		Component masterComponent = Component.builder()
//				.role(Role.MASTER)
//				.permissionCode(downLinePermissionCode)
//				.build();
//
//		Component agentComponent = Component.builder()
//				.role(Role.AGENT)
//				.permissionCode(downLinePermissionCode)
//				.build();
//		componentRepository.save(adminComponent);
//		componentRepository.save(seniorMasterComponent);
//		componentRepository.save(masterComponent);
//		componentRepository.save(agentComponent);
//
//
//	}
}
