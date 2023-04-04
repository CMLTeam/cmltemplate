package com.cmlteam.cmltemplate.config;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BadRequestError {
  private final boolean success;
  private final String error;
  private final Map<String, String> errors;
}
