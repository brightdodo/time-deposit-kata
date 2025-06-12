-- src/main/resources/db/migration/V2__insert_test_data.sql
-- Flyway migration to insert test data into time_deposits and withdrawals tables

-- Insert sample time deposits
INSERT INTO time_deposits (id, plan_type, days, balance) VALUES
                                                             (1, 'basic',   40,  1000.00),
                                                             (2, 'student', 365, 2000.00),
                                                             (3, 'premium',  50, 1500.00);

-- Insert sample withdrawals
INSERT INTO withdrawals (id, time_deposit_id, amount, date) VALUES
                                                                (1, 1, 100.00, '2025-06-01'),
                                                                (2, 2, 200.00, '2025-05-15'),
                                                                (3, 3, 150.00, '2025-04-20');