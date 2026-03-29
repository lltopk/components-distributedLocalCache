DROP TABLE IF EXISTS distributed_cache_system_setting_po;
CREATE TABLE distributed_cache_system_setting_po (
                                                     id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
                                                     config_key VARCHAR(50) NOT NULL UNIQUE COMMENT 'configKey',
                                                     config_value VARCHAR(255) NOT NULL COMMENT 'configValue',
                                                     create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
                                                     update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='distributed_cache_system_setting_po';
