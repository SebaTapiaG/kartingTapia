# Etapa de construcción
FROM node:18 AS builder
WORKDIR /app
COPY . .

# Estas variables de entorno serán usadas en tiempo de build
ARG VITE_BACKEND_SERVER
ARG VITE_BACKEND_PORT

ENV VITE_BACKEND_SERVER=$VITE_BACKEND_SERVER
ENV VITE_BACKEND_PORT=$VITE_BACKEND_PORT

RUN npm install
RUN npm run build

# Etapa de producción con Nginx
FROM nginx:stable-alpine
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
