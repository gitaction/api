package top.fsky.crawler.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.jdbc.datasource.init.ScriptUtils.executeSqlScript;

@ActiveProfiles("integration")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationBaseTest.class);
    
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected DataSource dataSource;

    protected void cleanTables(String filePath) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            executeSqlScript(conn, new ClassPathResource(filePath));
        }
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) throws SQLException {
        String displayName = testInfo.getDisplayName();
        LOGGER.info(displayName + " to be executed...");
    }

    @AfterAll
    public void afterAll() throws SQLException {
        cleanTables("/sql/clean.sql");
    }
}
