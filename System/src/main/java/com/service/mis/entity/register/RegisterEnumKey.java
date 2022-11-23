package com.service.mis.entity.register;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class RegisterEnumKey {
    @Id
    private int id;
    private final String name;
    @OneToMany(targetEntity = RegisterEnumValue.class, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "key_id")
    private List<RegisterEnumValue> RegisterEnumValue = new ArrayList<>();
}
