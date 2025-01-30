package cn.apimix.core.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: Hor
 * @Date: 2024/12/15 21:00
 * @Version: 1.0
 */
@Configuration
public class LocalDateTimeSerializerConfig {
    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer(JacksonProperties properties) {
        String dateFormat = properties.getDateFormat();
        if (StringUtils.isBlank(dateFormat)) {
          dateFormat = "yyyy-MM-dd HH:mm:ss";
         }
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateFormat));
    }

   @Bean
   public LocalDateTimeDeserializer localDateTimeDeserializer(JacksonProperties properties) {
    String dateFormat = properties.getDateFormat();
    if (StringUtils.isBlank(dateFormat)) {
      dateFormat = "yyyy-MM-dd HH:mm:ss";
     }
    return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateFormat));

   }

   @Bean
   public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(
          LocalDateTimeSerializer localDateTimeSerializer, LocalDateTimeDeserializer localDateTimeDeserializer) {
        return builder -> {
          // 序列化
          builder.serializerByType(LocalDateTime.class, localDateTimeSerializer);
          // 反序列化
          builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer);
         };
   }

}
