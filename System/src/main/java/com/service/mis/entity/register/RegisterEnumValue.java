package com.service.mis.entity.register;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class RegisterEnumValue {
    @Id
    private int id;
    private final String value;

}
