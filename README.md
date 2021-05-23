# AkamaiCDN

![spring badge](https://img.shields.io/badge/Spring%20Boot%20Framework-2.3.2.RELEASE-brightgreen)
![docker badge](https://img.shields.io/badge/Docker-20.10.5-blue)

# Project Description

Project for measuring RTT, Packet Loss and Throughput of most popular CDN's - Content Delivery Networks

# CDN

* youtube.com
* akamai.com
* netflix.com
* facebook.com

# [Swagger](https://akamai-cdn.herokuapp.com/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

# Ping endpoints
### Measure and save results of RTT and Packet Loss for 4 CDN's
```http
GET http://localhost:8091/ping/save
```

### Get all Ping results in the database
```http
GET http://localhost:8091/ping/all
```

### Get all Ping results from given timestamp
```http
GET http://localhost:8091/ping/rtt
```
```http
GET http://localhost:8091/ping/packetLoss
```
Params:
| Param | Description |
| :--- | :--- |
| `start_date`  | Start Date expressed as epoch |
| `end_date` | End Date expressed as epoch |

# Throughput endpoints
### Get all Throughput results in the database
```http
GET http://localhost:8091/throughput/all
```

### Get all Throughput results from a given timestamp
```http
GET http://localhost:8091/throughput
```

Params:
| Param | Description |
| :--- | :--- |
| `start_date`  | Start Date expressed as epoch |
| `end_date` | End Date expressed as epoch |



