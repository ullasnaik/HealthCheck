#!/bin/bash

if [ "$1" = "AllAppServerStatus.py" ]
then
	sh /scratch/app/product/fmw/oracle_common/common/bin/wlst.sh AllAppServerStatus.py
elif [ "$1" = "AllDataSourceStatus.py" ]
then
    sh /scratch/app/product/fmw/oracle_common/common/bin/wlst.sh AllDataSourceStatus.py
elif [ "$1" = "AllAppStatus.py" ]
then
    sh /scratch/app/product/fmw/oracle_common/common/bin/wlst.sh AllAppStatus.py
elif [ "$1" = "AllHeapThreadStatus.py" ]
then
    sh /scratch/app/product/fmw/oracle_common/common/bin/wlst.sh AllHeapThreadStatus.py
else
    echo "Please enter valid python script as argument"
fi



