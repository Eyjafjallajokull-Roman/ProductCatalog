package org.example.productcategory.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class LoginAttemptService {

  private static final int MAX_ATTEMPTS = 5;
  private static final Duration BLOCK_DURATION = Duration.ofMinutes(10);

  private final ValueOperations<String, Object> valueOps;

  public LoginAttemptService(RedisTemplate<String, Object> redisTemplate) {
    this.valueOps = redisTemplate.opsForValue();
  }

  public void loginFailed(String username) {
    String key = getKey(username);
    Integer attempts = (Integer) valueOps.get(key);
    attempts = (attempts == null) ? 1 : attempts + 1;
    valueOps.set(key, attempts, BLOCK_DURATION);
    log.warn("Login attempt failed for user '{}'. Attempts: {}", username, attempts);
    //will add user to block list (redis) if attempts exceed MAX_ATTEMPTS (need to wait BLOCK_DURATION)
    if (attempts >= MAX_ATTEMPTS) {
      log.error("User '{}' is now blocked for {} minutes.", username, BLOCK_DURATION.toMinutes());
    }
  }

  public void loginSucceeded(String username) {
    boolean removed = Boolean.TRUE.equals(valueOps.getOperations().delete(getKey(username)));
    log.info("Login succeeded for user '{}'. Previous attempt count reset: {}", username, removed);
  }

  public boolean isBlocked(String username) {
    Integer attempts = (Integer) valueOps.get(getKey(username));
    boolean blocked = attempts != null && attempts >= MAX_ATTEMPTS;
    log.info("Checking if user '{}' is blocked: {} (attempts: {})", username, blocked, attempts);
    return blocked;
  }

  private String getKey(String username) {
    return "login:attempts:" + username;
  }
}