package top.fsky.crawler.adapter.inbound.rpc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/health")
@Api(value = "health", tags = "health")
public class HealthController {
    private final JdbcTemplate jdbcTemplate;
    private static final int NORMAL_ERROR_COUNT = 1;
    
    @Autowired
    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @GetMapping
    @ApiOperation("health check")
    public ResponseEntity<?> healthCheck() {
        int errorCode = check();
        if (NORMAL_ERROR_COUNT != errorCode) {
            return new ResponseEntity<>("down", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("up");
    }

    public int check(){
        List<Object> results = jdbcTemplate.query("select 1 from dual",
                new SingleColumnRowMapper<>());
        log.info("===== DB checking...");
        for (Object result : results) {
            log.info(result.toString());
        }
        return results.size();
    }
}
