package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.models.Producer;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.ProducerRepository;
import vn.edu.iuh.services.ProducerService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final ProducerRepository producerRepository;

    @Override
    public Producer createProducer(String name) {
        Producer producer = Producer.builder()
                                    .name(name)
                                    .status(BaseStatus.ACTIVE)
                                    .build();
        return producerRepository.save(producer);
    }
}
