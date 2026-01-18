INSERT INTO game_bank_setting (id, agent_code, call_back_url, agent_id)
VALUES (1, '86a6b626-e245-41b4-b167-6c873829219e', 'https://api.gamebank.vip', 'PTlGwa6V');

-- BCrypt hash for password "12345678": $2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK
-- Default secret_code: 123456

-- Create units records (using high IDs to avoid conflicts)
INSERT INTO units (id, main_unit, game_unit, promotion_unit, tickets, turn_amount, total_bet_unit) VALUES
(999991, 0, 0, 0, 0, 0, 0), -- Admin
(999992, 0, 0, 0, 0, 0, 0), (999993, 0, 0, 0, 0, 0, 0), -- SeniorMaster
(999994, 0, 0, 0, 0, 0, 0), (999995, 0, 0, 0, 0, 0, 0), -- Master
(999996, 0, 0, 0, 0, 0, 0), (999997, 0, 0, 0, 0, 0, 0), -- AffiliateAgent
(999998, 0, 0, 0, 0, 0, 0), (999999, 0, 0, 0, 0, 0, 0), -- Agent
(999981, 0, 0, 0, 0, 0, 0), (999982, 0, 0, 0, 0, 0, 0), (999983, 0, 0, 0, 0, 0, 0),
(999984, 0, 0, 0, 0, 0, 0), (999985, 0, 0, 0, 0, 0, 0), (999986, 0, 0, 0, 0, 0, 0),
(999987, 0, 0, 0, 0, 0, 0), (999988, 0, 0, 0, 0, 0, 0), (999989, 0, 0, 0, 0, 0, 0), -- Users
(999971, 0, 0, 0, 0, 0, 0), (999972, 0, 0, 0, 0, 0, 0), (999973, 0, 0, 0, 0, 0, 0),
(999974, 0, 0, 0, 0, 0, 0), (999975, 0, 0, 0, 0, 0, 0), (999976, 0, 0, 0, 0, 0, 0),
(999977, 0, 0, 0, 0, 0, 0), (999978, 0, 0, 0, 0, 0, 0), (999979, 0, 0, 0, 0, 0, 0); -- Streamers

-- Admin
INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer)
SELECT 'Admin', 'AY0000009', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'ADMIN', '123456', true, 999991, false
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = 'AY0000009');

-- SeniorMaster (SE)
INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer)
SELECT 'SeniorMaster Dev', 'SE0000001', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'SENIORMASTER', '123456', true, 999992, false
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = 'SE0000001');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer)
SELECT 'SeniorMaster Default', 'SE0000009', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'SENIORMASTER', '123456', true, 999993, false
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = 'SE0000009');

-- Master (MS)
INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer)
SELECT 'Master Dev', 'MS0000001', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'MASTER', '123456', true, 999994, false
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = 'MS0000001');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer)
SELECT 'Master Default', 'MS0000009', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'MASTER', '123456', true, 999995, false
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = 'MS0000009');

-- AffiliateAgent (AFG) - All must be under AG0000009
INSERT INTO users (full_name, ar7_id, password, role, code, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'AffiliateAgent Dev', 'AFG0000001', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'AFFILIATEAGENT', 'DEV', '123456', true, 999996, false, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE code = 'DEV');

INSERT INTO users (full_name, ar7_id, password, role, code, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'AffiliateAgent Default', 'AFG0000009', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'AFFILIATEAGENT', '009', '123456', true, 999997, false, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE code = '009');

-- Agent (AG)
INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer)
SELECT 'Agent Dev', 'AG0000001', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'AGENT', '123456', true, 999998, false
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = 'AG0000001');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer)
SELECT 'Agent Default', 'AG0000009', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'AGENT', '123456', true, 999999, false
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = 'AG0000009');

-- Developer Users (0000001 to 0000009) - All under AG0000001
INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Developer 1', '0000001', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999981, false, 'AG0000001'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000001');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Developer 2', '0000002', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999982, false, 'AG0000001'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000002');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Developer 3', '0000003', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999983, false, 'AG0000001'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000003');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Developer 4', '0000004', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999984, false, 'AG0000001'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000004');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Developer 5', '0000005', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999985, false, 'AG0000001'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000005');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Developer 6', '0000006', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999986, false, 'AG0000001'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000006');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Developer 7', '0000007', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999987, false, 'AG0000001'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000007');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Developer 8', '0000008', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999988, false, 'AG0000001'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000008');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Developer 9', '0000009', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999989, false, 'AG0000001'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000009');

-- Streamers (streamer = 1)
INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Streamer 1', '0000000', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999971, true, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '0000000');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Streamer 2', '1111111', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999972, true, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '1111111');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Streamer 3', '3333333', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999973, true, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '3333333');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Streamer 4', '4444444', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999974, true, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '4444444');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Streamer 5', '5555555', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999975, true, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '5555555');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Streamer 6', '6666666', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999976, true, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '6666666');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Streamer 7', '7777777', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999977, true, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '7777777');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Streamer 8', '8888888', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999978, true, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '8888888');

INSERT INTO users (full_name, ar7_id, password, role, secret_code, status, unit_id, streamer, parent_user_id)
SELECT 'Streamer 9', '9999999', '$2a$12$Wy3EWzVmGvHqosJTpeIoTO0ELkT.CGUjYeuygVw/lCvEnvIXpN0hK', 'USER', '123456', true, 999979, true, 'AG0000009'
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM users WHERE ar7_id = '9999999');

-- Component permissions for ADMIN
INSERT INTO component (role, permission_code)
SELECT 'ADMIN', '{"01000": "Dashboard", "02000": "Profile", "03000": "DownLineUserList", "04000": "UserManagement", "05000": "Admin Unit History", "06000": "Unit Transaction History", "07000": "GameType", "08000": "Game Provider", "09000": "Bank Type", "10000": "Bank Name", "11000": "Bank Account", "12000": "Bank Type Auth", "13000": "Bank Name Auth", "14000": "User Report", "15000": "Deposit", "16000": "Withdraw", "17000": "Calculate Commission", "18000": "Setting", "19000": "Term and Condition"}'::json
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM component WHERE role = 'ADMIN');

-- Component permissions for SENIORMASTER
INSERT INTO component (role, permission_code)
SELECT 'SENIORMASTER', '{"01000": "Dashboard", "02000": "Profile", "03000": "DownLineUserList", "04000": "UserManagement", "06000": "Unit Transaction History", "11000": "Bank Account", "12000": "Bank Type Auth", "13000": "Bank Name Auth", "14000": "User Report", "15000": "Deposit", "16000": "Withdraw", "17000": "Calculate Commission", "18000": "Setting", "19000": "Term and Condition"}'::json
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM component WHERE role = 'SENIORMASTER');

-- Component permissions for MASTER
INSERT INTO component (role, permission_code)
SELECT 'MASTER', '{"01000": "Dashboard", "02000": "Profile", "03000": "DownLineUserList", "04000": "UserManagement", "06000": "Unit Transaction History", "11000": "Bank Account", "12000": "Bank Type Auth", "13000": "Bank Name Auth", "14000": "User Report", "15000": "Deposit", "16000": "Withdraw", "17000": "Calculate Commission", "18000": "Setting", "19000": "Term and Condition"}'::json
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM component WHERE role = 'MASTER');

-- Component permissions for AGENT
INSERT INTO component (role, permission_code)
SELECT 'AGENT', '{"01000": "Dashboard", "02000": "Profile", "03000": "DownLineUserList", "04000": "UserManagement", "06000": "Unit Transaction History", "11000": "Bank Account", "12000": "Bank Type Auth", "13000": "Bank Name Auth", "14000": "User Report", "15000": "Deposit", "16000": "Withdraw", "17000": "Calculate Commission", "18000": "Setting", "19000": "Term and Condition"}'::json
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM component WHERE role = 'AGENT');

-- Component permissions for AFFILIATEAGENT
INSERT INTO component (role, permission_code)
SELECT 'AFFILIATEAGENT', '{"01000": "Dashboard", "02000": "Profile", "03000": "DownLineUserList", "14000": "User Report"}'::json
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM component WHERE role = 'AFFILIATEAGENT');
