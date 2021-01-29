package com.example.demo.service.implement;

import com.example.demo.repository.Visit_ViewRepository;
import com.example.demo.service.Visit_ViewService;
import org.springframework.beans.factory.annotation.Autowired;

public class Visit_ViewImpl implements Visit_ViewService {
    @Autowired
    private Visit_ViewRepository repository;

}
