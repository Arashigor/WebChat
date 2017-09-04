let webSocket;
let serviceLocation = 'ws://localhost:8080/WebChat/chat/';

let $login;
let $password;
let $messageField;
let $chatHistory;
let room = '';

let colors = ["red","grey","green","orange","coral","crimson","cyan","yellow"];
let userColor = '';

function onMessage(evt) {
    let receivedMessage = JSON.parse(evt.data);

    let $messageLine = $(
        `<div class="response-line">
              <span class="badge date">${receivedMessage.date}</span>
              <span class="label label-info user" style="background-color:${receivedMessage.userColor}">
                        ${receivedMessage.sender}
              </span>
              <span class="message">${receivedMessage.message}</span>
           </div>`);

    $chatHistory.append($messageLine);

    //scroll
    let obj = document.getElementById("chat-history");
    obj.scrollTop = obj.scrollHeight;
}

function connect() {
    room = $('#form-login-chatroom').find('option:selected').val();
    userColor = colors[Math.floor(Math.random()*colors.length)];
    webSocket = new WebSocket(serviceLocation + 'room/' + $login.val());
    webSocket.onmessage = onMessage;
}

function sendMessage() {
    webSocket.send(formatMessage($messageField.val()));
    $messageField.val('').focus();
}

function formatMessage(unformattedMessage) {
    let formattedMessage;

    let date = new Date();
    let dateStr = `${date.getHours()}:${date.getMinutes()} ${date.getDate()}/${date.getMonth() + 1}`;

    if (unformattedMessage.startsWith('#')) {
        formattedMessage = `{
                    "message":"${$messageField.val().substring($messageField.val().indexOf(" "))}", 
                    "sender":"${$login.val()}",
                    "userColor":"${userColor}", 
                    "date":"${dateStr}", 
                    "receiver":"${$messageField.val().substring(1, $messageField.val().indexOf(" "))}"
                }`;
    } else {
        formattedMessage = `{
                    "message":"${$messageField.val().substring($messageField.val().indexOf(" "))}", 
                    "sender":"${$login.val()}",
                    "userColor":"${userColor}", 
                    "date":"${dateStr}", 
                    "receiver":""
                }`;
    }

    return formattedMessage;
}

function leaveRoom() {
    webSocket.close();
    $chatHistory.empty();
    $('.chat-wrapper').hide();
    $('.chat-login').show();
    $('.chat-signup').show();
    $login.focus();
}

function verifyUser() {
    return false;
}

$(document).ready(function() {
    $login = $('#login');
    $password = $('#password');
    $messageField = $('#message-field');
    $chatHistory = $('#chat-history');
    $('.chat-wrapper').hide();
    $('.chat-signup-input').hide();

    $login.focus();

    $('#btn-login').click(function(evt) {
        evt.preventDefault();

        if (verifyUser()) {
            connect();

            $('.chat-room-login-info').text(`Chat #${$login.val()} @${room}`);
            $('.chat-login').hide();
            $('.chat-signup').hide();
            $('.chat-wrapper').show();

            $messageField.focus();
        } else {
            $('.form-login-heading').text('Wrong Credentials =(');
        }
    });

    $('#btn-signup').click(function(evt) {
       evt.preventDefault();
       $('.chat-login').hide();
       $('.chat-signup').hide();
       $('.chat-signup-input').show();
    });

    $('#btn-signup-submit').click(function(evt) {
      evt.preventDefault();
      //registration process
    });

    $('#do-chat').submit(function(evt) {
        evt.preventDefault();
        sendMessage()
    });

    $('#leave-room').click(function() {
        leaveRoom();
    });
});
