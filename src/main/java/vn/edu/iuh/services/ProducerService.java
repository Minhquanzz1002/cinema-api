package vn.edu.iuh.services;

import vn.edu.iuh.models.Producer;

public interface ProducerService {
    /**
     * Create a new producer
     *
     * @param name producer name
     * @return producer object
     */
    Producer createProducer(String name);
}
