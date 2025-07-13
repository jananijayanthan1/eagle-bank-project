-- db/init.sql

CREATE TABLE IF NOT EXISTS users (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  email TEXT UNIQUE NOT NULL,
  password_hash TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  address TEXT NOT NULL, -- as JSON string
  created_timestamp TIMESTAMP NOT NULL,
  updated_timestamp TIMESTAMP NOT NULL
);

-- Create bank_accounts table
CREATE TABLE IF NOT EXISTS bank_accounts (
  id TEXT PRIMARY KEY,  -- you can use TEXT instead of UUID in SQLite
  user_id TEXT NOT NULL,
  account_number TEXT UNIQUE NOT NULL CHECK (account_number GLOB '01[0-9][0-9][0-9][0-9][0-9][0-9]'),
  sort_code TEXT NOT NULL CHECK (sort_code = '10-10-10'),
  name TEXT NOT NULL,
  account_type TEXT NOT NULL CHECK (account_type = 'personal'),
  balance NUMERIC(12,2) NOT NULL CHECK (balance >= 0),
  currency TEXT NOT NULL CHECK (currency = 'GBP'),
  created_timestamp TIMESTAMP NOT NULL,
  updated_timestamp TIMESTAMP NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
  id TEXT PRIMARY KEY CHECK (id GLOB 'tan-[A-Za-z0-9]*'),
  account_number TEXT NOT NULL,
  user_id TEXT NOT NULL,
  amount NUMERIC(12,2) NOT NULL CHECK (amount >= 0),
  currency TEXT NOT NULL CHECK (currency = 'GBP'),
  type TEXT NOT NULL CHECK (type IN ('deposit', 'withdrawal')),
  reference TEXT,
  created_timestamp TIMESTAMP NOT NULL,
  FOREIGN KEY (account_number) REFERENCES bank_accounts(account_number),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
