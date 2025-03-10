Microservices:


Engineer tasks -> 

Problem statement:
1. Designed for field engineers used to fix the issues of tower/network/power/electronic stuff.

Getting the tasks:
2. Managers/controllers adding the task into upstream, based on logic they were assigning the tasks to engineers

Sending the tasks:
3. Engineer login in android/browser(react native), /refresh and got 10 tasks
4. /travel /taskExecute /close - for all tasks
5. /submitTimesheet - sending to upstream

Description:
- We have two source systems(Upstream) FQ and MetQ then our application means ABC.com getting data into services
- Using springboot, postgres database, gitlab, jenkins, aws(ecr, eks - kubernetes service), promethues and grafana as monitoring tools
- Using android and react native as downstream and getting hits from thsi to out ABC.com application microservices
- Having multiple microservices name as CheckVersionAndLogging, AbcCoreSvc, AbcTaskService, LogAppException, PushNotification, TaskCoreSvc, TimesheetSvc, AbsenceDiary.
- Whenever hits aws api gateway triggers first then it call aws lambda for api request jwt token authorization and based on that navigating request to ABC.com microservices
- Our TimesheetSvc and TaskCoreSvc whenever get hits from downstream through aws apigateway, it call the upstream url using resttemplate and in return getting response which will stored in our db and also passes to downstream


- CheckVersionAndLogging - Self
    - /versionCheck -> checkVersion
    - APP_RELEASE_MASTER, EAS_LOG_VERSIONING
- EasTierList
    - /EasTierList/api -> getDropdownList, enrichment
    - EAS_TIER_MASTER
    - CircuitBreaker used only retry mechanism
- EngCoreSvc
    - /EngCoreServices -> /services/signOn/{engineerId}, /services/signOff/{engineerId}, /services/identify/{engineerId}/{device}
    - ABC_CLIENT_AUDITREQ, ENGINEER, ENGINEER_ROASTER, ENGINEER_TRACKING, ABC_ENG_AUDITREQ, TASKDATA
- EngTaskService
    - /EngTaskServices/services -> memoReceipt/{engineerId}, lunchReport/{engineerId}, getRecommended/{engineerId}
- LogAppException
    - /logAppException
    - LOG_APP_EXCEPTION
- PushNotification
    - /PushNotification/api -> /addTokenRequest, /sendTokenToFCMBaseOnOUC, /getNotification, /changeStatus, /signOffNotification, /signOnNotification, /callOutNotification
    - ABC_ENGINEER_TOKEN, NOTIFICATION_DETAILS(Many to one), ABC_NOTIFICATION_REQUESTS
- TaskCoreSvc - self
    - /TaskCoreServices/services -> /taskExecute/{engineerId}, /travelToSite/{engineerId}, /closeComplete/{easKey}, /uploadFiles/{easKey}
    - ABC_CLIENT_AUDITREQ, ABC_ENG_AUDITREQ, ENGINEER, TASKDATA
- TimesheetSvc
    - /TimesheetServices/services -> /TS_Mediator_PS
    - ALLOWANCES, ABC_CLIENT_AUDITREQ, ABC_ENG_AUDITREQ, ENGINEER, ENGINEER_ROASTER, ENGINEER_TIMESHEET, ENGINEER_TRACKING, TASKDATA
- UtilitiesSvc
    - service - JsonProcessor
- FSLite - Self
    - /fslite -> /einload, /searchtaskid, /pintask, /changetaskstatus, /domainload, /taskspage, /filtertask, /loadexchange1141code
    - SELF_PINNING_REQUESTS, EXCHANGECODE1141
- AbsenceDiary
    - /absencediary -> /usersignon, /viewabsence, /absencemodify, /bookabsence, /viewabsencesummary, /sickmodify, /viewsickdetails, /cancelabsence, /returntowork, /loadsickreasoncodes
    - ABSENCE_DIARY_REQUESTS
- PowerNocQrApp - Self
    - /powernocqrcodeapp/inventory -> /equipment/{primId}, /equipment/{primId}/notes, /equipment/installation, /characteristics/{primId}, /sub-assemblies/{primId}
    - NOC_QR_CODE_REQUESTS
- ECM(Elastic Capacity Management)
    - /history, /search(Name, 1141Code), /Floor, 
    - Room - DP, AHU,   
    - Suite - Multiple


- CheckVersion:
    - log the request in EAS_LOG_VERSIONING for future use
    - check the version in app release master and give the response for force upgrade

- TaskCoreSvc:
    - used for updating the status of the tasks
    - log the request in ABC_CLIENT_AUDITREQ 
    - use restTemplate and call the upstream api and used circuitbreaker here only
    - after getting response update same in ABC_CLIENT_AUDITREQ
    - send the response to downstream
    - Optional - TaskNotFoundException

    - Used Factory Design pattern -> taskExcecute/travelToSite/closeComplete/closeIncomplete

- Fslite:
    - used for listing the available tasks and pinning it
    - log the request in SELF_PINNING_REQUESTS 
    - use restTemplate and call the upstream api and get the tasks
    - after getting response update same in SELF_PINNING_REQUESTS
    - send the response to downstream

- PowerNocQrApp(SHREEMS - In house):
    - used for updating the equipment status(Characteristics management), notes
    - log the request in NOC_QR_CODE_REQUESTS 
    - use restTemplate and call the upstream api and get the tasks
    - after getting response update same in NOC_QR_CODE_REQUESTS
    - send the response to downstream


                        
High-Level Architecture:

                                +------------+
                                |  Clients   |
                                | (Android   |
                                |  & React   +
                                |  Native)   |         
                                +----v------+         
                                     |                                                                              
                                     |                                                                             
                                     |                                                                              
        +----------------------------v-------------------------------------------+                          
        |                        API Gateway (AWS)                                |                          
        |                                                                         |                          
        |                       +-------------------+                             |                          
        |                       |   JWT Authorization|                            |                          
        |                       |     (Lambda)       |                            |                          
        |                       +----------^--------+                             |                          
        |                                                                         |                          
        |                                                                         |
        +-------------------------------------------------------------------------+
        `                                    |
    +----------------------------------------v-----------------------------------------------------+
    |                            AWS EKS (Kubernetes Cluster)  
    |
    |    ABC.com Application (Spring Boot)                                        |
    |   +-------------------------+------------------------+---------------------------+--------+  |
    |   | CheckVersionAndLogging  |    AbcCoreSvc      |     AbcTaskService     | LogAppException  |
    |   +-------------------------+------------------------+---------------------------+--- ----+  |
    |   | PushNotification        |    TaskCoreSvc     |       TimesheetSvc     | AbsenceDiary     | 
    |   +-------------------------+------------------------+--------------------------+------------|
    |                                                                                                   
    |       +-------------------+                                                                  |
    |       |   PostgreSQL DB   |                                                                  |     
    |       |   (Containers)    |                                                                  |
    |       +-------------------+                                                                  |
    |                                                                                              |
    |                                                                                              |
    | +--------------+                                              +-------------+                |
    | | AWS CloudWatch|<--------------> Metrics & Logs <--------->  |Prometheus   |                |
    | +--------------+                                              | & Grafana   |                |
    |                                                               +-------------+                |
    +------------------------------------------------------------------------------------------------+
                        |
                        |   (RestTemplate calls to Upstream systems, DB storage)
                        |
                        v
                +--------+                             +--------+
                |        |                             |        |
                |   FQ   | --------------------------->|  MetQ  |
                |        |                             |        |
                +--------+                             +--------+



Faberlounge:

- @Transactional - Remove from cart and Add into orders table
- Filter
- Sort
- Wishlist
- @qualifier - for notifications
- CompletableFuture - Calling products and discount from another service

Absence Management:


