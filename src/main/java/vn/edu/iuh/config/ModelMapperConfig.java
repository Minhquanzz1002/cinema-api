package vn.edu.iuh.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.iuh.dto.res.LoginResponseDTO;
import vn.edu.iuh.models.User;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.typeMap(User.class, LoginResponseDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getName(), LoginResponseDTO::setRole);
        });

        return modelMapper;
    }
}
