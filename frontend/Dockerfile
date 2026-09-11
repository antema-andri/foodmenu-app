# Étape 1 : build Angular
FROM node:20-alpine AS build

WORKDIR /app

# Copier package.json et installer dépendances
COPY package*.json ./
RUN npm ci --legacy-peer-deps

# Copier le reste du projet
COPY . .

# Build Angular (production)
RUN npm run build -- --configuration production


# Étape 2 : serveur Nginx
FROM nginx:alpine

# Supprimer config par défaut
RUN rm -rf /usr/share/nginx/html/*

# Copier le build Angular
# COPY --from=build /app/dist/menu-app/browser /usr/share/nginx/html
COPY --from=build /app/dist/menu-choice/browser /usr/share/nginx/html

# Config Angular routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exposer le port
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]