#!/bin/bash

# Скрипт запуска утилиты конвертирования файлов csvtodbf.jar
# Версия 1.0
# Дата создания 11:44 15.10.2014

# =================================================
# Настройки.
RUN_DIR=`dirname $0`
# FILES_PATH=../TRANSFER
FILES_PATH=./REG.PAYMENT/10101889/7

echo .
echo ==================================================
echo `date '+%Y-%m-%d %H:%M:%S'` - START CONVERSION FILES

# ==================================================
# Поиск файлов выполнения OP*.csv
# Указываем расширения файлов для поиска
find $FILES_PATH -type f  -name 'OP*.csv' | while read file; do

# Запуск утилиты конвертирования файлов csvtodbf.jar вторым параметром указываем конфигурационный файл.
echo java -jar csvtodbf.jar $file ../TRANSFER/OP.ini
java -jar csvtodbf.jar $file ../TRANSFER/OP.ini
done

# ==================================================
# Поиск файлов выполнения SC*.csv
# Указываем расширения файлов для поиска
find $FILES_PATH -type f  -name 'SC*.csv' | while read file; do

# Запуск утилиты конвертирования файлов csvtodbf.jar вторым параметром указываем конфигурационный файл.
echo java -jar csvtodbf.jar $file ../TRANSFER/SC.ini
java -jar csvtodbf.jar $file ../TRANSFER/SC.ini
done

echo `date '+%Y-%m-%d %H:%M:%S'` - END CONVERSION FILES
echo ==================================================

exit 0