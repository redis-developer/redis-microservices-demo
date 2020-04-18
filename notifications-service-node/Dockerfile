FROM node:10

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install
COPY . .

EXPOSE 8888

CMD [ "node", "server.js", "--CONF_FILE", "./config.prod.json" ]


