package com.skilluser.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDto implements Serializable {

    private String email;
    private String subject;
    private String content;
}
