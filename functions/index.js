const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.sendFriendRequest = functions.firestore.document("users/{userId}/friend_requests/{requestId}")
    .onCreate((snap, context) => {
        const friendRequest = snap.data()

        var message = {
            data: {
                recipientId : friendRequest.recipientUid,
                documentId : friendRequest.uid
            },
            token: friendRequest.recipientToken
        };

        admin.messaging().send(message)
    });