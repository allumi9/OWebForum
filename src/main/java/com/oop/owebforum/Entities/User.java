package com.oop.owebforum.Entities;

import lombok.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String username;
    private int karma;
    private LocalDate dateOfRegistration;



}
