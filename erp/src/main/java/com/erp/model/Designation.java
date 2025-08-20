package com.erp.model;

import javax.persistence.*;

@Entity(name = "designation")
@Table(name = "designation")
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "designation_code")
    private Long designationCode;

    @Column(name = "designation_name")
    private String designationName;

    @Column(name = "level")
    private Integer level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDesignationCode() {
        return designationCode;
    }

    public void setDesignationCode(Long designationCode) {
        this.designationCode = designationCode;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    
}
