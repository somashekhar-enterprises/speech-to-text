let mediaRecorder;
let recordedChunks = [];
let isRecording = false;

const startButton = document.getElementById('startButton');
const stopButton = document.getElementById('stopButton');
const responseDiv = document.getElementById('response');

startButton.addEventListener('click', startRecording);
stopButton.addEventListener('click', stopRecording);

function startRecording() {
    navigator.mediaDevices.getUserMedia({ audio: true })
        .then(function(stream) {
            mediaRecorder = new MediaRecorder(stream);
            mediaRecorder.ondataavailable = handleDataAvailable;
            mediaRecorder.start();
            recordedChunks = [];
            isRecording = true;
            startButton.disabled = true;
            stopButton.disabled = false;
            responseDiv.textContent = 'Recording...';
        })
        .catch(function(error) {
            console.error('Error accessing the microphone:', error);
        });
}

function stopRecording() {
    if (isRecording) {
        mediaRecorder.stop();
        isRecording = false;
        startButton.disabled = false;
        stopButton.disabled = true;
        responseDiv.textContent = 'Processing...';
    }
}

function handleDataAvailable(event) {
    if (event.data.size > 0) {
        recordedChunks.push(event.data);
    }
}

function sendAudioToServer(audioBlob) {
    const formData = new FormData();
    formData.append('audio', audioBlob);

    fetch('http://127.0.0.1:8080/api/v1/speechtotext/transcribe', {
        method: 'POST',
        body: formData
    })
    .then(function(response) {
        if (response.ok) {
            // Handle the streaming response from the server
            receiveStreamingResponse(response);
        } else {
            console.error('Failed to send audio:', response.statusText);
            responseDiv.textContent = 'Error sending audio';
        }
    })
    .catch(function(error) {
        console.error('Error sending the audio:', error);
        responseDiv.textContent = 'Error sending audio';
    });
}

function receiveStreamingResponse(response) {
    const reader = response.body.getReader();
    const decoder = new TextDecoder();

    function read() {
        return reader.read().then(({ done, value }) => {
            if (done) {
                console.log('Streaming response complete!');
                return;
            }

            // Process the received data and display it on the screen
            const message = decoder.decode(value);
            displayMessage(message);

            return read(); // Continue reading
        });
    }

    read().catch(error => {
        console.error('Error reading streaming response:', error);
        responseDiv.textContent = 'Error receiving response';
    });
}

function displayMessage(message) {
    // Display the received message on the screen (you can customize this part)
    responseDiv.textContent = message;
}
