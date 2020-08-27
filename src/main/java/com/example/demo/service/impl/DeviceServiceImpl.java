package com.example.demo.service.impl;

import com.example.demo.entity.Device;
import com.example.demo.mapper.DeviceMapper;
import com.example.demo.service.IDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yaojiaqi
 * @since 2020-08-27
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}
