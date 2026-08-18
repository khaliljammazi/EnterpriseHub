# Kafka learning plan

Kafka is reserved for asynchronous business events, not ordinary CRUD. The first implementation should select one event, define its owner and schema, document delivery guarantees, make its consumer idempotent, and test retries and failure handling.

Suggested first slice: publish `sale.completed`, consume it to update reporting, and preserve inventory consistency within the sales transaction before publishing through an outbox pattern.

Do not add Kafka dependencies until this slice is ready to be implemented.
