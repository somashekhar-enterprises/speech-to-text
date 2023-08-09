# speech-to-text

## Prerequisites
1. JDK 17 or greater
2. Gradle 8 or greater
3. GCP speech-to-text setup with service account keys

## Instructions
1. Navigate to <code> /path/to/repo/root/core </code>
2. Make sure the <code>nlp.flask.api.url</code> and <code>audio.storage.path</code> in <code>application.yml</code> are set accurately
3. Run this command
<code> GOOGLE_APPLICATION_CREDENTIALS=/path/to/service/account/keys.json ./gradlew bootRun </code> 