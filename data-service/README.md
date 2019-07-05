Data Service
=============

Data service provides number of stocks present for a given user account. The stock and user account details are stored in database.

### Running the Data Service

The Okta security parameters **OKTA_CLIENT_ID**, **OKTA_CLIENT_SECRET** and **OKTA_ISSUER_URL** are required parameters to run data-service as it enables Okta security and validates Okta access token. Follow the [Okta Setup](/../OKTA.md) and copy **OKTA_CLIENT_ID** and **OKTA_CLIENT_SECRET** from Okta Application configuration settings; and **OKTA_ISSUER_URL** from Authorization Server settings. 

    $ java -jar data-service/build/libs/data-service-0.0.1-SNAPSHOT.jar
           -DOKTA_CLIENT_ID=xxxxxxxxxxxx
           -DOKTA_CLIENT_SECRET=xxxxxxxxx-xxxxxxx
           -DOKTA_ISSUER_URL=https://dev-xxxxxx.oktapreview.com/oauth2/xxxxxxxxxxxxxxxxxxxx

