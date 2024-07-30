# Stats Logging 

```
> cd scripts
> startGemFire.bat

> gfsh
    _________________________     __
   / _____/ ______/ ______/ /____/ /
  / /  __/ /___  /_____  / _____  /
 / /__/ / ____/  _____/ / /    / /  
/______/_/      /______/_/    /_/    10.1.0

Monitor and Manage VMware GemFire

gfsh>connect
Connecting to Locator at [host=localhost, port=10334] ..
Connecting to Manager at [host=cblack-z01, port=1099] ..
Successfully connected to: [host=cblack-z01, port=1099]

You are connected to a cluster of version 10.1.0.

gfsh>deploy --jar build\libs\gemfire-stat-logger-0.1.0.jar

Deploying files: gemfire-stat-logger-0.1.0.jar
Total file size is: 0.00MB

Continue?  (Y/n):
Member  |           JAR           | JAR Location
------- | ----------------------- | --------------------------------------------------------------------------------------------------------------------------------------
server1 | gemfire-stat-logger.jar | C:\Users\cblack\dev\projects\samples\gemfire-stat-logger\data\server1\deployments\gemfire-stat-logger\main\lib\gemfire-stat-logger-0.1.0.jar
server2 | gemfire-stat-logger.jar | C:\Users\cblack\dev\projects\samples\gemfire-stat-logger\data\server2\deployments\gemfire-stat-logger\main\lib\gemfire-stat-logger-0.1.0.jar

gfsh>execute function --id=StatLogger --arguments="add DistributionStats.replyWaitsInProgress"
Member  | Status | Message
------- | ------ | ------------------------
server1 | OK     | [Logging Metric count 1]
server2 | OK     | [Logging Metric count 1]

gfsh>execute function --id=StatLogger --arguments="add StatSampler.delayDuration"
Member  | Status | Message
------- | ------ | ------------------------
server1 | OK     | [Logging Metric count 2]
server2 | OK     | [Logging Metric count 2]

gfsh>execute function --id=StatLogger --arguments="clear"
Member  | Status | Message
------- | ------ | ----------------------------------
server1 | OK     | [Cleared list and stopped logging]
server2 | OK     | [Cleared list and stopped logging]

gfsh>execute function --id=StatLogger --arguments="cf"
Member  | Status | Message
------- | ------ | --------------
server1 | OK     | [No operation]
server2 | OK     | [No operation]

gfsh>
```
## Example Logging on Each Member  

```
[info 2024/07/29 20:06:50.266 PDT server1 <StatLogger> tid=0x164] DistributionStats.replyWaitsInProgress = 0

[info 2024/07/29 20:06:51.265 PDT server1 <StatLogger> tid=0x164] StatSampler.delayDuration = 999

[info 2024/07/29 20:06:51.265 PDT server1 <StatLogger> tid=0x164] DistributionStats.replyWaitsInProgress = 0

[info 2024/07/29 20:06:52.268 PDT server1 <StatLogger> tid=0x164] StatSampler.delayDuration = 1013

[info 2024/07/29 20:06:52.268 PDT server1 <StatLogger> tid=0x164] DistributionStats.replyWaitsInProgress = 0

[info 2024/07/29 20:06:53.264 PDT server1 <StatLogger> tid=0x164] StatSampler.delayDuration = 1001

[info 2024/07/29 20:06:53.264 PDT server1 <StatLogger> tid=0x164] DistributionStats.replyWaitsInProgress = 0

[info 2024/07/29 20:06:54.279 PDT server1 <StatLogger> tid=0x164] StatSampler.delayDuration = 1011
```
