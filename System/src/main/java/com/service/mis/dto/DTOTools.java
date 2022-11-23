package com.service.mis.dto;

import java.util.HashSet;
import java.util.Map;

public interface DTOTools<T> {
    HashSet getClassMethod(T CLASS, Map<Integer, String> fun);

    /**
     * @param CLASS
     * @param fun:Map<1,"get">
     * @return
     */
    boolean doClassMethodByPar(T CLASS, Map<Integer, String> fun);
}
