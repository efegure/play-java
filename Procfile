web: target/universal/stage/bin/play-java -Dhttp.port=${PORT}-Dsmtp.host=smtp.sendgrid.net -Dsmtp.port=587 -Dsmtp.ssl=yes -Dsmtp.user=$SENDGRID_USERNAME -Dsmtp.password=$SENDGRID_PASSWORD -Dplay.evolutions.db.default.autoApply=true -Dplay.crypto.secret=${APP_SECRET} -Ddb.default.driver=org.postgresql.Driver -Ddb.default.url=${DATABASE_URL}
