-- schema.sql
CREATE TABLE IF NOT EXISTS BANK_ACCOUNT (
    ACCOUNT_NUMBER BIGINT PRIMARY KEY,
    PASSWORD VARCHAR NOT NULL,
    BALANCE INT NOT NULL,
    NEXT_PAYMENT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    SESSION VARCHAR
);


CREATE TABLE IF NOT EXISTS TRANSACTION_HISTORY (
    TRANSACTION_ID INT PRIMARY KEY AUTO_INCREMENT,
    BANK_ACCOUNT INT NOT NULL,
    debit INT NOT NULL,
    credit INT NOT NULL,
    DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (BANK_ACCOUNT) REFERENCES BANK_ACCOUNT(ACCOUNT_NUMBER)
);