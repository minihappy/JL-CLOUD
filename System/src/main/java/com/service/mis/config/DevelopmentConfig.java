package com.service.mis.config;//package com.service.mis.config;
//
//import com.service.mis.data.user.AuthorityRepository;
//import com.service.mis.data.user.RoleRepository;
//import com.service.mis.data.user.UserRepository;
//import com.service.mis.entity.Authority;
//import com.service.mis.entity.Role;
//import com.service.mis.entity.User;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Configuration
//public class DevelopmentConfig {
//
//    @Bean
//    public CommandLineRunner dataLoader(UserRepository userRepo, RoleRepository roleRepo, AuthorityRepository authorityRepo, PasswordEncoder encoder) {
//        return args -> {
//            if (userRepo.findByUsername("admin") == null) {
//                Role role = new Role("ADMIN");
//                User user = new User("admin", encoder.encode("123456"));
//                Authority auth = new Authority(0,"系统管理", "", "sys:manage", "", 0, "el-icon-s-operation", 1, new Date(), new Date(), 1);
//
//                List<Role> roles = new ArrayList<>();
//                List<User> users = new ArrayList<>();
//                List<Authority> authList = new ArrayList<>();
//
//                roles.add(role);
//                users.add(user);
//                authList.add(auth);
//
//                user.setRole(roles);
//                role.setAuthority(authList);
//
//                authorityRepo.save(auth);
//                userRepo.save(user);
//                roleRepo.save(role);
//
//                Role role1 = new Role("ADMIN1");
//                User user1 = new User("admin1", encoder.encode("654321"));
//                Authority auth1 = new Authority(0,"系统管理", "", "sys:manage", "", 0, "el-icon-s-operation", 1, new Date(), new Date(), 1);
//
//                List<Role> roles1 = new ArrayList<>();
//                List<User> users1 = new ArrayList<>();
//                List<Authority> authList1 = new ArrayList<>();
//
//                roles1.add(role1);
//                users1.add(user1);
//                authList1.add(auth1);
//
//                user1.setRole(roles1);
//                role1.setAuthority(authList1);
//
//                authorityRepo.save(auth1);
//                userRepo.save(user1);
//                roleRepo.save(role1);
//            }
//        };
//    }
//
//}
