var webSocket;
var serviceLocation = "ws://localhost:8080/WebChat/chat/";
var $login;
var $message;
var $chatHistory;
var room = "";

function onMessageReceived(evt) {
    var msg = JSON.parse(evt.data);
    var $messageLine = $(
          '<div class="response-line">'
            + '<span class="badge date">' + msg.date + '</span>'
            + '<span class="label label-info user" style="background-color:' + msg.userColor + '">' + msg.sender + '</span>'
            + '<span class="message">' + msg.message + '</span>'
        + '</div>');


    $chatHistory.append($messageLine);
}

function connectToChatserver() {
    room = $('#form-login-chatroom').find('option:selected').val();
    webSocket = new WebSocket(serviceLocation + room + '/' + $login.val());
    webSocket.onmessage = onMessageReceived;
}

function sendMessage() {
    var msg;

    if ($message.val().startsWith('#')) {
        msg = '{"message":"' + $message.val() + '", "sender":"' + $login.val() + '", "date":"", "receiver":"' +
            $message.val().substring(1, $message.val().indexOf(" ")) + '"}';
    }
    else {
        msg = '{"message":"' + $message.val() + '", "sender":"' + $login.val() + '", "date":"", "receiver":""}';
    }

    webSocket.send(msg);
    $message.val('').focus();
}

function leaveRoom() {
    webSocket.close();
    $chatHistory.empty();
    $('.chat-wrapper').hide();
    $('.chat-login').show();
    $('.chat-signup').show();
    $login.focus();
}

$(document).ready(function() {
    $login = $('#login');
    $message = $('#message');
    $chatHistory = $('#chat-history');
    $('.chat-wrapper').hide();
    $('.chat-signup-input').hide();

    $login.focus();

    $('#btn-login').click(function (evt) {
        evt.preventDefault();
        connectToChatserver();
        $('.chat-room-login-info').text('Chat # '+$login.val() + "@" + room);
        $('.chat-login').hide();
        $('.chat-signup').hide();
        $('.chat-wrapper').show();
        $message.focus();
    });

    $('#btn-signup').click(function (evt) {
       evt.preventDefault();
       $('.chat-login').hide();
       $('.chat-signup').hide();
       $('.chat-signup-input').show();
    });

    $('#btn-signup-submit').click(function (evt) {
      evt.preventDefault();
      //registration process
    });

    $('#do-chat').submit(function (evt) {
        evt.preventDefault();
        sendMessage()
    });

    $('#leave-room').click(function (evt) {
        leaveRoom();
    });
});
