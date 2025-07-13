-- Seed data for users
INSERT INTO users (
  id,
  name,
  email,
  password_hash,
  phone_number,
  address
) VALUES (
  'usr-abc123',
  'Test User',
  'test@example.com',
  '$2b$12$examplehashedpasswordhere',  -- Replace with actual bcrypt hash
  '+441234567890',
  '{"line1":"123 High St","town":"London","county":"Greater London","postcode":"SW1A 1AA"}'
);

-- Seed data for bank_accounts
INSERT INTO bank_accounts (
  id,
  user_id,
  account_number,
  sort_code,
  name,
  account_type,
  balance,
  currency,
  created_at,
  updated_at
) VALUES (
  'acc-001',
  'usr-abc123',
  '01234567',
  '10-10-10',
  'My Personal Account',
  'personal',
  1000.00,
  'GBP',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- Seed data for transactions
INSERT INTO transactions (
  id,
  account_number,
  user_id,
  amount,
  currency,
  type,
  reference,
  created_at
) VALUES
(
  'tan-001abc',
  '01234567',
  'usr-abc123',
  250.00,
  'GBP',
  'deposit',
  'Initial deposit',
  CURRENT_TIMESTAMP
),
(
  'tan-002xyz',
  '01234567',
  'usr-abc123',
  50.00,
  'GBP',
  'withdrawal',
  'ATM withdrawal',
  CURRENT_TIMESTAMP
);
