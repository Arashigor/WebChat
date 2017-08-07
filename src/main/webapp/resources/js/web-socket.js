var wsocket;
var serviceLocation = "ws://localhost:8080/WebChat/chat/";
var $nickName;
var $message;
var $chatWindow;
var room = "";

function onMessageReceived(evt) {
    var msg = JSON.parse(evt.data);
    var $messageLine = $('<tr>'
        + '<td class="date">' + msg.date + '</td>'
        + '<td class="user label label-info">' + msg.sender + '</td>'
        + '<td class="message badge">' + msg.message + '</td>'
        + '</tr>');
    $chatWindow.append($messageLine);
}

function sendMessage() {
    var msg = '{"message":"' + $message.val() + '", "sender":"' + $nickName.val() + '", "date":""}';
    wsocket.send(msg);
    $message.val('').focus();
}

function connectToChatserver() {
    room = $('#chatroom').find('option:selected').val();
    wsocket = new WebSocket(serviceLocation + room);
    wsocket.onmessage = onMessageReceived;
}

function leaveRoom() {
    wsocket.close();
    $chatWindow.empty();
    $('.chat-wrapper').hide();
    $('.chat-login').show();
    $('.chat-signup').show();
    $nickName.focus();
}

$(document).ready(function() {
    $nickName = $('#nickname');
    $message = $('#message');
    $chatWindow = $('#response');
    $('.chat-wrapper').hide();
    $nickName.focus();

    $('#btn-login').click(function(evt) {
        evt.preventDefault();
        connectToChatserver();
        $('.chat-room-login-info').text('Chat # '+$nickName.val() + "@" + room);
        $('.chat-login').hide();
        $('.chat-signup').hide();
        $('.chat-wrapper').show();
        $message.focus();
    });

    $('#do-chat').submit(function(evt) {
        evt.preventDefault();
        sendMessage()
    });

    $('#leave-room').click(function(){
        leaveRoom();
    });
});
