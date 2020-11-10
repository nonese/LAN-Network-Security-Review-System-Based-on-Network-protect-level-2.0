package com.example.demo.service.impl;

import com.example.demo.entity.Emergency;
import com.example.demo.mapper.EmergencyMapper;
import com.example.demo.service.IEmergencyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yaojiaqi
 * @since 2020-11-10
 */
@Service
public class EmergencyServiceImpl extends ServiceImpl<EmergencyMapper, Emergency> implements IEmergencyService {

}
