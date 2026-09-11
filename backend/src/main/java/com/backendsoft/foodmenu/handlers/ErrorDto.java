package com.backendsoft.foodmenu.handlers;

import com.backendsoft.foodmenu.utils.ErrorCodes;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {
   private Integer httpCode;
   private ErrorCodes code;
   private String message;
   private List<String> errors;
}
