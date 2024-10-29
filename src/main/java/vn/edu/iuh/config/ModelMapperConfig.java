package vn.edu.iuh.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeResponseDTO;
import vn.edu.iuh.dto.res.UserAuthResponseDTO;
import vn.edu.iuh.dto.res.UserResponseDTO;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.models.User;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(User.class, UserAuthResponseDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getName(), UserAuthResponseDTO::setRole);
        });

        modelMapper.typeMap(User.class, UserResponseDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getName(), UserResponseDTO::setRole);
        });

        modelMapper.typeMap(ShowTime.class, AdminShowTimeResponseDTO.ShowTimeDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getMovie().getTitle(), AdminShowTimeResponseDTO.ShowTimeDTO::setMovieTitle);
                    mapper.map(src -> src.getRoom().getName(), AdminShowTimeResponseDTO.ShowTimeDTO::setRoomName);
                    mapper.map(src -> src.getCinema().getName(), AdminShowTimeResponseDTO.ShowTimeDTO::setCinemaName);
                });

        return modelMapper;
    }
}
