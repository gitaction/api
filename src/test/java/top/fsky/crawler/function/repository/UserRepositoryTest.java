package top.fsky.crawler.function.repository;

import top.fsky.crawler.application.model.User;
import top.fsky.crawler.application.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

@ActiveProfiles("function")
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.flyway.enabled=false"
})
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @Sql(value = {"classpath:/sql/insert_users.sql"})
  public void given_user_when_findByEmail_then_return_prepared_user() {
    Optional<User> user = userRepository.findByEmail("wayde.sun@gmail.com");
    Assertions.assertTrue(user.isPresent());
    Assertions.assertEquals("wayde.sun@gmail.com", user.get().getEmail());
  }
}