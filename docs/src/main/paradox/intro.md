# Introduction

* Goal is to have a central service storing all information that we have for jobs
* Multiple sources might provide data for the jobs


* How would parsing work?
  * We could have some kind of lambda function that is triggered once a job is updated
  * We could parse right away when a job is updated (behind a queue)
* How would other services be informed about jobs that are available?
  * There could be webhooks per tenant
  * The service could be aware of downstream services (I don't think this would be a clever idea) as other services might depend on this one
  * Is it clever if downstream services know about this service? also maybe not as it would be a single point of failure
  * Will webhooks scale the amount of events?
* Naming conventions for metadata?
* 