### 🧠 LiveWatch – Real-Time User Activity Tracker

LiveWatch is a real-time user monitoring system that tracks how many users are currently active on your website or application.
It leverages Spring Boot, Kafka, Redis, and WebSocket on the backend, with a React frontend to visualize live user counts.

------

🚀 Tech Stack
🖥 Backend (Spring Boot)

Spring Boot (Java 17+)

Spring WebSocket (STOMP)

Spring Kafka

Spring Data Redis

Lombok

Gradle Build Tool

🌐 Frontend (React)

React 18+

@stomp/stompjs & sockjs-client for real-time WebSocket communication

TailwindCSS / CSS for UI styling

⚙️ Infrastructure

Apache Kafka (Event Streaming)

Redis (In-Memory Counter Storage)

Zookeeper (Kafka coordination)

----

### 🏗️ Project Architecture

```
        ┌───────────────────────────┐
        │        Frontend (React)   │
        │  - Opens session          │
        │  - Listens via WebSocket  │
        └─────────────┬─────────────┘
                      │
                      ▼
        ┌───────────────────────────┐
        │   Spring Boot API Server  │
        │  - REST: /join, /leave    │
        │  - Publishes to Kafka     │
        │  - WebSocket endpoint     │
        └─────────────┬─────────────┘
                      │
              ┌───────┴────────┐
              │                │
              ▼                ▼
     ┌──────────────────┐  ┌─────────────────┐
     │   Kafka Topic    │  │   Redis Cache   │
     │  ("user-events") │  │ Key: live_users │
     │  joined/left evt │  │ Value: count    │
     └──────────────────┘  └─────────────────┘
              │
              ▼
       ┌────────────────────────┐
       │ Consumer Service       │
       │ - Reads from Kafka     │
       │ - Updates Redis count  │
       │ - Pushes updates via   │
       │   WebSocket to clients │
       └────────────────────────┘

```

### 🧠 How It Works

When a user visits the frontend, an event is sent via Kafka Producer.

Kafka Consumer processes the event and updates the Redis counter (live-user-count).

The updated count is broadcast in real-time through WebSocket using SimpMessagingTemplate.

The React frontend subscribes to /topic/liveCount and displays live updates instantly.


### 🔥 Example Redis Command
```
| Action              | Command                 |
| ------------------- | ----------------------- |
| Set live user count | `SET live-user-count 0` |
| Increment count     | `INCR live-user-count`  |
| Decrement count     | `DECR live-user-count`  |
| Get count           | `GET live-user-count`   |

```

### 🧩 Example Kafka Commands

```
| Action           | Command                                                                                                                 |
| ---------------- | ----------------------------------------------------------------------------------------------------------------------- |
| List topics      | `kafka-topics.bat --list --bootstrap-server localhost:9092`                                                             |
| Create topic     | `kafka-topics.bat --create --topic user-events --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1` |
| Delete topic     | `kafka-topics.bat --delete --topic user-events --bootstrap-server localhost:9092`                                       |
| Consume messages | `kafka-console-consumer.bat --topic user-events --from-beginning --bootstrap-server localhost:9092`                     |

```
----
### 💡 Features

✅ Real-time user count updates

✅ WebSocket-based live feed

✅ Kafka event-driven communication

✅ Redis for fast in-memory counting

✅ Lightweight and scalable architecture

✅ Easy integration with any frontend


---- 
### 🏁 License

This project is licensed under the MIT License – free to use and modify.
