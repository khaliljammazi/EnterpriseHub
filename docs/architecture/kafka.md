# Kafka: first implemented event

Kafka is reserved for asynchronous business events, not ordinary CRUD. The first learning slice publishes `company.created` after a company has been persisted. A notification consumer receives the event.

## Event schema

```json
{
  "eventId": "9b319d5a-65bc-4cb3-83cd-973726b1d065",
  "companyId": "ad168bbf-275b-4977-83a2-ad327f982904",
  "companyName": "EnterpriseHub",
  "companyType": "STARTUP",
  "occurredAt": "2026-09-30T17:00:00Z"
}
```

The Kafka message key is `companyId`, so events for the same company remain ordered inside one partition. The topic is `company-events`; the example consumer group is `enterprisehub-notifications`.

## Local execution

Start PostgreSQL and Kafka:

```powershell
docker compose up -d postgres kafka
```

Enable Kafka when starting Spring Boot:

```powershell
$env:KAFKA_ENABLED='true'
cd backend
.\mvnw.cmd spring-boot:run
```

Creating a company through `POST /api/companies` publishes the event. The example consumer writes the received notification to the application log.

Kafka is disabled by default, so developers can start the API without a broker. In this mode, a logging adapter replaces the Kafka publisher.

## Production limitation

This first slice saves the company and then sends the event. A crash between those operations can lose the event. The production evolution is a transactional outbox: save the company and outbox record in the same PostgreSQL transaction, publish the outbox asynchronously, and make consumers idempotent using `eventId`. Retry and dead-letter handling must also be defined before production use.

## Product stock event

When a product is created with five units or fewer, or its stock crosses from above five to five or fewer, the backend publishes `StockLowEvent` to `product-events`. The Kafka key is `productId`. Updating an already-low stock from four to three does not publish another alert.

```json
{
  "eventId": "49ffec81-c611-4e01-ac77-62972cfaed10",
  "productId": "977e1407-58a3-4653-bf07-406876e30965",
  "companyId": "ad168bbf-275b-4977-83a2-ad327f982904",
  "sku": "COFFEE-01",
  "remainingQuantity": 5,
  "occurredAt": "2026-10-01T13:00:00Z"
}
```
