-- create password_reset_tokens table
CREATE TABLE IF NOT EXISTS password_reset_tokens (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  token VARCHAR(255) NOT NULL UNIQUE,
  expiry_date TIMESTAMP NOT NULL,
  used BOOLEAN NOT NULL DEFAULT FALSE,
  user_id BIGINT NOT NULL,
  CONSTRAINT fk_prt_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- create admin_audit table
CREATE TABLE IF NOT EXISTS admin_audit (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  admin_username VARCHAR(150) NOT NULL,
  action VARCHAR(255) NOT NULL,
  target_username VARCHAR(150),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
