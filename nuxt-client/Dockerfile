FROM node:22-alpine

WORKDIR /app

COPY package.json ./

RUN npm i

COPY . .

RUN npm run build

RUN rm -rf node_modules .nuxt

CMD ["node", ".output/server/index.mjs"]
