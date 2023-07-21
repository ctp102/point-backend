package com.point.core.deduct.service;

import com.point.core.deduct.repository.DeductPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeductPointServiceImpl implements DeductPointService {

    private final DeductPointRepository deductPointRepository;

}
