// let username = null;
// let socket = null;
// let stompClient = null;
//
// function login() {
//     const usernameInput = document.getElementById('usernameInput');
//     username = usernameInput.value.trim();
//     if (username) {
//         document.getElementById('loginForm').style.display = 'none';
//         document.getElementById('chat').style.display = 'block';
//         document.getElementById('messageForm').style.display = 'block';
//         console.log("Calling connect");
//         connect();
//     }
// }
//
// function connect() {
//     socket = new SockJS('/chat');
//     stompClient = Stomp.over(socket);
//     stompClient.connect({}, function (frame) {
//         console.log('Connected: ' + frame);
//         stompClient.subscribe('/topic/public', function (message) {
//             showMessage(JSON.parse(message.body));
//         });
//     },
//         function (error) {
//             console.log('Connection error: ' + error);
//             setTimeout(connect, 5000);
//         });
// }
//
// function sendMessage() {
//     const messageInput = document.getElementById('messageInput');
//     const message = messageInput.value.trim();
//     if (message) {
//         const chatMessage = {
//             content: message,
//             sender: username
//         };
//         stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
//         messageInput.value = '';
//     }
// }
//
// function showMessage(message) {
//     console.log('Received: ' + message);
//     const chatDiv = document.getElementById('chat');
//     const messageElement = document.createElement('div');
//     messageElement.textContent = message.sender + ': ' + message.content;
//     chatDiv.appendChild(messageElement);
//     chatDiv.scrollTop = chatDiv.scrollHeight;
// }
//
// document.getElementById('loginButton').addEventListener('click', function (e) {
//     e.preventDefault();
//     login();
// });
//
// document.getElementById('messageForm').addEventListener('submit', function (e) {
//     e.preventDefault();
//     sendMessage();
// });

// Variables for tracking script loading status
let scriptCheckInterval = null;

// Check if external libraries are loaded
function checkExternalLibraries() {
    console.log("Checking external libraries...");
    if (typeof SockJS !== 'undefined' && typeof Stomp !== 'undefined') {
        console.log("SockJS and Stomp libraries loaded successfully");
        if (scriptCheckInterval) {
            clearInterval(scriptCheckInterval);
            scriptCheckInterval = null;
        }
    } else {
        console.warn("Waiting for SockJS and Stomp libraries to load...");
    }
}

// Start checking when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    console.log("DOM loaded, ready for interactions");
    
    // Check libraries every second until they're loaded
    scriptCheckInterval = setInterval(checkExternalLibraries, 1000);
    
    // Stop checking after 10 seconds regardless
    setTimeout(function() {
        if (scriptCheckInterval) {
            clearInterval(scriptCheckInterval);
            scriptCheckInterval = null;
            console.error("Timed out waiting for libraries to load");
        }
    }, 10000);
});

let stompClient;
let username;

function connect() {
    try {
        username = document.getElementById("usernameInput").value;
        if (!username) return;

        document.getElementById("login-section").style.display = "none";
        document.getElementById("main").style.display = "block";
        document.getElementById("welcome").innerText = "Welcome, " + username;

        if (username === "admin") {
            document.getElementById("admin-section").style.display = "block";
        }

        // Check if SockJS is loaded
        if (typeof SockJS === 'undefined') {
            console.error("SockJS is not defined. Waiting for script to load...");
            // Try again after a short delay
            setTimeout(function() {
                if (typeof SockJS === 'undefined') {
                    console.error("SockJS still not loaded after delay. Cannot connect.");
                    alert("Error connecting: SockJS library not loaded correctly. Please refresh the page.");
                    return;
                }
                console.log("SockJS loaded after delay, continuing connection...");
                establishConnection();
            }, 1000);
            return;
        }
        
        establishConnection();
    } catch (error) {
        console.error("Error in connect function:", error);
    }
}

function establishConnection() {
    console.log("Connecting with SockJS...");
    try {
        let socket = new SockJS("/chat");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function () {
            console.log("Connected to WebSocket server");
            stompClient.subscribe("/topic/auctions", function (message) {
                console.log("Received auction message:", message.body);
                let data = JSON.parse(message.body);
                updateAuction(data);
            });
        }, function(error) {
            console.error("Connection error:", error);
        });
    } catch (error) {
        console.error("Error establishing connection:", error);
    }
}

function createAuction() {
    try {
        let itemName = document.getElementById("itemName").value;
        let minPrice = parseFloat(document.getElementById("minPrice").value);
        let duration = parseInt(document.getElementById("duration").value);

        console.log("Creating auction:", itemName, minPrice, duration);
        
        if (!stompClient) {
            console.error("Not connected to WebSocket! stompClient is undefined");
            alert("Not connected to the server. Please try logging in again.");
            return;
        }
        
        if (!stompClient.connected) {
            console.error("WebSocket connection not established!");
            alert("Connection to server not established. Please try logging in again.");
            return;
        }

        stompClient.send("/app/auction", {}, JSON.stringify({
            type: "create",
            sender: username,
            itemName: itemName,
            price: minPrice,
            duration: duration
        }));
        console.log("Auction creation message sent");
        
        // Clear the form
        document.getElementById("itemName").value = "";
        document.getElementById("minPrice").value = "";
        document.getElementById("duration").value = "";
    } catch (error) {
        console.error("Error in createAuction:", error);
        alert("Error creating auction: " + error.message);
    }
}

function bid(auctionId) {
    try {
        let price = parseFloat(document.getElementById("bid-" + auctionId).value);
        
        if (isNaN(price) || price <= 0) {
            alert("Please enter a valid bid amount");
            return;
        }
        
        stompClient.send("/app/auction", {}, JSON.stringify({
            type: "bid",
            sender: username,
            auctionId: auctionId,
            price: price
        }));
        
        // Clear the input
        document.getElementById("bid-" + auctionId).value = "";
    } catch (error) {
        console.error("Error in bid function:", error);
        alert("Error placing bid: " + error.message);
    }
}

const auctions = {}; // Local state
const countdownIntervals = {}; // To track countdown timers

function updateAuction(msg) {
    let id = msg.auctionId;
    let box;

    if (!auctions[id]) {
        box = document.createElement("div");
        box.className = "auction-box";
        box.id = id;
        document.getElementById("auctions").appendChild(box);
        auctions[id] = { ...msg };
    } else {
        box = document.getElementById(id);
        auctions[id] = { ...auctions[id], ...msg };
    }

    // Clear any existing countdown interval
    if (countdownIntervals[id]) {
        clearInterval(countdownIntervals[id]);
        delete countdownIntervals[id];
    }
    
    if (msg.type === "expire" || msg.status === "expired") {
        // Auction has expired
        let winner = msg.sender && msg.sender !== "No winner" ? msg.sender : "No winner";
        box.innerHTML = `
            <div class="expired-auction">
                <strong>${msg.itemName}</strong> - AUCTION ENDED<br>
                Final Price: $${msg.price.toFixed(2)}<br>
                Winner: ${winner}
            </div>
        `;
    } else {
        // Active auction with countdown
        updateActiveAuction(box, msg, id);
        
        // Set up countdown timer
        if (msg.expirationTime) {
            countdownIntervals[id] = setInterval(() => {
                updateCountdown(id, msg.expirationTime);
            }, 1000);
            
            // Initial update
            updateCountdown(id, msg.expirationTime);
        }
    }
}

function updateActiveAuction(box, msg, id) {
    box.innerHTML = `
        <strong>${msg.itemName}</strong><br>
        Current Bid: $${msg.price.toFixed(2)} ${msg.sender ? `by ${msg.sender}` : ""}<br>
        <div id="countdown-${id}">Time remaining: calculating...</div>
        <div class="bid-form">
            <input type="number" id="bid-${id}" placeholder="Your Bid" min="${msg.price + 0.01}" step="0.01">
            <button onclick="bid('${id}')">Place Bid</button>
        </div>
    `;
}

function updateCountdown(id, expirationTime) {
    const countdownElement = document.getElementById(`countdown-${id}`);
    if (!countdownElement) {
        // Element doesn't exist anymore, clear the interval
        if (countdownIntervals[id]) {
            clearInterval(countdownIntervals[id]);
            delete countdownIntervals[id];
        }
        return;
    }
    
    const now = Date.now();
    const timeLeft = expirationTime - now;
    
    if (timeLeft <= 0) {
        countdownElement.innerHTML = "Auction has ended";
        clearInterval(countdownIntervals[id]);
        delete countdownIntervals[id];
        return;
    }
    
    // Calculate hours, minutes, seconds
    const hours = Math.floor(timeLeft / (1000 * 60 * 60));
    const minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
    
    countdownElement.innerHTML = `Time remaining: ${hours}h ${minutes}m ${seconds}s`;
}
