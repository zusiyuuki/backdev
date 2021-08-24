INSERT INTO `task_type` VALUES
(1,'緊急','最優先で取り掛かるべきタスク'),
(2,'重要','期限に間に合わせるべきタスク'),
(3,'できれば','今後やってみたいアイデア');

INSERT INTO `task` VALUES
(NULL,1,1,'JUnitを学習','テストの仕方を学習する','2020-07-07 15:00:00'),
(NULL,1,3,'サービスの自作','マイクロサービスを作ってみる','2020-09-13 17:00:00');

INSERT INTO `authority` VALUES
('USER', 10),
('STAFF', 20),
('ADMIN', 30);

INSERT INTO `user` VALUES
(NULL, 'ユーザー1', 'user1@example.com', 'pass1', '1', 'USER', 'key1'),
(NULL, 'ユーザー2', 'user2@example.com', 'pass2', '0', 'USER', 'key2'),
(NULL, 'ユーザー3', 'user3@example.com', 'pass3', '1', 'USER', 'key3'),
(NULL, 'ユーザー4', 'user4@example.com', 'pass4', '1', 'ADMIN', 'key4');
