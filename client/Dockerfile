FROM node:22-alpine

WORKDIR /app

COPY package.json ./

RUN npm ci

COPY . .

RUN npm run build-sandbox

RUN rm -rf node_modules .nuxt

CMD ["node", ".output/server/index.mjs"]
