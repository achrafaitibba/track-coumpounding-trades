package com.achrafaitibba.trackcompoundingtrades.configuration.token;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    @Query("""
    select t from Token t inner join User a on t.user.username = a.username
    where a.username = :username and (t.expired = false or t.revoked = false)
""")
    List<Token> findAllValidTokensByUser(String username);
    Optional<Token> findByToken(String token);
}
