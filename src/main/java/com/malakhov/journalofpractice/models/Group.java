package com.malakhov.journalofpractice.models;

import com.malakhov.journalofpractice.exception.ResourceAlreadyExistException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Group name is mandatory")
    @Size(max = 50)
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "group")
    private Set<User> members = new HashSet<>();

    public void addNewMember(User user) {
        user.setGroup(this);
        boolean result = members.add(user);
        if(!result) {
            throw new ResourceAlreadyExistException("Such user already exists in group");
        }
    }
}
