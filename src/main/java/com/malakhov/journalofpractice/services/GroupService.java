package com.malakhov.journalofpractice.services;

import com.malakhov.journalofpractice.exception.ResourceAlreadyExistException;
import com.malakhov.journalofpractice.exception.ResourceNotFoundException;
import com.malakhov.journalofpractice.models.Credential;
import com.malakhov.journalofpractice.models.Group;
import com.malakhov.journalofpractice.repositories.CredentialsRepository;
import com.malakhov.journalofpractice.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    public void createGroup(String name) {
        checkIfGroupExist(name);

        Group group = new Group();
        group.setName(name);
        groupRepository.save(group);
    }

    public void addMember(String email, String name) {
        Credential credential = credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        Group group = groupRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Group Not Found with name: " + name));

        group.addNewMember(credential.getUser());
        groupRepository.save(group);
    }

    private void checkIfGroupExist(String name) throws ResourceAlreadyExistException {
        Optional<Group> group = groupRepository.findByName(name);
        if (group.isPresent()) {
            throw new ResourceAlreadyExistException(
                    String.format("Group with the same name %s already exist", name)
            );
        }
    }
}
