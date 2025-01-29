package com.skndan.rdp.repo;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.skndan.rdp.entity.ProfileGroup;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface GroupRepo extends CrudRepository<ProfileGroup, UUID>, PagingAndSortingRepository<ProfileGroup, UUID> {
  Page<ProfileGroup> findAllByActive(boolean status, PageRequest of);
}
