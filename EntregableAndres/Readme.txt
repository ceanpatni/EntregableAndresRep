muy importante que configuren la conexion de la base de datos con usuario y contrasena respectivos

para generar la imagen
docker build -t entregableandres:v1.0.2 .

docker run -p 8082:8080 entregableandres:v1.0.2