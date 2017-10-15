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
    room = $('.form-control').find('option:selected').val();
    userColor = colors[Math.floor(Math.random()*colors.length)];
    webSocket = new WebSocket(serviceLocation + room + '/' + $login.val());
    console.log(serviceLocation + room + '/' + $login.val());
    webSocket.onmessage = onMessage;

    $('#login-header').text('Hello again! :)')
        .css({'color':'#151529'});

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
                    "message":"${$messageField.val()}", 
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

function isExistedUser() {
    let verified = true;
    let data = "login=" + $login.val() + "&password=" + $password.val();

    $.ajax({
        async : false,
        type: "POST",
        url: "doLogin",
        data: data,
        success: function (data) {
            if (data !== "true") {
               verified = false;
            }
        }
    });

    return verified;
}

function isSignedUp() {
    let $loginSignup = $('#login-signup');
    let $passwordSignup = $('#password-signup');
    let $passwordSignupRepat = $('#password-signup-repeat');
    let $emailSignup = $('#email-signup');

    let signedUp = true;

    if ($passwordSignup.val() !== $passwordSignupRepat.val()) {
        signedUp =  false;
    } else {
        let emailRegexp = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if(!emailRegexp.test($emailSignup.val())) {
            signedUp = false;
        } else {
            let data = "login=" + $loginSignup.val()
                + "&password=" + $passwordSignup.val()
                + "&email=" + $emailSignup.val();

            $.ajax({
                async : false,
                type: "POST",
                url: "doSignup",
                data: data,
                success: function (data) {
                    if (data !== "true") {
                        signedUp = false;
                    }
                }
            });
        }
    }

    return signedUp;
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

        if (isExistedUser()) {
            connect();

            $('.chat-room-login-info').text(`Chat #${$login.val()} @${room}`);
            $('.chat-login').hide();
            $('.chat-signup').hide();
            $('.chat-wrapper').show();

            $messageField.focus();
        } else {
            $('#login-header').text('Wrong Credentials =(')
                .css({'color':'#E54D5C'});
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

      if (isSignedUp()) {
          $('.chat-signup-input').hide();
          $('.chat-login').show();
          $('.chat-signup').show();

          $('#login-header').text('Enter your data')
              .css({'color':'#151529'});

          $login.focus();

      } else {
          $('#signup-header').text('Something isn\'t valid :(')
              .css({'color':'#E54D5C'});
      }

    });

    $('#do-chat').submit(function(evt) {
        evt.preventDefault();
        sendMessage()
    });

    $('#leave-room').click(function() {
        leaveRoom();
    });
});