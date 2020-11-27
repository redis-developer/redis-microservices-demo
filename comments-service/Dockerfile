FROM node:10

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install
COPY . .

ENV  REDIS_HOST=redis-service
ENV  REDIS_PORT=6379
ENV  REDIS_PASSWORD=

EXPOSE 8086

CMD [ "node", "server.js", "--CONF_FILE", "./config.prod.json" ]
