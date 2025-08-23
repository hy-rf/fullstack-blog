cd server
mvn clean package
TIMESTAMP=$(date +%Y%m%d%H%M%S)
scp -i ~/.ssh/key.pem target/lol-1.0.jar user@<server url>:app_dir/lol-${TIMESTAMP}.jar
cd ../client
echo "VERSION=\"$TIMESTAMP\"" >> .env.production
npm run build
tar -czf client-${TIMESTAMP}.tar.gz .output
scp -i ~/.ssh/key.pem client-${TIMESTAMP}.tar.gz user@<server url>:app_dir
ssh -i ~/.ssh/key.pem user@<server url> << EOF
cd app_dir
sudo lsof -t -i:8080 | xargs -r kill -15
nohup java -Xms128m -Xmx256m -XX:MaxRAMPercentage=50 -XX:+UseSerialGC -jar lol-${TIMESTAMP}.jar --spring.profiles.active=prod > /dev/null 2>&1 < /dev/null &

tar -xzf client-${TIMESTAMP}.tar.gz -C ./
rm client-${TIMESTAMP}.tar.gz
mv .output client-${TIMESTAMP}
sudo lsof -t -i:3000 | xargs -r kill -15
nohup node client-${TIMESTAMP}/server/index.mjs > client.log 2>&1 < /dev/null &
EOF
cd ../
rm client/client-${TIMESTAMP}.tar.gz
rm -rf server/target
git restore client/.env.production