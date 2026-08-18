# WebRTC learning plan

The Angular client will manage media devices and peer connections. The Spring Boot backend will authenticate participants and exchange SDP offers, answers, and ICE candidates through a signaling channel. A TURN server will be required for reliable connectivity outside local networks.

Start with a one-to-one call, then add screen sharing and connection recovery. Evaluate an SFU before implementing larger rooms.
