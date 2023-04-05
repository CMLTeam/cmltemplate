package com.cmlteam.cmltemplate.services;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "s3")
@ConditionalOnProperty({"s3.enabled"})
@Validated
@Getter
@Setter
@ToString(exclude = {"accessKey", "secretKey"})
public class AwsS3Props {
  @NotBlank private String bucket;
  @NotBlank private String accessKey;
  @NotBlank private String secretKey;
  @NotBlank private String region;
  @NotBlank private String endpoint;
}
