# visual-productivty

## Test the application

*All commands below suggest to be executed in the root directory of the project.*

There are two ways to test the application. You can either run the entire app with a single docker-compose or run more "dev friendly" version with a docker-compose which contains the services needed to run the backend and then run the frontend with npm.

Before running any commands be sure to have a .env file with at least the following variables:

```
GOOGLE_CLIENT_ID=<your google client id>
GOOGLE_CLIENT_SECRET=<your google client secret>
```

<br>

### For the full-app version (which is the closest to a production environnement):

Create a file called docker/.env.
Add your own SMTP server (something like Google, ...)
You'll need the following variables:

```
SMTP_HOST=<your smtp host>
SMTP_PORT=<your smtp port
SMTP_USERNAME=<your smtp username>
SMTP_PASSWORD=<your smtp password>
```

And then you can run the full-app by running:

```bash
docker compose -f docker/docker-compose.full-app.yml up -d --build
```

