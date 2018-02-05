package com.softserve.academy.spaced.repetition.domain.enums;

import javax.persistence.*;
import java.util.List;

public enum EntityName{

   DECK_COMMENT,
   COURSE;

   public static String nam(EntityName o) {
      return o.name();
   }
}
