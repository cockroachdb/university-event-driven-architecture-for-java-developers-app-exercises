package io.roach.movrapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.roach.movrapi.entity.User;

/**
 * JPA Repository for Users - we have no specific requirements for User other than the standard Create,
 * Update, Delete options we inherit from the JpaRepository interface we're sub-classing, so we don't need
 * any special logic here
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
