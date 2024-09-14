package vn.edu.iuh.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.iuh.dto.res.UserAuthResponseDTO;
import vn.edu.iuh.dto.res.UserResponseDTO;
import vn.edu.iuh.models.User;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.typeMap(User.class, UserAuthResponseDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getName(), UserAuthResponseDTO::setRole);
        });

        modelMapper.typeMap(User.class, UserResponseDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getName(), UserResponseDTO::setRole);
        });

        return modelMapper;
    }
}
