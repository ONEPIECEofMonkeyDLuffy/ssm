package com.itheima.ssm.service.impl;

import com.itheima.ssm.dao.IUserDao;
import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import com.itheima.ssm.service.IUserService;
import com.itheima.ssm.utils.BCryptPasswordEncoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo=null;
        userInfo=userDao.findByName(username);
       return new User(userInfo.getUsername(),userInfo.getPassword(),userInfo.getStatus()==0?false:true,
       true,true,true,getAuth(userInfo.getRoles()));
    }

    private List<SimpleGrantedAuthority> getAuth(List<Role> roles) {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }
        return list;
    }

    @Override
    public List<UserInfo> findAll() throws Exception{
        return userDao.findAll();
    }

    @Override
    public void save(UserInfo userInfo) throws Exception{
        userInfo.setPassword(BCryptPasswordEncoderUtils.passwordEncoder(userInfo.getPassword()));
        userDao.save(userInfo);
    }

    @Override
    public UserInfo findById(String id) throws Exception{
        return userDao.findById(id);
    }

    @Override
    public void addRoleToUser(String userId, String[] roleIds) {
        System.out.println(userId);
        for (String roleId : roleIds) {
            System.out.println(roleId);
            userDao.addRoleToUser(userId,roleId);
        }
    }


    @Override
    public List<Role> findOtherRoles(String userId) {
        return userDao.findOtherRoles(userId);
    }

}
