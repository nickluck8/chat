# Chat Application Service

This is a small chat application service that provides APIs for user authentication, sending and deleting messages, and managing chat rooms.

## Project startup

- you must have Docker installed and running
- `git pull https://github.com/nickluck8/chat.git`
- `mvn spring-boot:run -Pdocker-compose`

## How to use: 
1. POST ``/api/v1/login``
2. You will get auth token which you need to use in each request, if you use included postman collection token will be set automatically
3. GET ``/api/rooms`` in order to get available rooms, there is only one room by default
4. GET `/api/messages?roomId=default` to get messages by roomId, it supports pagination
5. DELETE `api/messages/65f49da54eabc60c5e0edb09` to delete message by id, only owner can delete
6. POST `/api/messages` to send message, body must include content and roomdId



## Authentication

### Login Endpoint

This endpoint allows users to log in and obtain a JWT token for accessing protected resources.

- **URL:** `/api/v1/login`
- **Method:** `POST`
- **Response:**
    - **Status Code:** `200 OK`
    - **Body:**
      ```json
      {
        "accessToken": "example_token"
      }
      ```

## Messages

### Get All Messages

This endpoint retrieves messages from a specific chat room.

- **URL:** `/api/messages`
- **Method:** `GET`
- **Query Parameters:**
    - `roomId`: ID of the chat room
    - `page` (optional, default: `0`): Page number
    - `size` (optional, default: `10`): Number of messages per page
- **Response:**
    - **Status Code:** `200 OK`
    - **Body:** Array of messages

### Send Message

This endpoint allows users to send messages to a chat room.

- **URL:** `/api/messages`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "roomId": "example_room_id",
    "content": "example_message_content"
  }
- **Response:**
    - **Status Code:** `200 OK`
    - **Body:** Array of messages

### Delete Message
This endpoint allows users to delete their own messages.

- **URL:** `/api/messages/{id}`
- **Method:** `DELETE`
- **Path Variable:** `id` - ID of the message to delete
**Response:**
  - **Status Code:** `200 OK`

### Rooms
Get All Rooms
This endpoint retrieves a list of all chat rooms.

- **URL:** `/api/rooms`
- **Method:** `GET`
- **Response:** `Array of chat rooms`


For more information check postman collection