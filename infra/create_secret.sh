#!/bin/bash

SECRET_NAME="vp-secret"

if [ -z "$1" ]; then
    echo "Usage: $0 <namespace>"
    exit 1
fi

kubectl get namespace $1 > /dev/null 2>&1

export $(sops -d secret.env | xargs)

kubectl -n=$1 delete secret ${SECRET_NAME} 2> /dev/null
kubectl -n=$1 \
create secret generic ${SECRET_NAME} \
--from-literal="API_PATH=$API_PATH" \
--from-literal="DB_USERNAME=$DB_USERNAME" \
--from-literal="DB_PASSWORD=$DB_PASSWORD" \
--from-literal="DB_URL=$DB_URL" \
--from-literal="DB_NAME=$DB_NAME" \
--from-literal="POSTGRES_PASSWORD=$DB_PASSWORD" \
--from-literal="POSTGRES_USER=$DB_USER" \
--from-literal="POSTGRES_DB=$DB_NAME" \
--from-literal="GOOGLE_CLIENT_ID=$GOOGLE_CLIENT_ID" \
--from-literal="GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET" \
--from-literal="SMTP_HOST=$SMTP_HOST" \
--from-literal="SMTP_PORT=$SMTP_PORT" \
--from-literal="SMTP_USERNAME=$SMTP_USERNAME" \
--from-literal="SMTP_PASSWORD=$SMTP_PASSWORD" \
--from-literal="HOST=$HOST" \
--from-literal="JWT_SECRET=$JWT_SECRET" \
--from-literal="ORIGIN=$ORIGIN" \
--from-literal="PORT=$PORT" \
--from-literal="MINIO_ROOT_USER=$S3_ACCESS_KEY" \
--from-literal="MINIO_ROOT_PASSWORD=$S3_SECRET_KEY" \
--from-literal="S3_ACCESS_KEY=$S3_ACCESS_KEY" \
--from-literal="S3_SECRET_KEY=$S3_SECRET_KEY" \
--from-literal="S3_ENDPOINT=$S3_ENDPOINT" \
--from-literal="S3_PROJECT_BUCKET=$S3_PROJECT_BUCKET" \
--from-literal="S3_TEMP_FOLDER=$S3_TEMP_FOLDER" \
--from-literal="S3_USER_BUCKET=$S3_USER_BUCKET" \
--from-literal="VP_ALLOWED_HEADERS=$VP_ALLOWED_HEADERS" \
--from-literal="VP_ALLOWED_METHODS=$VP_ALLOWED_METHODS" \
--from-literal="VP_ALLOWED_ORIGINS=$VP_ALLOWED_ORIGINS" \
--from-literal="VP_REGISTER_PATTERN=$VP_REGISTER_PATTERN" \
--from-literal="CYPRESS_email=$CYPRESS_email" \
--from-literal="CYPRESS_password=$CYPRESS_password" \
--from-literal="CYPRESS_baseUrl=$CYPRESS_baseUrl"
