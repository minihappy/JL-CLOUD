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
public class RegisterItems {
    @Id
    private int id;
    private final String itemName;
    @OneToMany(targetEntity = RegisterFields.class, cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private List<RegisterFields> fields = new ArrayList<>();
}
