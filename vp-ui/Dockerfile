FROM node:18-alpine AS build
WORKDIR /app
COPY package.json ./
RUN npm install
COPY . ./
RUN npm run build

FROM nginx:1.23.1-alpine
RUN rm -rf /var/www/html/*
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist/vp-ui /var/www/html
