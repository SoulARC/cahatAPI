const logInElement = document.querySelector('#login');
const chatElement = document.querySelector('#chat');
const userForm = document.querySelector('#userForm');
const connect = document.querySelector('#connect');
const mainChat = document.querySelector('#main-chat');
const sendDiv = document.querySelector('#sendDiv');
const main = document.querySelector('#main');
let userName = null;
let stomp = null;
let users = null;
const URL = "http://localhost:8080"

function connectSocket(event) {
    userName = document.querySelector('#username').value.trim();
    if (userName) {
        logInElement.classList.add("dis");
        chatElement.classList.remove("dis");
        const socket = new SockJS(URL + '/websocket');
        stomp = Stomp.over(socket);
        stomp.connect({}, connectedDone)
    }
    event.preventDefault()
}

function connectedDone(options) {
    stomp.subscribe("/topic/all", sendMessage)
    stomp.send("/app/chat.logIn", {},
        JSON.stringify({sender: userName, chatType: 'JOIN'})
    )
    connect.classList.add('dis')
}

function sendMessage(payload) {
    const message = JSON.parse(payload.body)
    if (message.chatType === 'JOIN') {
        joinUser(message, "join")
        listUsers()
    } else if (message.chatType === 'LEAVE') {
        joinUser(message, "leave")
        listUsers()
    } else {
        const li = document.createElement('li');
        li.classList.add('sms');
        const image = document.createElement('img');
        image.src = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";
        const span1 = document.createElement('span');
        span1.classList.add('my-message');
        const span2 = document.createElement('span');
        span2.classList.add('user');
        const span2User = document.createTextNode(message.sender);
        span2.appendChild(span2User)
        const span3 = document.createElement('span');
        span3.classList.add('mes');
        const span3Message = document.createTextNode(message.message);
        span3.appendChild(span3Message)
        span1.appendChild(span2);
        span1.appendChild(span3);
        li.appendChild(image);
        li.appendChild(span1);
        mainChat.appendChild(li);
    }
}

function joinUser(message, state) {
    const li1 = document.createElement('li');
    const li2 = document.createElement('li');
    const hr1 = document.createElement('hr');
    const hr2 = document.createElement('hr');
    const messageJoin = document.createTextNode(message.sender + " " + state)
    li1.classList.add('status');
    li1.appendChild(messageJoin)
    li2.appendChild(hr1)
    li2.appendChild(li1)
    li2.appendChild(hr2)
    mainChat.appendChild(li2)
}

function send() {
    const messageUser = document.querySelector('#sms').value.trim()     //
    if (messageUser && stomp) {
        const userMessage = {
            message: messageUser,
            chatType: 'CHAT',
            sender: userName
        }
        stomp.send("/app/chat.send", {}, JSON.stringify(userMessage))
        document.querySelector('#sms').value = '';
    }
}

function listUsers() {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", URL + "/active");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            users = JSON.parse(xhr.responseText);
            showUser(users)
        }
    };
    xhr.send();
}

function showUser(users) {
    document.getElementById('test').remove();
    const mainDiv = document.createElement('div');
    mainDiv.classList.add('abso');
    mainDiv.id = 'test';
    for (let z = 0; z < users.length; z++) {
        const div = document.createElement('div');
        const span1 = document.createElement('span');
        span1.classList.add('name-us');
        const userName = document.createTextNode(users[z].username);
        span1.appendChild(userName);
        const span2 = document.createElement('span');
        const i = document.createElement('i');
        i.classList.add("fas");
        i.classList.add("fa-circle");
        span2.appendChild(i);
        div.appendChild(span1);
        div.appendChild(span2);
        mainDiv.appendChild(div);
    }
    main.appendChild(mainDiv)

}

userForm.addEventListener('submit', connectSocket)
sendDiv.addEventListener('click', send)