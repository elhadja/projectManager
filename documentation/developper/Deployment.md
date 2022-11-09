# Frontend
## Deploy with Netlify
To deploy the fronend app, you just need to push your changes to the production branch (cf avalable environnements).
## staging environnement
* URL: https://project-manager-staging.netlify.app/
* Production branch: staging-front
## Production environnement
* URL: https://elhadjium-projectmanager.netlify.app/
* Production Branch: dfrontend
# Backend
## Deploy with Heroku
### With Github
To deploy the backend app, you just need to push your changes to the production branch (cf avalable environnements). Currently automatic deploy not working, so you have to manualy deploy the production branch from Heroku's site.
Note: **Heroku must be connected to the github repository**
### With Heroku CLI
* Heroku login
* Checkout to the branch you want to deploy (main for example)
* git subtree push --prefix=PMBackend environnement-remote master
## Staging environnement
* URL: https://still-brushlands-21859.herokuapp.com/
* Production branch: main
## Production environnment
* URL: https://mighty-peak-16888.herokuapp.com/
* Production branch: dbackend