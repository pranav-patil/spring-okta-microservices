Edge Service (Netflix Zuul)
=============

The [Netflix Zuul](https://github.com/Netflix/zuul) is a JVM-based router and server side load balancer. Zuul uses a range of different types of filters that enables to quickly and nimbly apply functionality to the edge service. These filters help to perform Authenticatio/Security, Monitoring traffic, dynamically routing requests to different backend clusters as needed and Load shedding by allocating capacity for each request type. 
It also enables the edge service to respond directly with static response instead of forwarding to internal cluster.


### Zuul Request Lifecycle

Zuul is a series of Filters that are capable of performing a range of actions during the routing of HTTP requests and responses. 
Every Zuul filter has the type which indicates the stage during the routing flow when the Filter will be applied, the order of execution across multiple Filters, conditions for execution and the action to be executed if the Criteria is met. Zuul provides a framework to dynamically read, compile, and run these Filters.
Filters share the state through a RequestContext which is unique to each request instead of communicating directly with each other.
There are several standard Filter types that correspond to the typical lifecycle of a request:

* **PRE** Filters execute before routing to the origin. Example request authentication and logging debug info.
* **ROUTING** Filters handle routing the request to an origin. This is where the origin HTTP request is built and sent using Apache HttpClient or Netflix Ribbon.
* **POST** Filters execute after the request has been routed to the origin. Examples include adding standard HTTP headers to the response, gathering statistics and metrics, and streaming the response from the origin to the client.
* **ERROR** Filters execute when an error occurs during one of the other phases.


   ![Zuul_Request_Lifecycle](images/zuul_request_lifecycle.png)
   
### Running the Edge Service

The Okta security parameters **OKTA_CLIENT_ID**, **OKTA_CLIENT_SECRET** and **OKTA_ISSUER_URL** are required parameters to run edge-service as it enables Okta security and validates Okta access token. Follow the [Okta Setup](/../OKTA.md) and copy **OKTA_CLIENT_ID** and **OKTA_CLIENT_SECRET** from Okta Application configuration settings; and **OKTA_ISSUER_URL** from Authorization Server settings. 

    $ java -jar edge-service/build/libs/edge-service-0.0.1-SNAPSHOT.jar
           -DOKTA_CLIENT_ID=xxxxxxxxxxxx
           -DOKTA_CLIENT_SECRET=xxxxxxxxx-xxxxxxx
           -DOKTA_ISSUER_URL=https://dev-xxxxxx.oktapreview.com/oauth2/xxxxxxxxxxxxxxxxxxxx

