# speech-to-text

## Prerequisites
1. JDK 17 or greater
2. Gradle 8 or greater
3. GCP speech-to-text setup with service account keys
4. ffmpeg tool installed
5. PostgreSQL 15 installed (Postgres.app is a good choice)

## Instructions
1. Start a postgreSQL server and ensure a database called "pm" exists
2. Navigate to <code> /path/to/repo/root/core </code>
3. Run any pending migrations with <code>./gradlew flywayMigrate</code>
4. Make sure the <code>nlp.flask.api.url</code> and <code>audio.storage.path</code> in <code>application.yml</code> are set accurately
5. Run this command
<code> GOOGLE_APPLICATION_CREDENTIALS=/path/to/service/account/keys.json ./gradlew bootRun </code> 
