#!/bin/bash

# ������ arhcomo, ������������� ����� ������� �24
# ������ 1.1
# ���� �������� 15:03 26.05.2014

# =================================================
# ���������.

# ���������� ���� ������ ������� ����� ����� ����� ���������� � �����
DAYOLD="2"

# ������������� � ��� ������������ ������ ���-������
ARH="../bnk.backup/COMO-arh-$(date +%Y%m%d-%H%M%S)-$DAYOLD-dayold.tar"

# ����������� ���������� ���-������
FROMDIR="./&COMO&/"

# =================================================
# ������� �������������
# ������������ ��� ����� �� �������� �����
# ���� ���������� ������� ������������ ���� ��. ������������ ������� find
# =================================================
find $FROMDIR -type f -mtime +$DAYOLD -exec tar --remove-files -vrf $ARH "{}" \; && gzip -1v -S .gz $ARH && echo "CREATE ARCHIV - OK"

exit 0