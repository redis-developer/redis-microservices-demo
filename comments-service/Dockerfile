FROM node:10

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install
COPY . .

ENV REDIS_URI=redis://redis-service:6379

EXPOSE 8086

CMD [ "node", "server.js", "--CONF_FILE", "./config.prod.json" ]
