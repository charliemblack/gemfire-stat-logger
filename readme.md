# Stats Logging

## Overview
The Stats Logging project allows teams to monitor and manage metrics within a VMware GemFire cluster. By deploying the `gemfire-stat-logger` JAR, teams log various statistics and adjust the logging interval dynamically.

## Setup Instructions

1. **Start GemFire**:
    ```shell
    cd scripts
    startGemFire.bat
    ```

2. **Connect to the GemFire Cluster using Gfsh**:
    ```shell
    gfsh
    ```
   You should see an output similar to:
    ```plaintext
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
    ```

3. **Deploy the Stat Logger JAR**:
    ```shell
    deploy --jar build\libs\gemfire-stat-logger-0.2.0.jar
    ```

   Confirm the deployment:
    ```plaintext
    Deploying files: gemfire-stat-logger-0.2.0.jar
    Total file size is: 0.00MB

    Continue?  (Y/n):
    Member  |              JAR              | JAR Location
    ------- | ----------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------
    server1 | gemfire-stat-logger-0.2.0.jar | C:\Users\cblack\dev\projects\samples\gemfire-stat-logger\data\server1\deployments\gemfire-stat-logger\main\lib\gemfire-stat-logger-0.2.0.jar
    server2 | gemfire-stat-logger-0.2.0.jar | C:\Users\cblack\dev\projects\samples\gemfire-stat-logger\data\server2\deployments\gemfire-stat-logger\main\lib\gemfire-stat-logger-0.2.0.jar
    ```

4. **Add Metrics to Log**: You can add any GemFire statistic that you wish to monitor.   Here are two examples:
    - To log `DistributionStats.replyWaitsInProgress`:
        ```shell
        execute function --id=StatLogger --arguments="add DistributionStats.replyWaitsInProgress"
        ```
    - To log `StatSampler.delayDuration`:
        ```shell
        execute function --id=StatLogger --arguments="add StatSampler.delayDuration"
        ```

5. **Set Logging Interval in milliseconds**:
    ```shell
    execute function --id=StatLogger --arguments="interval 5000"
    ```
6. **Clear Logged Metrics and Stop Logging**:
    ```shell
    execute function --id=StatLogger --arguments="clear"
    ```

## Example Logging on Each Member

Here's an example of the logging output you might see on each member:

```plaintext
[info 2024/07/29 20:06:50.266 PDT server1 <StatLogger> tid=0x164] DistributionStats.replyWaitsInProgress = 0
[info 2024/07/29 20:06:51.265 PDT server1 <StatLogger> tid=0x164] StatSampler.delayDuration = 999
[info 2024/07/29 20:06:51.265 PDT server1 <StatLogger> tid=0x164] DistributionStats.replyWaitsInProgress = 0
[info 2024/07/29 20:06:52.268 PDT server1 <StatLogger> tid=0x164] StatSampler.delayDuration = 1013
[info 2024/07/29 20:06:52.268 PDT server1 <StatLogger> tid=0x164] DistributionStats.replyWaitsInProgress = 0
[info 2024/07/29 20:06:53.264 PDT server1 <StatLogger> tid=0x164] StatSampler.delayDuration = 1001
[info 2024/07/29 20:06:53.264 PDT server1 <StatLogger> tid=0x164] DistributionStats.replyWaitsInProgress = 0
[info 2024/07/29 20:06:54.279 PDT server1 <StatLogger> tid=0x164] StatSampler.delayDuration = 1011
```

With these steps, you can easily monitor and manage your GemFire cluster's performance metrics using the Stat Logger tool.