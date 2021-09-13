alter table book_index add column storage_type varchar(10) NOT NULL DEFAULT 'db' COMMENT '存储方式' after book_price ;


