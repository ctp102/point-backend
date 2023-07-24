INSERT INTO USERS(user_id, remain_point, created_date, updated_date)
VALUES(1, 10000, '2023-01-01 00:00:00', '2023-01-01 00:00:00');

INSERT INTO EARN_POINT(earn_point_id, user_id, save_point, remain_point, point_deduct_status, expiration_yn, expired_date)
VALUES(1, 1, 10000, 10000, 'POINT_DEDUCT_ST_AVAILABLE', 'N', '2024-01-01 00:00:00');

INSERT INTO POINT_HISTORY(point_history_id, user_id, point, point_action_type)
VALUES(1, 1, 10000, 'POINT_EARN');