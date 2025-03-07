package casaart.emails_clients_db.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Configuration
public class ApplicationBeanConfiguration {
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> context) {
                String source = context.getSource();
                if (source == null || source.isEmpty()) {
                    return null;
                }

                try {
                    if (source.matches("\\d{4}-\\d{2}-\\d{2}")) { // yyyy-MM-dd
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } else if (source.matches("\\d{2}\\d{2}\\d{2}")) { // yyMMdd
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyMMdd"));
                    } else if (source.matches("\\d{2} [A-Za-z]{3} \\d{4}")) { // 25 Feb 2025
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
                    } else if (source.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) { // 25.02.2025
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    }
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Невалиден формат на датата: " + source, e);
                }

                throw new IllegalArgumentException("Неизвестен формат на датата: " + source);
            }
        });

        modelMapper.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> context) {
                String source = context.getSource();
                if (source == null || source.isEmpty()) {
                    return null;
                }
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });

        modelMapper.addConverter(new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(MappingContext<String, LocalTime> context) {
                String source = context.getSource();
                if (source == null || source.isEmpty()) {
                    return null;
                }
                return LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm:ss"));
            }
        });

        return modelMapper;
    }
}

