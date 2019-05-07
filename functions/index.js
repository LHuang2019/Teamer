const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.sendFriendRequest = functions.firestore.document("users/{userId}/friend_requests/{requestId}")
    .onCreate((snap, context) => {
        const friendRequest = snap.data()

        var message = {
            data: {
                type : "friendRequest",
                recipientId : friendRequest.recipientUid,
                documentId : friendRequest.uid
            },
            token: friendRequest.recipientToken
        };

        admin.messaging().send(message)
    });

exports.sendMessage = functions.firestore.document("users/{userId}/pending_messages/{requestId}")
    .onCreate((snap, context) => {
        const messageReq = snap.data()

        var message = {
            data: {
                type : "pendingMessage",
                recipientId : messageReq.recipientUid,
                documentId : messageReq.uid
            },
            token: messageReq.recipientToken
        };

        admin.messaging().send(message)
    });