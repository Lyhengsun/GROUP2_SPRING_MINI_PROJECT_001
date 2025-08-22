package kshrd.group2.article_mgmt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kshrd.group2.article_mgmt.model.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    public Optional<AppUser> findByEmail(String email);
}
