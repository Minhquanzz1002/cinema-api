package vn.edu.iuh.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.iuh.dto.common.auth.res.UserAuthResponse;
import vn.edu.iuh.dto.common.auth.res.UserResponse;
import vn.edu.iuh.models.User;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(User.class, UserAuthResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getName(), UserAuthResponse::setRole);
        });

        modelMapper.typeMap(User.class, UserResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getName(), UserResponse::setRole);
        });

        return modelMapper;
    }
}
