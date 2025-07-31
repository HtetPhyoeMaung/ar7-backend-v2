package com.security.spring.spacetechmm.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpaceTechGameRegisterObj {
   private String domain;
   private String game;
   private int level;
   private String id;
   private String nickname;
   private Integer balance;
   private String profile;

}
