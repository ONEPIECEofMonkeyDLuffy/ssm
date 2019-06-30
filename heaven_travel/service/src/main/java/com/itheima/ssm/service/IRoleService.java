package com.itheima.ssm.service;

import com.itheima.ssm.domain.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IRoleService {
    void save(Role role) throws Exception;
    List<Role> findAll() throws Exception;
    Role findById(String id) throws Exception;
}
