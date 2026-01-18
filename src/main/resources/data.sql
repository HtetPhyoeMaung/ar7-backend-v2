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

-- Game Types
INSERT INTO game_type (id, code, description, sort_number)
SELECT 1, 'SLOT', 'စလော့', 0
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM game_type WHERE id = 1 OR code = 'SLOT');

INSERT INTO game_type (id, code, description, sort_number)
SELECT 2, 'LIVE_CASINO', 'ကာစီနို', 0
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM game_type WHERE id = 2 OR code = 'LIVE_CASINO');

INSERT INTO game_type (id, code, description, sort_number)
SELECT 3, 'SPORT_BOOK', 'SPORT', 0
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM game_type WHERE id = 3 OR code = 'SPORT_BOOK');

INSERT INTO game_type (id, code, description, sort_number)
SELECT 8, 'FISHING', 'ငါးပစ်', 0
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM game_type WHERE id = 8 OR code = 'FISHING');

INSERT INTO game_type (id, code, description, sort_number)
SELECT 11, 'ESPORT', 'ESPORT', 0
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM game_type WHERE id = 11 OR code = 'ESPORT');

INSERT INTO game_type (id, code, description, sort_number)
SELECT 15, 'SHAN_BUGYI', 'ရှမ်း - ဘူကြီး', 0
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM game_type WHERE id = 15 OR code = 'SHAN_BUGYI');

-- Hot Games (Initial Data)
INSERT INTO hot_games (id, game_name)
SELECT 1, $${"africanBuffalo":[{"platform":"web","gameUrl":"https://new.buffalo789.com/","description":"African Buffalo","game_code":"b001","game_name":"African Buffalo","game_type":"slot","product_id":0,"product_code":2026,"image_url":"https://ar7imageserversit.sgp1.digitaloceanspaces.com/games/b001/5c2d8e2e-2a94-470b-95e8-f2fc838b7ccd.jpg","support_currency":"MMK","status":"ACTIVE"},{"platform":"web","gameUrl":"https://new.buffalo789.com/","description":"African Buffalo","game_code":"b001","game_name":"African Buffalo","game_type":"slot","product_id":0,"product_code":2026,"image_url":"https://ar7imageserversit.sgp1.digitaloceanspaces.com/games/b001/5c2d8e2e-2a94-470b-95e8-f2fc838b7ccd.jpg","support_currency":"MMK","status":"ACTIVE"},{"platform":"web","gameUrl":"https://scatter.buffalo789.com/","description":"African Buffalo Scatter","game_code":"b003","game_name":"African Buffalo","game_type":"slot","product_id":0,"product_code":2026,"image_url":"https://ar7imageserversit.sgp1.digitaloceanspaces.com/games/b001/759a936b-f776-4f86-9ac9-d5a427584f6f.png","support_currency":"MMK","status":"ACTIVE"},{"platform":"web","gameUrl":"https://scatter.buffalo789.com/","description":"African Buffalo Scatter","game_code":"b003","game_name":"African Buffalo","game_type":"slot","product_id":0,"product_code":2026,"image_url":"https://ar7imageserversit.sgp1.digitaloceanspaces.com/games/b001/759a936b-f776-4f86-9ac9-d5a427584f6f.png","support_currency":"MMK","status":"ACTIVE"},{"platform":"web","gameUrl":"https://buffalo789.com/","description":"African Buffalo Excluding 9","game_code":"b002","game_name":"African Buffalo","game_type":"slot","product_id":0,"product_code":2026,"image_url":"https://ar7imageserversit.sgp1.digitaloceanspaces.com/games/b001/759a936b-f776-4f86-9ac9-d5a427584f6f.png","support_currency":"MMK","status":"ACTIVE"},{"platform":"web","gameUrl":"https://buffalo789.com/","description":"African Buffalo Excluding 9","game_code":"b002","game_name":"African Buffalo","game_type":"slot","product_id":0,"product_code":2026,"image_url":"https://ar7imageserversit.sgp1.digitaloceanspaces.com/games/b001/759a936b-f776-4f86-9ac9-d5a427584f6f.png","support_currency":"MMK","status":"ACTIVE"},{"platform":"web","gameUrl":"https://buffalo789.com/","description":"African Buffalo Excluding 9","game_code":"b002","game_name":"African Buffalo","game_type":"slot","product_id":0,"product_code":2026,"image_url":"https://ar7imageserversit.sgp1.digitaloceanspaces.com/games/b001/759a936b-f776-4f86-9ac9-d5a427584f6f.png","support_currency":"MMK","status":"ACTIVE"},{"platform":"web","gameUrl":"https://buffalo789.com/","description":"African Buffalo Excluding 9","game_code":"b002","game_name":"African Buffalo","game_type":"slot","product_id":0,"product_code":2026,"image_url":"https://ar7imageserversit.sgp1.digitaloceanspaces.com/games/b001/759a936b-f776-4f86-9ac9-d5a427584f6f.png","support_currency":"MMK","status":"ACTIVE"},{"platform":"web","gameUrl":"https://buffalo789.com/","description":"African Buffalo Excluding 9","game_code":"b002","game_name":"African Buffalo","game_type":"slot","product_id":0,"product_code":2026,"image_url":"https://ar7imageserversit.sgp1.digitaloceanspaces.com/games/b001/759a936b-f776-4f86-9ac9-d5a427584f6f.png","support_currency":"MMK","status":"ACTIVE"}],"hotBuffalo":[{"game_code":"vswaysbufking","game_name":"Buffalo King Megaways","game_type":"SLOT","product_id":1185,"product_code":1006,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1006/SLOT/vswaysbufking.png","support_currency":"NGN,COP,AOA,EUR,LBP,CAD,VND,USD,MYR,JPY,MXN,CHF,CZK,PHP,MMK,KRW,ETB,IRR,TND,BDT,IDR,BRL,THB,INR,KES","status":"ACTIVATED"},{"game_code":"vs4096bufking","game_name":"Buffalo King","game_type":"SLOT","product_id":1185,"product_code":1006,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1006/SLOT/vs4096bufking.png","support_currency":"NGN,COP,AOA,EUR,LBP,CAD,VND,USD,MYR,JPY,MXN,CHF,CZK,PHP,MMK,KRW,ETB,IRR,TND,BDT,IDR,BRL,THB,INR,KES","status":"ACTIVATED"},{"game_code":"vswaysbkingasc","game_name":"Buffalo King Untamed Megaways","game_type":"SLOT","product_id":1185,"product_code":1006,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1006/SLOT/vswaysbkingasc.png","support_currency":"NGN,COP,AOA,EUR,LBP,CAD,VND,USD,MYR,JPY,MXN,CHF,CZK,PHP,MMK,KRW,ETB,IRR,TND,BDT,IDR,BRL,THB,INR,KES","status":"ACTIVATED"},{"game_code":"108","game_name":"Buffalo Win","game_type":"SLOT","product_id":1141,"product_code":1007,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1007/SLOT/108.png","support_currency":"IDR2","status":"ACTIVATED"},{"game_code":"14094","game_name":"Bull Treasure","game_type":"SLOT","product_id":1194,"product_code":1085,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1085/SLOT/14094.png","support_currency":"MMK","status":"ACTIVATED"},{"game_code":"259","game_name":"Charge Buffalo ASCENT","game_type":"SLOT","product_id":1144,"product_code":1091,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1091/SLOT/259.png","support_currency":"IDR,INR,MMK,THB,VND,USDT,HKD,NPR,JPY,PKR,USD,KHR2,KRW,VND2,LKR,IDR2,BDT,CNY,MYR,PHP,SGD","status":"ACTIVATED"},{"game_code":"259","game_name":"Charge Buffalo ASCENT","game_type":"SLOT","product_id":1144,"product_code":1091,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1091/SLOT/259.png","support_currency":"IDR,INR,MMK,THB,VND,USDT,HKD,NPR,JPY,PKR,USD,KHR2,KRW,VND2,LKR,IDR2,BDT,CNY,MYR,PHP,SGD","status":"ACTIVATED"},{"game_code":"460","game_name":"3 Charge Buffalo","game_type":"SLOT","product_id":1144,"product_code":1091,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1091/SLOT/460.png","support_currency":"IDR,INR,MMK,THB,VND,USDT,HKD,NPR,JPY,PKR,USD,KHR2,KRW,VND2,LKR,IDR2,BDT,CNY,MYR,PHP,SGD","status":"ACTIVATED"},{"game_code":"vs243chargebull","game_name":"Raging Bull","game_type":"SLOT","product_id":1185,"product_code":1006,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1006/SLOT/vs243chargebull.png","support_currency":"NGN,COP,AOA,EUR,LBP,CAD,VND,USD,MYR,JPY,MXN,CHF,CZK,PHP,MMK,KRW,ETB,IRR,TND,BDT,IDR,BRL,THB,INR,KES","status":"ACTIVATED"},{"game_code":"vs25bullfiesta","game_name":"Bull Fiesta","game_type":"SLOT","product_id":1185,"product_code":1006,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1006/SLOT/vs25bullfiesta.png","support_currency":"NGN,COP,AOA,EUR,LBP,CAD,VND,USD,MYR,JPY,MXN,CHF,CZK,PHP,MMK,KRW,ETB,IRR,TND,BDT,IDR,BRL,THB,INR,KES","status":"ACTIVATED"},{"game_code":"PSS-ON-00096","game_name":"FORTUNE BULL","game_type":"SLOT","product_id":1174,"product_code":1050,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1050/SLOT/PSS-ON-00096.png","support_currency":"USD,HKD,KHR2,MAD,USDT,INR,MYR,KRW,SGD,CHF,LAK2,VND2,GBP,THB,MMK2,CAD,NZD,KHR,TND,CZK,SEK,IDR,VND,JPY,TRY,HUF,NOK,ZAR,ARS,BDT,IDR2,BRL,PHP,EUR,CNY,MMK","status":"ACTIVATED"},{"game_code":"vs20trswild2","game_name":"Black Bull","game_type":"SLOT","product_id":1185,"product_code":1006,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1006/SLOT/vs20trswild2.png","support_currency":"NGN,COP,AOA,EUR,LBP,CAD,VND,USD,MYR,JPY,MXN,CHF,CZK,PHP,MMK,KRW,ETB,IRR,TND,BDT,IDR,BRL,THB,INR,KES","status":"ACTIVATED"},{"game_code":"14092","game_name":"Dragon Soar - Hyper Wild","game_type":"SLOT","product_id":1194,"product_code":1085,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1085/SLOT/14092.png","support_currency":"MMK","status":"ACTIVATED"},{"game_code":"vswaysrhino","game_name":"Great Rhino Megaways","game_type":"SLOT","product_id":1185,"product_code":1006,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1006/SLOT/vswaysrhino.png","support_currency":"NGN,COP,AOA,EUR,LBP,CAD,VND,USD,MYR,JPY,MXN,CHF,CZK,PHP,MMK,KRW,ETB,IRR,TND,BDT,IDR,BRL,THB,INR,KES","status":"ACTIVATED"},{"game_code":"vs20rhinoluxe","game_name":"Great Rhino Deluxe","game_type":"SLOT","product_id":1185,"product_code":1006,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1006/SLOT/vs20rhinoluxe.png","support_currency":"NGN,COP,AOA,EUR,LBP,CAD,VND,USD,MYR,JPY,MXN,CHF,CZK,PHP,MMK,KRW,ETB,IRR,TND,BDT,IDR,BRL,THB,INR,KES","status":"ACTIVATED"},{"game_code":"485","game_name":"3 Coin Wild Horse","game_type":"SLOT","product_id":1144,"product_code":1091,"image_url":"https://images.gscplusmd.com/statics/production/images/games/1091/SLOT/485.png","support_currency":"IDR,INR,MMK,THB,VND,USDT,HKD,NPR,JPY,PKR,USD,KHR2,KRW,VND2,LKR,IDR2,BDT,CNY,MYR,PHP,SGD","status":"ACTIVATED"}],"hotSlot":[],"hotFishing":[],"hotMyanmarGame":[],"hotLiveCasino":[],"hotSport":[]}$$::jsonb::text
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM hot_games WHERE id = 1);