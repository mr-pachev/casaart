package casaart.emails_clients_db.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

//        modelMapper.addConverter(new Converter<String, LocalDate>() {
//            @Override
//            public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
//                return LocalDate
//                        .parse(mappingContext.getSource(),
//                                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            }
//        });
//
//        modelMapper.addConverter(new Converter<String, LocalDate>() {
//            @Override
//            public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
//                return LocalDate
//                        .parse(mappingContext.getSource(),
//                                DateTimeFormatter.ofPattern("yyMMdd"));
//            }
//        });
//
//        modelMapper.addConverter(new Converter<String, LocalDateTime>() {
//            @Override
//            public LocalDateTime convert(MappingContext<String, LocalDateTime> mappingContext) {
//                return LocalDateTime
//                        .parse(mappingContext.getSource(),
//                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            }
//        });
//
//        modelMapper.addConverter(new Converter<String, LocalTime>() {
//            @Override
//            public LocalTime convert(MappingContext<String, LocalTime> mappingContext) {
//                return LocalTime.parse(
//                        mappingContext.getSource(),
//                        DateTimeFormatter.ofPattern("HH:mm:ss")
//                );
//            }
//        });

        return modelMapper;
    }
}

