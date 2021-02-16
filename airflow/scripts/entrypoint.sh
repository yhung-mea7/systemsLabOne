#!/usr/bin/env bash

TRY_LOOP="20"

: "${MYSQL_HOST:="airflowdb"}"
: "${MYSQL_PORT:="3306"}"
: "${MYSQL_USER:="root"}"
: "${MYSQL_PASSWORD:="password"}"
: "${MYSQL_DB:="airflowdb"}"
: "${MYSQL_ROOT_PASSWORD:="password"}"

# Defaults and back-compat
: "${AIRFLOW_HOME:="/usr/local/airflow"}"
: "${AIRFLOW__CORE__FERNET_KEY:=${FERNET_KEY:=$(python -c "from cryptography.fernet import Fernet; FERNET_KEY = Fernet.generate_key().decode(); print(FERNET_KEY)")}}"
: "${AIRFLOW__CORE__EXECUTOR:=LocalExecutor}"

# Airflow concurrency management
: "${AIRFLOW__CORE__PARALLELISM:=16}" # maximum active tasks anywhere.
: "${AIRFLOW__CORE__DAG_CONCURRENCY:=16}" # maximum tasks that can be scheduled at once, per DAG
: "${AIRFLOW__CORE__MAX_ACTIVE_RUN:=8}" # per DAG

# Data set connection vars
: "${AIRFLOW_CONN_MY_SRC_DB:=mysql://root:password@db:3306/lawncare}"
: "${AIRFLOW_CONN_MY_DST_DB:=cassandra://cassandra:9042/lawncare_analytics}"

# Other
AIRFLOW__CORE__LOAD_EXAMPLES=False
AIRFLOW__CORE__SQL_ALCHEMY_CONN="mysql://$MYSQL_USER:$MYSQL_PASSWORD@$MYSQL_HOST:$MYSQL_PORT/$MYSQL_DB"
AIRFLOW__CELERY__RESULT_BACKEND="mysql://$MYSQL_USER:$MYSQL_PASSWORD@$MYSQL_HOST:$MYSQL_PORT/$MYSQL_DB"

export \
  AIRFLOW_HOME \
  AIRFLOW__CORE__EXECUTOR \
  AIRFLOW__CORE__FERNET_KEY \
  AIRFLOW__CORE__PARALLELISM \
  AIRFLOW__CORE__DAG_CONCURRENCY \
  AIRFLOW__CORE__MAX_ACTIVE_RUN \
  AIRFLOW_CONN_MY_SRC_DB \
  AIRFLOW_CONN_MY_DST_DB \
  AIRFLOW__CORE__LOAD_EXAMPLES \
  AIRFLOW__CORE__SQL_ALCHEMY_CONN \
  AIRFLOW__CELERY__RESULT_BACKEND

wait_for_port() {
  local name="$1" host="$2" port="$3"
  local j=0
  while ! nc -z "$host" "$port" >/dev/null 2>&1 < /dev/null; do
    j=$((j+1))
    if [ $j -ge $TRY_LOOP ]; then
      echo >&2 "$(date) - $host:$port still not reachable, giving up"
      exit 1
    fi
    echo "$(date) - waiting for $name... $j/$TRY_LOOP"
    sleep 5
  done
}

echo "Connecting to airflow management db on $MYSQL_HOST port $MYSQL_PORT"
wait_for_port "MySQL" "$MYSQL_HOST" "$MYSQL_PORT"

echo "Starting airflow initdb"
airflow initdb
# With the Local and Sequential executors it should all run in one container.
echo "Starting airflow scheduler"
airflow scheduler &
echo "Starting airflow webserver"
exec airflow webserver
