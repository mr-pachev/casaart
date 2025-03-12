package casaart.emails_clients_db.config;

import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.Rating;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
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

                    } else if (source.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}")) { // 29.5.2024
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern("d.M.yyyy"));

                    } else if (source.matches("[A-Za-z]{3} [A-Za-z]{3} \\d{2} \\d{2}:\\d{2}:\\d{2} [A-Z]{3} \\d{4}")) {
                        // Формат: Wed May 29 00:00:00 EEST 2024
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                        sdf.setLenient(false); // За по-строго парсване
                        Date date = sdf.parse(source);
                        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    }
                } catch (DateTimeParseException | ParseException e) {
                    throw new IllegalArgumentException("Невалиден формат на датата: " + source, e);
                }

                throw new IllegalArgumentException("Неизвестен формат на датата: " + source);
            }
        });

        return modelMapper;
    }

}

