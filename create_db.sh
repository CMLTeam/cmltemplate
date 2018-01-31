#!/usr/bin/env bash

echo "
create database cmltemplate CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
grant all privileges on cmltemplate.* to 'cmltemplate'@'%' identified by 'cmltemplate';
flush privileges;
" | mysql -uroot -p
