package com.mnr.securityservice.service;

import com.mnr.securityservice.dao.AppRoleRepository;
import com.mnr.securityservice.dao.AppUSerRepository;
import com.mnr.securityservice.entities.AppRole;
import com.mnr.securityservice.entities.AppUser;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    //injection des dependances via constructor
    private final AppUSerRepository appUSerRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppRoleRepository appRoleRepository;
    public AccountServiceImpl(AppUSerRepository appUSerRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AppRoleRepository appRoleRepository) {
        this.appUSerRepository = appUSerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.appRoleRepository = appRoleRepository;
    }


    //add user
    @Override
    public AppUser saveUser(String username, String password, String confirmedPassword) {
        AppUser user=appUSerRepository.findByUsername(username);
        if(user != null) throw new RuntimeException("User already exists");
        if(!password.equals(confirmedPassword)) throw new RuntimeException("Please confirm password");

        //user existe pas , creer un user
        AppUser appUser= new AppUser();
        appUser.setUsername(username);
        appUser.setActived(true);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUSerRepository.save(appUser);
        //add role to user by default
        addRoleToUser(username, "USER");

        return appUser;
    }

    @Override
    public AppRole save(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUSerRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser appUser= appUSerRepository.findByUsername(username);
        AppRole appRole= appRoleRepository.findByRoleName(rolename);
        //add role to user
        appUser.getRoles().add(appRole);
    }



}

//vid: 04 min 30
