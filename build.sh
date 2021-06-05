echo 'Building project'
mvn clean install -DskipTests
echo 'Build Completed. Jar Created'
echo 'Creating docker image'
docker build -t yogakani10/qrlogin:latest -f ../../practice/dockerfiles/qrLogin/qrLogin.txt .
echo 'Latest image generated'
docker images
echo 'Pushing image to docker hub'
docker push yogakani10/qrlogin:latest
echo 'Image pushed successfully'
